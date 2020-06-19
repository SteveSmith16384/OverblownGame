package com.scs.splitscreenfps.game.systems;

import java.util.Vector;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.EntityFactory;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.entities.Wall;
import com.scs.splitscreenfps.game.input.MouseAndKeyboardInputMethod;
import com.scs.splitscreenfps.game.mapdata.MapBlockComponent;

public class MapEditorSystem extends AbstractSystem {

	private enum Mode {ROTATION, POSITION, SIZE};

	private Game game;
	private Mode mode = Mode.POSITION;
	private AbstractEntity selectedObject;

	public MapEditorSystem(BasicECS ecs, Game _game) {
		super(ecs, PlayerData.class);

		game = _game;
	}


	public void saveMap() {
		try {
			game.currentLevel.saveFile();
			Settings.p("Saved");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		AbstractPlayersAvatar player = (AbstractPlayersAvatar)entity;

		MouseAndKeyboardInputMethod keyboard = (MouseAndKeyboardInputMethod)player.inputMethod;

		if (keyboard.isMouseClicked()) {
			btCollisionObject obj = game.rayTestByDir(player.camera.position, player.camera.direction, 100);
			selectedObject = null;
			if (obj != null) {
				selectedObject = (AbstractEntity)obj.userData;

				MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
				if (block == null) {
					// todo - add block?
				}
				Settings.p(block.name + " selected");
			} else {
				Settings.p("Nothing selected");
			}
		}

		if (keyboard.isKeyJustPressed(Keys.NUM_1)) { // Save
			this.saveMap();
		}

		if (selectedObject != null) {
			if (keyboard.isKeyJustPressed(Keys.P)) {
				mode = Mode.POSITION;
			} else if (keyboard.isKeyJustPressed(Keys.S)) {
				mode = Mode.SIZE;
			} else if (keyboard.isKeyJustPressed(Keys.R)) {
				mode = Mode.ROTATION;
			} else if (keyboard.isKeyJustPressed(Keys.N)) {
				MapBlockComponent block = new MapBlockComponent();
				block.size = new Vector3(1, 1, 1);
				block.texture_filename = "";
				this.selectedObject = this.createAndAddEntityFromBlockData(block);
			} else if (keyboard.isKeyJustPressed(Keys.LEFT)) {
				switch (mode) {
				case POSITION:
					this.moveBlock(new Vector3(.1f, 0, 0));
					break;
				case SIZE:
					this.resizeBlock(new Vector3(.1f, 0, 0));
					break;
				default:
					throw new RuntimeException("Todo");
				}
			} else if (keyboard.isKeyJustPressed(Keys.PAGE_UP)) {
				switch (mode) {
				case POSITION:
					this.moveBlock(new Vector3(0, 0, .1f));
					break;
				case SIZE:
					this.resizeBlock(new Vector3(0, 0, .1f));
					break;
				default:
					throw new RuntimeException("Todo");
				}
			} else if (keyboard.isKeyJustPressed(Keys.RIGHT)) {
				switch (mode) {
				case POSITION:
					this.moveBlock(new Vector3(-.1f, 0, 0));
					break;
				case SIZE:
					this.resizeBlock(new Vector3(-.1f, 0, 0));
					break;
				default:
					throw new RuntimeException("Todo");
				}
			} else if (keyboard.isKeyJustPressed(Keys.PAGE_DOWN)) {
				switch (mode) {
				case POSITION:
					this.moveBlock(new Vector3(0, 0, -.1f));
					break;
				case SIZE:
					this.resizeBlock(new Vector3(0, 0, -.1f));
					break;
				default:
					throw new RuntimeException("Todo");
				}
			} else if (keyboard.isKeyJustPressed(Keys.UP)) {
				switch (mode) {
				case POSITION:
					this.moveBlock(new Vector3(0, 0.1f, 0));
					break;
				case SIZE:
					this.resizeBlock(new Vector3(0, .1f, 0));
					break;
				default:
					throw new RuntimeException("Todo");
				}
			} else if (keyboard.isKeyJustPressed(Keys.DOWN)) {
				switch (mode) {
				case POSITION:
					this.moveBlock(new Vector3(0, -0.1f, 0));
					break;
				case SIZE:
					this.resizeBlock(new Vector3(0, -0.1f, 0));
					break;
				default:
					throw new RuntimeException("Todo");
				}
			}
		}
	}


	private void moveBlock(Vector3 off) {
		// Move left
		MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
		Matrix4 mat = this.setBlockDataToCurrentPosition(block);
		PhysicsComponent md = (PhysicsComponent)selectedObject.getComponent(PhysicsComponent.class);
		/*
		Matrix4 mat = new Matrix4();
		md.body.getWorldTransform(mat);

		mat.getTranslation(block.position);*/

		block.position.add(off);
		mat.setTranslation(block.position);
		md.body.setWorldTransform(mat);
	}


	private void resizeBlock(Vector3 adj) {
		MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
		this.setBlockDataToCurrentPosition(block);

		block.size.add(adj); 
		this.selectedObject.remove();
		this.selectedObject = this.createAndAddEntityFromBlockData(block);
	}


	private Matrix4 setBlockDataToCurrentPosition(MapBlockComponent block) {
		// Set matix to current pos
		PhysicsComponent md = (PhysicsComponent)selectedObject.getComponent(PhysicsComponent.class);
		Matrix4 mat = new Matrix4();
		md.body.getWorldTransform(mat);
		mat.getTranslation(block.position);
		return mat;

	}


	public AbstractEntity createAndAddEntityFromBlockData(MapBlockComponent block) {
		if (block.model_filename != null && block.model_filename.length() > 0) {
			AbstractEntity doorway = EntityFactory.Model(game.ecs, block.name, block.model_filename, 
					8, -2f, 7, 
					block.mass);
			doorway.addComponent(block);
			game.ecs.addEntity(doorway);
			return doorway;
		} else if (block.texture_filename != null && block.texture_filename.length() > 0) {
			Wall wall = new Wall(game.ecs, block.name, block.texture_filename, block.position.x, block.position.y, block.position.z, 
					block.size.x, block.size.y, block.size.z, 
					block.mass);
			wall.addComponent(block);
			game.ecs.addEntity(wall);
			return wall;
		}
		Settings.p("Ignoring line");
		return null;
	}
}
