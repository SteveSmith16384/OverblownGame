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

public class MapBuilderSystem extends AbstractSystem {

	private enum Mode {ROTATION, POSITION, SIZE};

	private Game game;
	private Mode mode;
	private AbstractEntity selectedObject;

	public MapBuilderSystem(BasicECS ecs, Game _game) {
		super(ecs, PlayerData.class);

		game = _game;
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
					// todo - add block
				}
			}
			Settings.p(obj + " selected");
		}

		if (keyboard.isKeyJustPressed(Keys.NUM_1)) { // Save
			try {
				game.currentLevel.saveFile();
				Settings.p("Saved");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (selectedObject != null) {
			if (keyboard.isKeyJustPressed(Keys.P)) {
				mode = Mode.POSITION;
				/*MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);

				// Move left
				PhysicsComponent md = (PhysicsComponent)selectedObject.getComponent(PhysicsComponent.class);
				Matrix4 mat = new Matrix4();
				md.body.getWorldTransform(mat);
				mat.getTranslation(block.position);
				block.position.x -= 0.1f;
				mat.setTranslation(block.position);
				md.body.setWorldTransform(mat);
				//md.body.activate();
				 */
			} else if (keyboard.isKeyJustPressed(Keys.S)) {
				mode = Mode.SIZE;
			} else if (keyboard.isKeyJustPressed(Keys.LEFT)) {
				switch (mode) {
				case POSITION:
					this.moveBlock(new Vector3(.1f, 0, 0));
					break;
				case SIZE:
					this.resizeBlock(new Vector3(.1f, 0, 0));
					break;
				}
			} else if (keyboard.isKeyJustPressed(Keys.RIGHT)) {
				switch (mode) {
				case POSITION:
					this.moveBlock(new Vector3(-.1f, 0, 0));
					break;
				case SIZE:
					this.resizeBlock(new Vector3(-.1f, 0, 0));
					break;
				}
			}
		}
	}


	private void moveBlock(Vector3 off) {
		MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);

		// Move left
		PhysicsComponent md = (PhysicsComponent)selectedObject.getComponent(PhysicsComponent.class);
		Matrix4 mat = new Matrix4();
		md.body.getWorldTransform(mat);
		mat.getTranslation(block.position);
		block.position.add(off);
		mat.setTranslation(block.position);
		md.body.setWorldTransform(mat);
	}
	

	private void resizeBlock(Vector3 adj) {
		// todo - set matix to current pos
		MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
		block.size.add(adj); // todo - adjust pos by half
		this.selectedObject.remove();
		this.selectedObject = this.createAndAddEntityFromBlockData(block);
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
