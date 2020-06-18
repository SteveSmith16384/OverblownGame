package com.scs.splitscreenfps.game.systems;

import java.io.IOException;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.google.gson.JsonIOException;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
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

		if (keyboard.isShootPressed()) {
			btCollisionObject obj = game.rayTestByDir(player.camera.position, player.camera.direction, 100);
			selectedObject = (AbstractEntity)obj.userData;
			Settings.p(obj + " selected");
			
			MapBlockComponent block = (MapBlockComponent)this.selectedObject.getComponent(MapBlockComponent.class);
			if (block == null) {
				// todo - add block
			}
		}
		
		if (keyboard.isKeyJustPressed(Keys.NUM_1)) {
			// Save
			try {
				game.currentLevel.saveFile();
				Settings.p("Saved");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
