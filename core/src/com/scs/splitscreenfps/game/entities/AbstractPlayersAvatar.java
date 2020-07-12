package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.graphics.Camera;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.PersonCameraController;
import com.scs.splitscreenfps.game.input.IInputMethod;

public abstract class AbstractPlayersAvatar extends AbstractEntity {

	public Camera camera;
	public IInputMethod inputMethod;
	public int playerIdx;
	public PersonCameraController cameraController;

	public AbstractPlayersAvatar(BasicECS _ecs, int _playerIdx, String _name) {
		super(_ecs, _name);
		
		playerIdx = _playerIdx;
	}

}
