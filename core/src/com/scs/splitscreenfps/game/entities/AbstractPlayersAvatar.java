package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.PlayerCameraController;
import com.scs.splitscreenfps.game.components.CanShoot;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.components.PlayerMovementData;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.input.IInputMethod;

import ssmith.libgdx.ModelFunctions;

public abstract class AbstractPlayersAvatar extends AbstractEntity {

	public static final float PLAYER_HEIGHT = 0.4f;

	public Camera camera;
	public IInputMethod inputMethod;
	public final int playerIdx, hero_id;
	public PlayerCameraController cameraController;
	protected Game game;
	public boolean controlled_connected = true;

	public AbstractPlayersAvatar(Game _game, int _playerIdx, String _name, Camera _camera, int _hero_id, IInputMethod _inputMethod, int health, float speed) {
		super(_game.ecs, _name);

		game = _game;
		playerIdx = _playerIdx;
		hero_id = _hero_id;

		inputMethod = _inputMethod;

		camera = _camera;//_viewportData.camera;
		cameraController = new PlayerCameraController(camera);
		if (Game.physics_enabled == false) {
			camera.position.set(0, 5, 0);
		}

		PlayerMovementData md = new PlayerMovementData(speed);
		this.addComponent(md);

		addComponent(new PlayerData(playerIdx, health));

		addComponent(new CanShoot());

		this.addComponent(new PositionComponent());
	}


	public void setAvatarColour() {
		HasModelComponent hasModel = (HasModelComponent)this.getComponent(HasModelComponent.class);
		ModelInstance instance = hasModel.model;
		ModelFunctions.setColour(instance, Settings.getColourForSide(playerIdx));
	}


	public abstract float getDefaultDamping();



}
