package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.PersonCameraController;
import com.scs.splitscreenfps.game.ViewportData;
import com.scs.splitscreenfps.game.components.AnimatedComponent;
import com.scs.splitscreenfps.game.components.CanShoot;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.components.PlayerMovementData;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.input.IInputMethod;

import ssmith.libgdx.ModelFunctions;

// This also moves the camera
public class PlayersAvatar_Person extends AbstractPlayersAvatar {

	public static final float PLAYER_HEIGHT = 0.4f;
	public static final float DAMPING = 0.9f;

	public PlayersAvatar_Person(Game _game, int playerIdx, ViewportData _viewportData, IInputMethod _inputMethod) {
		super(_game.ecs, playerIdx, PlayersAvatar_Person.class.getSimpleName() + "_" + playerIdx);

		inputMethod = _inputMethod;

		PlayerMovementData md = new PlayerMovementData();
		this.addComponent(md);

		// Model stuff
		this.addAlienModel(playerIdx);

		btCapsuleShape capsuleShape = new btCapsuleShape(0.2f, PLAYER_HEIGHT);
		final Vector3 inertia = new Vector3(0, 0, 0);
		capsuleShape.calculateLocalInertia(1.0f, inertia);

		btDefaultMotionState motionState = new btDefaultMotionState();
		btRigidBody player_body = new btRigidBody(2f, motionState, capsuleShape, inertia);
		player_body.userData = this;
		player_body.setDamping(DAMPING, DAMPING);
		player_body.setAngularFactor(new Vector3(0, 0, 0)); // prevent the player from falling over
		PhysicsComponent physics = new PhysicsComponent(player_body);
		physics.removeIfFallen = false;
		physics.physicsControlsRotation = false;
		addComponent(physics);

		this.addComponent(new PositionComponent());

		camera = _viewportData.camera;
		cameraController = new PersonCameraController(camera, inputMethod);
		if (Game.physics_enabled == false) {
			camera.position.set(0, 5, 0);
		}
		addComponent(new PlayerData(playerIdx));

		setAvatarColour(this, playerIdx);

		addComponent(new CanShoot());

	}


	private void setAvatarColour(AbstractEntity e, int idx) {
		// Reset player colours
		HasModelComponent hasModel = (HasModelComponent)e.getComponent(HasModelComponent.class);
		ModelInstance instance = hasModel.model;
		for (int i=0 ; i<instance.materials.size ; i++) {
			instance.materials.get(i).set(ColorAttribute.createDiffuse(Settings.getColourForSide(idx)));
			instance.materials.get(i).set(ColorAttribute.createAmbient(Settings.getColourForSide(idx)));
		}
	}


	private ModelInstance addAlienModel(int playerIdx) {
		ModelInstance instance = ModelFunctions.loadModel("models/quaternius/Alien.g3db", false, null, 1f);
		float scale = ModelFunctions.getScaleForHeight(instance, .8f);
		instance.transform.scl(scale);
		//Vector3 offset = ModelFunctions.getOrigin(instance);
		//offset.y -= .9f; // Hack since model is too high

		HasModelComponent hasModel = new HasModelComponent(instance, -0.4f, 90, scale, true);
		hasModel.dontDrawInViewId = playerIdx;
		this.addComponent(hasModel);

		AnimationController animation = new AnimationController(instance);
		AnimatedComponent anim = new AnimatedComponent(animation, "AlienArmature|Alien_Walk", "AlienArmature|Alien_Idle", "AlienArmature|Alien_Death", "AlienArmature|Alien_Jump");
		anim.animationController = animation;
		this.addComponent(anim);

		return instance;
	}



}

