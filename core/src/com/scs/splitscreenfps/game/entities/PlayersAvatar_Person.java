package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
import com.scs.splitscreenfps.game.components.HasGuiSpriteComponent;
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
	private static final float MOVE_SPEED = 15;//20;//25;//1.5f;

	public PersonCameraController cameraController;
	private Vector3 tmpVector = new Vector3();
	private Vector2 tmpVec2 = new Vector2();

	public PlayersAvatar_Person(Game _game, int playerIdx, ViewportData _viewportData, IInputMethod _inputMethod) {
		super(_game.ecs, playerIdx, PlayersAvatar_Person.class.getSimpleName() + "_" + playerIdx);

		//game = _game;
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
		//player_body.setDamping(0.8f, 0.8f);
		player_body.setDamping(.9f, .9f);
		player_body.setAngularFactor(new Vector3(0, 0, 0)); // prevent the player from falling over
		PhysicsComponent physics = new PhysicsComponent(player_body);
		physics.removeIfFallen = false;
		physics.physicsControlsRotation = false;
		addComponent(physics);

		this.addComponent(new PositionComponent());

		camera = _viewportData.camera;
		cameraController = new PersonCameraController(camera, inputMethod);

		addComponent(new PlayerData(playerIdx));

		setAvatarColour(this, playerIdx);

		addComponent(new CanShoot());

		// Add crosshairs
		Texture weaponTex = new Texture(Gdx.files.internal("crosshairs.png"));		
		Sprite sprite = new Sprite(weaponTex);
		sprite.setPosition((Gdx.graphics.getWidth()-sprite.getWidth())/2, 0);		
		HasGuiSpriteComponent hgsc = new HasGuiSpriteComponent(sprite, HasGuiSpriteComponent.Z_CARRIED, new Rectangle(0.45f, 0.45f, 0.1f, 0.1f));
		addComponent(hgsc);

	}


	private void setAvatarColour(AbstractEntity e, int idx) {
		// Reset player colours
		HasModelComponent hasModel = (HasModelComponent)e.getComponent(HasModelComponent.class);
		ModelInstance instance = hasModel.model;
		for (int i=0 ; i<instance.materials.size ; i++) {
			switch (idx) {
			case 0:
				//instance.materials.get(i).set(ColorAttribute.createDiffuse(Color.RED));
				//instance.materials.get(i).set(ColorAttribute.createAmbient(Color.RED));
				break;
			case 1:
				instance.materials.get(i).set(ColorAttribute.createDiffuse(Color.MAGENTA));
				instance.materials.get(i).set(ColorAttribute.createAmbient(Color.MAGENTA));
				break;
			case 2:
				instance.materials.get(i).set(ColorAttribute.createDiffuse(Color.RED));
				instance.materials.get(i).set(ColorAttribute.createAmbient(Color.RED));
				break;
			case 3:
				instance.materials.get(i).set(ColorAttribute.createDiffuse(Color.YELLOW));
				instance.materials.get(i).set(ColorAttribute.createAmbient(Color.YELLOW));
				break;
			default:
				throw new RuntimeException("Todo");
			}
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
		AnimatedComponent anim = new AnimatedComponent(animation, "AlienArmature|Alien_Walk", "AlienArmature|Alien_Idle");
		anim.animationController = animation;
		this.addComponent(anim);

		return instance;
	}


	public void process() { // todo - move this to PlayerInputSystem
		checkMovementInput();
		cameraController.update();

		// Position camera
		if (Game.physics_enabled) {
			PositionComponent posData = (PositionComponent)this.getComponent(PositionComponent.class);
			camera.position.set(posData.position.x, posData.position.y + (PLAYER_HEIGHT/2)+Settings.CAM_OFFSET, posData.position.z);

			// Set rotation based on camera
			tmpVec2.set(camera.direction.x, camera.direction.z);
			posData.angle_y_degrees = -tmpVec2.angle();
		}
	}


	private void checkMovementInput() {
		if (Game.physics_enabled) {
			PlayerMovementData movementData = (PlayerMovementData)this.getComponent(PlayerMovementData.class);

			if (this.inputMethod.getForwards() > Settings.MIN_AXIS) {
				//Settings.p("Fwd:" + this.inputMethod.isForwardsPressed());
				tmpVector.set(camera.direction);
				tmpVector.y = 0;
				movementData.offset.add(tmpVector.nor().scl(this.inputMethod.getForwards() * MOVE_SPEED));
			} else if (this.inputMethod.getBackwards() > Settings.MIN_AXIS) {
				//Settings.p("Back:" + this.inputMethod.isBackwardsPressed());
				tmpVector.set(camera.direction);
				tmpVector.y = 0;
				movementData.offset.add(tmpVector.nor().scl(-MOVE_SPEED * this.inputMethod.getBackwards()));
			}
			if (this.inputMethod.getStrafeLeft() > Settings.MIN_AXIS) {
				tmpVector.set(camera.direction).crs(camera.up);
				tmpVector.y = 0;
				movementData.offset.add(tmpVector.nor().scl(-MOVE_SPEED * this.inputMethod.getStrafeLeft()));
			} else if (this.inputMethod.getStrafeRight() > Settings.MIN_AXIS) {
				tmpVector.set(camera.direction).crs(camera.up);
				tmpVector.y = 0;
				movementData.offset.add(tmpVector.nor().scl(MOVE_SPEED * this.inputMethod.getStrafeRight()));
			}

			if (this.inputMethod.isJumpPressed()) {
				movementData.jumpPressed = true;
			}
		} else {
			if (this.inputMethod.getForwards() > Settings.MIN_AXIS) {
				tmpVector.set(camera.direction);
				tmpVector.scl(0.01f);
				camera.position.add(tmpVector);
				//camera.update();
			} else if (this.inputMethod.getBackwards() > Settings.MIN_AXIS) {
				tmpVector.set(camera.direction);
				tmpVector.scl(-0.01f);
				camera.position.add(tmpVector);
				//camera.update();
			}
			if (this.inputMethod.getStrafeLeft() > Settings.MIN_AXIS) {
				tmpVector.set(camera.direction).crs(camera.up);
				tmpVector.y = 0;
				tmpVector.scl(-0.01f);
				camera.position.add(tmpVector);
			} else if (this.inputMethod.getStrafeRight() > Settings.MIN_AXIS) {
				tmpVector.set(camera.direction).crs(camera.up);
				tmpVector.y = 0;
				tmpVector.scl(0.01f);
				camera.position.add(tmpVector);
			}
		}
	}

}

