package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.PersonCameraController;
import com.scs.splitscreenfps.game.components.DrawTextIn3DSpaceComponent;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.input.IInputMethod;

public abstract class AbstractPlayersAvatar extends AbstractEntity {

	public static final float PLAYER_HEIGHT = 0.4f;

	public Camera camera;
	public IInputMethod inputMethod;
	public int playerIdx;
	public PersonCameraController cameraController;
	protected Game game;
	public boolean controlled_connected = true;
	
	public AbstractPlayersAvatar(Game _game, int _playerIdx, String _name) {
		super(_game.ecs, _name);
		
		game = _game;
		playerIdx = _playerIdx;
		
		DrawTextIn3DSpaceComponent text = new DrawTextIn3DSpaceComponent("", new Vector3(0, .3f, 0), 4, playerIdx);
		this.addComponent(text);

	}

	
	protected void setAvatarColour(AbstractEntity e, int idx) {
		// Reset player colours
		HasModelComponent hasModel = (HasModelComponent)e.getComponent(HasModelComponent.class);
		ModelInstance instance = hasModel.model;
		for (int i=0 ; i<instance.materials.size ; i++) {
			instance.materials.get(i).set(ColorAttribute.createDiffuse(Settings.getColourForSide(idx)));
			instance.materials.get(i).set(ColorAttribute.createAmbient(Settings.getColourForSide(idx)));
		}
	}
	
	
	public abstract float getDefaultDamping();



}
