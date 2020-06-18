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
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.input.MouseAndKeyboardInputMethod;
import com.scs.splitscreenfps.game.mapdata.MapBlockComponent;

public class MapBuilderSystem extends AbstractSystem {

	private Game game;

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
			if (keyboard.isKeyJustPressed(Keys.F)) {
				MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);

				// Move left
				PhysicsComponent md = (PhysicsComponent)selectedObject.getComponent(PhysicsComponent.class);
				Matrix4 mat = new Matrix4();
				md.body.getWorldTransform(mat);
				mat.getTranslation(block.position);
				block.position.x -= 0.1f;
				mat.setTranslation(block.position);
				md.body.setWorldTransform(mat);
				md.body.activate();

			}
		}
	}

}
