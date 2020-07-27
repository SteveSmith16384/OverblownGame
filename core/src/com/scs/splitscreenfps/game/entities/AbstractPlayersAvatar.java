package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.PlayerCameraController;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.input.IInputMethod;

public abstract class AbstractPlayersAvatar extends AbstractEntity {

	public static final float PLAYER_HEIGHT = 0.4f;

	public Camera camera;
	public IInputMethod inputMethod;
	public final int playerIdx, hero_id;
	public PlayerCameraController cameraController;
	protected Game game;
	public boolean controlled_connected = true;
	
	public AbstractPlayersAvatar(Game _game, int _playerIdx, String _name, int _hero_id) {
		super(_game.ecs, _name);
		
		game = _game;
		playerIdx = _playerIdx;
		hero_id = _hero_id;
	}

	
	protected void setAvatarColour(int character) {
		if (character == AvatarFactory.CHAR_PIGGY) {
			this.setColour(Color.RED);
		} else if (character == AvatarFactory.CHAR_VICTIM) {
			this.setColour(Color.GREEN);
		} else {
			this.setColour(Settings.getColourForSide(playerIdx));
		}
	}
	

	private void setColour(Color c) {
		HasModelComponent hasModel = (HasModelComponent)this.getComponent(HasModelComponent.class);
		ModelInstance instance = hasModel.model;
		for (int i=0 ; i<instance.materials.size ; i++) {
			instance.materials.get(i).set(ColorAttribute.createDiffuse(c));
			instance.materials.get(i).set(ColorAttribute.createAmbient(c));
		}
	}
	
	public abstract float getDefaultDamping();



}
