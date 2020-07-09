package com.scs.splitscreenfps.game.systems;

import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.input.MouseAndKeyboardInputMethod;
import com.scs.splitscreenfps.game.mapdata.MapBlockComponent;

import ssmith.lang.NumberFunctions;

/**
 * Limitations of the map editor:-
 * All the data is stored in MapBlockData, since it's not possible to get the l/w/h from a physics body, especially when its rotated.
 * However, we do get the position from the physics body.
 *
 */
public class MapEditorSystem extends AbstractSystem {

	private enum Mode {ROTATION, POSITION, SIZE, TEXTURE, MASS, NEW_BLOCK};

	private float MOVE_INC = 0.25f;

	private Game game;
	private Mode mode = Mode.POSITION;
	private AbstractEntity selectedObject;
	private MouseAndKeyboardInputMethod keyboard;

	// Settle stats
	private float mass_cache;
	private long settle_end_time;
	private AbstractEntity settle_block;

	public MapEditorSystem(BasicECS ecs, Game _game) {
		super(ecs, PlayerData.class);

		game = _game;

		game.physics_enabled = false;
	}


	public void process() {
		if (settle_block != null) {
			if (this.settle_end_time < System.currentTimeMillis()) {
				MapBlockComponent block = (MapBlockComponent)settle_block.getComponent(MapBlockComponent.class);
				this.setBlockDataFromPhysicsData(settle_block, block);
				block.mass = this.mass_cache;
				this.settle_block.remove();
				this.selectedObject = game.currentLevel.createAndAddEntityFromBlockData(block);
				this.settle_block = null;
				game.appendToLog("Block settled.");
			}
		}
		super.process();
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		AbstractPlayersAvatar player = (AbstractPlayersAvatar)entity;

		if (player.inputMethod instanceof MouseAndKeyboardInputMethod == false) {
			return;
		}

		keyboard = (MouseAndKeyboardInputMethod)player.inputMethod;

		if (keyboard.isMouseClicked()) {
			selectedObject = null;
			btCollisionObject obj = game.rayTestByDir(player.camera.position, player.camera.direction, 100);
			if (obj != null) {
				selectedObject = (AbstractEntity)obj.userData;

				MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
				if (block != null) {
					Settings.p(block.name + " selected");
					game.appendToLog("Selected: " + block.name + " (" + block.id + ")");
					game.appendToLog("Mass=" + block.mass);
				} else {
					game.appendToLog("Block not found");
					selectedObject = null;
				}
			} else {
				game.appendToLog("Nothing selected");
			}
		}

		if (keyboard.isKeyJustPressed(Keys.NUM_1)) { // Save
				this.saveMap();
				game.appendToLog("Map saved");
		} else if (keyboard.isKeyJustPressed(Keys.NUM_2)) { // settle
				if (game.physics_enabled == false) {
					game.appendToLog("Physics must be enabled");
				} else {
					settleBlock();
				}
		} else if (keyboard.isKeyJustPressed(Keys.NUM_3)) { // Show pos
				game.appendToLog("Cam pos: " + player.camera.position);
		} else if (keyboard.isKeyJustPressed(Keys.NUM_5)) { // Draw physics
			Settings.DRAW_PHYSICS = !Settings.DRAW_PHYSICS;
		} else if (keyboard.isKeyJustPressed(Keys.P)) { // Position mode
			mode = Mode.POSITION;
			game.appendToLog("Position mode selected");
			if (this.selectedObject != null) {
				MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
				game.appendToLog("Position: " + block.position);
			}
		} else if (keyboard.isKeyJustPressed(Keys.B)) { // Size mode
			if (mode == Mode.NEW_BLOCK) {
				createNewBox();
			} 
			mode = Mode.SIZE;
			game.appendToLog("Size mode selected");
			if (this.selectedObject != null) {
				MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
				game.appendToLog("Size: " + block.size);
			}
		} else if (keyboard.isKeyJustPressed(Keys.R)) { // rotation mode
			mode = Mode.ROTATION;
			game.appendToLog("Rotation mode selected");
			if (this.selectedObject != null) {
				MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
				game.appendToLog("Rotation: " + block.rotation);
			}
		} else if (keyboard.isKeyJustPressed(Keys.T)) { // Textures
			mode = Mode.TEXTURE;
			game.appendToLog("Texture mode selected");
		} else if (keyboard.isKeyJustPressed(Keys.M)) { // Mass
			mode = Mode.MASS;
			game.appendToLog("Mass mode selected");
			if (this.selectedObject != null) {
				MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
				game.appendToLog("Current mass: " + block.mass);
			}
		} else if (keyboard.isKeyJustPressed(Keys.G)) { // Toggle physics
			game.physics_enabled = !game.physics_enabled;
			//Settings.DEBUG_PHYSICS = game.physics_enabled;
			game.appendToLog("Physics enabled: " + game.physics_enabled);
		} else if (keyboard.isKeyJustPressed(Keys.N)) {  // New block
			this.mode = Mode.NEW_BLOCK;
			game.appendToLog("Press B, C or E.");
		} else if (keyboard.isKeyJustPressed(Keys.C)) {  // New cylinder
			if (mode == Mode.NEW_BLOCK) {
				createNewCylinder();
				mode = Mode.POSITION;
			}
		} else if (keyboard.isKeyJustPressed(Keys.E)) {  // New sphere
			if (mode == Mode.NEW_BLOCK) {
				createNewSphere();
				mode = Mode.POSITION;
			}
		}

		if (selectedObject != null) {
			if (keyboard.isKeyJustPressed(Keys.C)) { // Clone
				MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
				MapBlockComponent new_block = block.clone();
				this.selectedObject = game.currentLevel.createAndAddEntityFromBlockData(new_block);
				game.currentLevel.mapdata.blocks.add(new_block);
			} else if (keyboard.isKeyJustPressed(Keys.X)) { // Remove
				if (keyboard.isKeyPressed(Keys.SHIFT_LEFT)) {
					MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
					game.currentLevel.mapdata.blocks.remove(block);
					this.selectedObject.remove();
					game.appendToLog("Block removed");
					this.selectedObject = null;
				} else {
					game.appendToLog("Press Shift to remove block");
				}
			} else if (keyboard.isKeyJustPressed(Keys.U)) { // Undo
				MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
				this.selectedObject.remove();
				this.selectedObject = game.currentLevel.createAndAddEntityFromBlockData(block);
			} else if (keyboard.isKeyJustPressed(Keys.NUM_0)) { // Reset rotation/position/size
				reAlignBlock();
				game.appendToLog("Block re-aligned");
			} else if (keyboard.isKeyJustPressed(Keys.LEFT)) {
				switch (mode) {
				case POSITION:
					this.moveBlock(new Vector3(MOVE_INC, 0, 0));
					break;
				case SIZE:
					this.resizeBlock(new Vector3(MOVE_INC, 0, 0));
					break;
				case ROTATION:
					this.rotateBlock(new Vector3(10, 0, 0));
					break;
				case TEXTURE:
					this.rotateTexture(-1);
					break;
				default:
					game.appendToLog("Unknown mode: " + mode);
				}
			} else if (keyboard.isKeyJustPressed(Keys.UP)) {
				switch (mode) {
				case POSITION:
					this.moveBlock(new Vector3(0, 0, MOVE_INC));
					break;
				case SIZE:
					this.resizeBlock(new Vector3(0, 0, MOVE_INC));
					break;
				case ROTATION:
					this.rotateBlock(new Vector3(0, 0, 10));
					break;
				case MASS:
					this.changeMass(1);
					break;
				default:
					game.appendToLog("Unknown mode: " + mode);
				}
			} else if (keyboard.isKeyJustPressed(Keys.RIGHT)) {
				switch (mode) {
				case POSITION:
					this.moveBlock(new Vector3(-MOVE_INC, 0, 0));
					break;
				case SIZE:
					this.resizeBlock(new Vector3(-MOVE_INC, 0, 0));
					break;
				case ROTATION:
					this.rotateBlock(new Vector3(-10, 0, 0));
					break;
				case TEXTURE:
					this.rotateTexture(1);
					break;
				default:
					game.appendToLog("Unknown mode: " + mode);
				}
			} else if (keyboard.isKeyJustPressed(Keys.DOWN)) {
				switch (mode) {
				case POSITION:
					this.moveBlock(new Vector3(0, 0, -MOVE_INC));
					break;
				case SIZE:
					this.resizeBlock(new Vector3(0, 0, -MOVE_INC));
					break;
				case ROTATION:
					this.rotateBlock(new Vector3(0, 0, -10));
					break;
				case MASS:
					this.changeMass(-1);
					break;
				default:
					game.appendToLog("Unknown mode: " + mode);
				}
			} else if (keyboard.isKeyJustPressed(Keys.PAGE_UP)) {
				switch (mode) {
				case POSITION:
					this.moveBlock(new Vector3(0, MOVE_INC, 0));
					break;
				case SIZE:
					this.resizeBlock(new Vector3(0, MOVE_INC, 0));
					break;
				case ROTATION:
					this.rotateBlock(new Vector3(0, 10, 0));
					break;
				default:
					game.appendToLog("Unknown mode: " + mode);
				}
			} else if (keyboard.isKeyJustPressed(Keys.PAGE_DOWN)) {
				switch (mode) {
				case POSITION:
					this.moveBlock(new Vector3(0, -MOVE_INC, 0));
					break;
				case SIZE:
					this.resizeBlock(new Vector3(0, -MOVE_INC, 0));
					break;
				case ROTATION:
					this.rotateBlock(new Vector3(0, -10, 0));
					break;
				default:
					game.appendToLog("Unknown mode: " + mode);
				}
			}
		} else {
			//game.appendToLog("No object selected");
		}
	}


	private void createNewBox() {
		MapBlockComponent block = new MapBlockComponent();
		block.size = new Vector3(1, 1, 1);
		block.position = new Vector3(0, 5, 0);
		block.type = "cube";
		block.name = "New cube " + NumberFunctions.rnd(1, 100);
		block.mass = 0;
		this.selectedObject = game.currentLevel.createAndAddEntityFromBlockData(block);
		game.currentLevel.mapdata.blocks.add(block);
		this.settleBlock(); // Need this to add it to the physics world, so it can be selected!
		game.appendToLog(block.name + " created");
	}
	
	
	private void createNewSphere() {
		MapBlockComponent block = new MapBlockComponent();
		block.size = new Vector3(1, 1, 1);
		block.position = new Vector3(0, 5, 0);
		block.type = "sphere";
		block.name = "New sphere " + NumberFunctions.rnd(1, 100);
		block.mass = 0;
		this.selectedObject = game.currentLevel.createAndAddEntityFromBlockData(block);
		game.currentLevel.mapdata.blocks.add(block);
		this.settleBlock(); // Need this to add it to the physics world, so it can be selected!
		game.appendToLog(block.name + " created");
	}
	
	
	private void createNewCylinder() {
		MapBlockComponent block = new MapBlockComponent();
		block.size = new Vector3(1, 1, 1);
		block.position = new Vector3(0, 5, 0);
		block.type = "cylinder";
		block.name = "New cylinder " + NumberFunctions.rnd(1, 100);
		block.mass = 0;
		this.selectedObject = game.currentLevel.createAndAddEntityFromBlockData(block);
		game.currentLevel.mapdata.blocks.add(block);
		this.settleBlock(); // Need this to add it to the physics world, so it can be selected!
		game.appendToLog(block.name + " created");
	}
	
	/*
	private void setStartPos(int id, Vector3 pos) {
		if (game.currentLevel.mapdata.start_positions == null) {
			game.currentLevel.mapdata.start_positions = new HashMap<Integer, Vector3>();
		}
		//Vector3 pos = player.camera.position;
		game.currentLevel.mapdata.start_positions.put(id,  pos);
		game.appendToLog("Start pos " + (id+1) + " set to " + pos);
		this.mode = Mode.POSITION;
	}
	*/

	private void settleBlock() {
		if (selectedObject == null) {
			game.appendToLog("No block selected");
			return;
		}
		MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
		if (block.mass > 0) {
			game.appendToLog("Block already has mass");
			return;
		}
		this.settle_end_time = System.currentTimeMillis() + 2000;
		this.mass_cache = block.mass;
		block.mass = 1;
		this.selectedObject.remove();
		this.selectedObject = game.currentLevel.createAndAddEntityFromBlockData(block);
		settle_block = this.selectedObject;
		game.appendToLog("Settling block...");
	}


	private void moveBlock(Vector3 off) {
		if (keyboard.isKeyPressed(Keys.CONTROL_LEFT)) {
			off.scl(.1f);
		}

		MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
		Matrix4 mat = this.setBlockDataFromPhysicsData(selectedObject, block);
		PhysicsComponent md = (PhysicsComponent)selectedObject.getComponent(PhysicsComponent.class);
		block.position.add(off);
		mat.setTranslation(block.position);
		md.body.setWorldTransform(mat);
		md.body.activate();
		game.appendToLog("New position: " + block.position);
	}


	private void resizeBlock(Vector3 adj) {
		if (keyboard.isKeyPressed(Keys.CONTROL_LEFT)) {
			adj.scl(.1f);
		}

		MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
		//this.setBlockDataFromPhysicsData(block);

		block.size.add(adj);
		if (keyboard.isKeyPressed(Keys.SHIFT_LEFT)) {
			// Only adjust one side
			block.position.add(adj.scl(.5f));
		}
		this.selectedObject.remove();
		this.selectedObject = game.currentLevel.createAndAddEntityFromBlockData(block);
		game.appendToLog("New size: " + block.size);
	}


	private void rotateBlock(Vector3 adj) {
		if (keyboard.isKeyPressed(Keys.CONTROL_LEFT)) {
			adj.scl(.1f);
		}

		MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
		//this.setBlockDataFromPhysicsData(block);

		block.rotation.add(adj);
		this.selectedObject.remove();
		this.selectedObject = game.currentLevel.createAndAddEntityFromBlockData(block);

		game.appendToLog("New Rotation: " + block.rotation);
	}


	private void rotateTexture(int off) {
		MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
		//this.setBlockDataFromPhysicsData(block);

		int max = -1;
		Iterator<Integer> it = game.currentLevel.mapdata.textures.keySet().iterator();
		while (it.hasNext()) {
			max = it.next();
		}
		block.texture_id += off;
		if (block.texture_id < 0) {
			block.texture_id = max;
		} else if (block.texture_id > max) {
			block.texture_id = 0;
		}
		this.selectedObject.remove();
		this.selectedObject = game.currentLevel.createAndAddEntityFromBlockData(block);
	}


	private void changeMass(int off) {
		MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
		this.setBlockDataFromPhysicsData(this.selectedObject, block);

		block.mass += off;
		if (block.mass < 0) {
			block.mass = 0;
		}
		game.appendToLog("Mass=" + block.mass);

		// Recreate the block
		this.selectedObject.remove();
		this.selectedObject = game.currentLevel.createAndAddEntityFromBlockData(block);
	}


	private void reAlignBlock() {
		MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
		this.setBlockDataFromPhysicsData(selectedObject, block);

		block.position.scl(1/MOVE_INC);
		block.position.x = Math.round(block.position.x)*MOVE_INC; 
		block.position.y = Math.round(block.position.y)*MOVE_INC; 
		block.position.z = Math.round(block.position.z)*MOVE_INC; 

		block.size.scl(1/MOVE_INC);
		block.size.x = Math.round(block.size.x)*MOVE_INC; 
		block.size.y = Math.round(block.size.y)*MOVE_INC; 
		block.size.z = Math.round(block.size.z)*MOVE_INC; 

		block.rotation.set(0, 0, 0);
		this.selectedObject.remove();
		this.selectedObject = game.currentLevel.createAndAddEntityFromBlockData(block);
	}


	// Required since physics may well have moved the position of the block
	private Matrix4 setBlockDataFromPhysicsData(AbstractEntity entity, MapBlockComponent block) {
		// Set matrix to current pos
		PhysicsComponent md = (PhysicsComponent)entity.getComponent(PhysicsComponent.class);
		Matrix4 mat = md.body.getWorldTransform();
		mat.getTranslation(block.position);
		return mat;

	}


	public void saveMap() {
		try {
			// loop through each block and set the model data to the position of the block
			Iterator<AbstractEntity> it = game.ecs.getEntityIterator();
			while (it.hasNext()) {
				AbstractEntity e = it.next();
				MapBlockComponent block = (MapBlockComponent)e.getComponent(MapBlockComponent.class);
				if (block != null) {
					this.setBlockDataFromPhysicsData(e, block);
				}
			}

			game.currentLevel.saveFile();
			Settings.p("Map saved");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
