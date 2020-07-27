package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.PlayerCameraController;
import com.scs.splitscreenfps.game.components.CanShoot;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.components.PlayerMovementData;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.input.IInputMethod;

public class PlayerAvatar_Ball extends AbstractPlayersAvatar {

	private static final float DAMPING = 0.5f;

	public PlayerAvatar_Ball(Game _game, int playerIdx, Camera camera, int hero_id, IInputMethod _inputMethod, int health, float speed) {
		super(_game, playerIdx, PlayerAvatar_Person.class.getSimpleName() + "_" + playerIdx, camera, hero_id, _inputMethod, health, speed);

		float diam = .8f;
		// Model stuff
		Texture tex = game.getTexture("textures/set3_example_1.png");
		Material black_material = new Material(TextureAttribute.createDiffuse(tex));
		//ModelBuilder modelBuilder = new ModelBuilder();
		Model sphere_model = game.modelBuilder.createSphere(diam,  diam,  diam, 10, 10, black_material, VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);
		ModelInstance instance = new ModelInstance(sphere_model);
		HasModelComponent hasModel = new HasModelComponent(instance, 0, 0, 1f, true);
		hasModel.dontDrawInViewId = playerIdx;
		this.addComponent(hasModel);

		btSphereShape capsuleShape = new btSphereShape(diam/2);
		final Vector3 inertia = new Vector3(0, 0, 0);
		capsuleShape.calculateLocalInertia(1.0f, inertia);

		btDefaultMotionState motionState = new btDefaultMotionState();
		btRigidBody player_body = new btRigidBody(2f, motionState, capsuleShape, inertia);
		player_body.userData = this;
		player_body.setDamping(DAMPING, DAMPING);
		player_body.setRestitution(.8f);
		PhysicsComponent physics = new PhysicsComponent(player_body);
		physics.removeIfFallen = false;
		physics.physicsControlsRotation = false;
		addComponent(physics);

	}

	@Override
	public float getDefaultDamping() {
		return DAMPING;
	}


}
