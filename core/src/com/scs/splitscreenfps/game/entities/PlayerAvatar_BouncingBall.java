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
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.input.IInputMethod;

public class PlayerAvatar_BouncingBall extends AbstractPlayersAvatar {

	private static final float DIAM = 0.8f;
	private static final float DAMPING = 0.5f;

	public PlayerAvatar_BouncingBall(Game _game, int playerIdx, Camera camera, int hero_id, IInputMethod _inputMethod, int health, float speed) {
		super(_game, playerIdx, PlayerAvatar_Person.class.getSimpleName() + "_" + playerIdx, camera, hero_id, _inputMethod, health, speed);

		// Model stuff
		Texture tex = game.getTexture("textures/set3_example_1.png");
		Material black_material = new Material(TextureAttribute.createDiffuse(tex));
		Model sphere_model = game.modelBuilder.createSphere(DIAM,  DIAM,  DIAM, 10, 10, black_material, VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);
		ModelInstance instance = new ModelInstance(sphere_model);
		HasModelComponent hasModel = new HasModelComponent(instance, 0, 0, 1f, true);
		hasModel.dontDrawInViewId = playerIdx;
		this.addComponent(hasModel);

		btSphereShape capsuleShape = new btSphereShape(DIAM/2);
		final Vector3 inertia = new Vector3(0, 0, 0);
		capsuleShape.calculateLocalInertia(1.0f, inertia);

		btDefaultMotionState motionState = new btDefaultMotionState();
		btRigidBody player_body = new btRigidBody(4f, motionState, capsuleShape, inertia);
		player_body.userData = this;
		player_body.setDamping(DAMPING, DAMPING);
		player_body.setRestitution(1f);
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
