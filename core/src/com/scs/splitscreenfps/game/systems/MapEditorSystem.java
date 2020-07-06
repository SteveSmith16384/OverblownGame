package com.scs.splitscreenfps.game.systems;

import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractEvent;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.FallenOffEdgeEvent;
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

	private enum Mode {ROTATION, POSITION, SIZE, TEXTURE, MASS};
	
	private float MOVE_INC = 0.2f;

	private Game game;
	private Mode mode = Mode.POSITION;
	private AbstractEntity selectedObject;

	public MapEditorSystem(BasicECS ecs, Game _game) {
		super(ecs, PlayerData.class);

		game = _game;

		game.physics_enabled = false;
	}

	
	public void process() {
		/*List<AbstractEvent> colls = ecs.getEvents(FallenOffEdgeEvent.class);
		for (AbstractEvent evt : colls) {
			FallenOffEdgeEvent event = (FallenOffEdgeEvent)evt;

			for (int i=game.currentLevel.mapdata.blocks.size()-1 ; i>= 0 ; i--) {
				MapBlockComponent block = game.currentLevel.mapdata.blocks.get(i);
				if (event.entity1 == block.e
			}

			
		}*/
		super.process();
	}
	

	@Override
	public void processEntity(AbstractEntity entity) {
		AbstractPlayersAvatar player = (AbstractPlayersAvatar)entity;

		if (player.inputMethod instanceof MouseAndKeyboardInputMethod == false) {
			return;
		}

		MouseAndKeyboardInputMethod keyboard = (MouseAndKeyboardInputMethod)player.inputMethod;

		if (keyboard.isMouseClicked()) {
			btCollisionObject obj = game.rayTestByDir(player.camera.position, player.camera.direction, 100);
			selectedObject = null;
			if (obj != null) {
				selectedObject = (AbstractEntity)obj.userData;

				MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
				if (block != null) {
					Settings.p(block.name + " selected");
					game.appendToLog("Selected: " + block.id);
					game.appendToLog("Mass=" + block.mass);
				} else {
					selectedObject = null;
				}
			} else {
				Settings.p("Nothing selected");
			}
		}

		if (keyboard.isKeyJustPressed(Keys.NUM_1)) { // Save
			this.saveMap();
			game.appendToLog("Map saved");
		}

		if (keyboard.isKeyJustPressed(Keys.P)) { // Position mode
			mode = Mode.POSITION;
			game.appendToLog("Position mode selected");
		} else if (keyboard.isKeyJustPressed(Keys.B)) { // Size mode
			mode = Mode.SIZE;
			game.appendToLog("Sizen mode selected");
		} else if (keyboard.isKeyJustPressed(Keys.R)) { // rotation mode
			mode = Mode.ROTATION;
			game.appendToLog("Rotation mode selected");
		} else if (keyboard.isKeyJustPressed(Keys.T)) { // Textures
			mode = Mode.TEXTURE;
			game.appendToLog("Texture mode selected");
		} else if (keyboard.isKeyJustPressed(Keys.M)) { // Mass
			mode = Mode.MASS;
			game.appendToLog("Mass mode selected");
		} else if (keyboard.isKeyJustPressed(Keys.G)) { // Toggle physics
			game.physics_enabled = !game.physics_enabled;
			game.appendToLog("Physics enabled: " + game.physics_enabled);
		} else if (keyboard.isKeyJustPressed(Keys.N)) {  // New block
			MapBlockComponent block = new MapBlockComponent();
			block.size = new Vector3(1, 1, 1);
			block.position = new Vector3(5, 5, 5);
			block.type = "cube";
			block.name = "New cube " + NumberFunctions.rnd(1, 100);
			this.selectedObject = game.currentLevel.createAndAddEntityFromBlockData(block);
			game.currentLevel.mapdata.blocks.add(block);
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
			} else if (keyboard.isKeyJustPressed(Keys.NUM_0)) { // Reset rotation
				reAlignBlock();
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
				case MASS:
					this.changeMass(1);
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
				case MASS:
					this.changeMass(-1);
					break;
				default:
					game.appendToLog("Unknown mode: " + mode);
				}
			}
		} else {
			//game.appendToLog("No object selected");
		}
	}


	private void moveBlock(Vector3 off) {
		MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
		Matrix4 mat = this.setBlockDataFromPhysicsData(block);
		PhysicsComponent md = (PhysicsComponent)selectedObject.getComponent(PhysicsComponent.class);
		block.position.add(off);
		mat.setTranslation(block.position);
		md.body.setWorldTransform(mat);
		md.body.activate();
	}


	private void resizeBlock(Vector3 adj) {
		MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
		//this.setBlockDataFromPhysicsData(block);

		block.size.add(adj); 
		this.selectedObject.remove();
		this.selectedObject = game.currentLevel.createAndAddEntityFromBlockData(block);
	}


	private void rotateBlock(Vector3 adj) {
		MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
		//this.setBlockDataFromPhysicsData(block);

		block.rotation.add(adj);
		this.selectedObject.remove();
		this.selectedObject = game.currentLevel.createAndAddEntityFromBlockData(block);
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
		//this.setBlockDataFromPhysicsData(block);

		block.mass += off;
		if (block.mass < 0) {
			block.mass = 0;
		}
		game.appendToLog("Mass=" + block.mass);
	}


	private void reAlignBlock() {
		MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
		this.setBlockDataFromPhysicsData(block);

		//todo block.size.x = (int)
		block.rotation.set(0, 0, 0);
		this.selectedObject.remove();
		this.selectedObject = game.currentLevel.createAndAddEntityFromBlockData(block);
	}


	// Required since physics may well have moved the position of the block
	private Matrix4 setBlockDataFromPhysicsData(MapBlockComponent block) {
		// Set matrix to current pos
		PhysicsComponent md = (PhysicsComponent)selectedObject.getComponent(PhysicsComponent.class);
		Matrix4 mat = md.body.getWorldTransform();
		mat.getTranslation(block.position);
		return mat;

	}


	public void saveMap() {
		try {
			// loop through each block and set the model data to the position/size/rot of the block
			/*Iterator<AbstractEntity> it = game.ecs.getEntityIterator();
			while (it.hasNext()) {
				AbstractEntity e = it.next();
				MapBlockComponent block = (MapBlockComponent)e.getComponent(MapBlockComponent.class);
				if (block != null) {
					this.setBlockDataFromPhysicsData(block);
				}
			}*/

			// Remove blocks that have fallen ofd the edge
			/*for (int i=game.currentLevel.mapdata.blocks.size()-1 ; i>= 0 ; i--) {
				MapBlockComponent block = game.currentLevel.mapdata.blocks.get(i);
				if (block.position.y < 0) {
					game.currentLevel.mapdata.blocks.remove(i);
				}
			}*/

			game.currentLevel.saveFile();
			Settings.p("Map saved");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
