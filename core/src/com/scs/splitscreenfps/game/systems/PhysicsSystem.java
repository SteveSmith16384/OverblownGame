package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.AffectedByExplosionComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;

public class PhysicsSystem extends AbstractSystem {

	// Flags
	public static final short COLL_PLAYER = 8;
	public static final short COLL_PLAYERS_BULLET = 9;
	public static final short COLL_ALL = -1;


	private Game game;

	public PhysicsSystem(Game _game, BasicECS ecs) {
		super(ecs, PhysicsComponent.class);

		game = _game;
	}


	@Override
	public void process() {
		// Do nothing!
	}

	/*
	@Override
	public void processEntity(AbstractEntity e) {
		PhysicsComponent pc = (PhysicsComponent)e.getComponent(PhysicsComponent.class);
		if (pc.force != null) {
			//pc.body.applyCentralForce(pc.force.scl(1));
			pc.body.applyCentralImpulse(pc.force);
			pc.force = null;

		}
	}
	 */

	@Override
	public void addEntity(AbstractEntity e) {
		super.addEntity(e);

		PhysicsComponent pc = (PhysicsComponent)e.getComponent(PhysicsComponent.class);
		game.dynamicsWorld.addRigidBody(pc.body);

		if (pc.disable_gravity) {
			pc.body.setGravity(new Vector3());
		}
		if (pc.force != null) {
			//pc.body.applyCentralForce(pc.force.scl(1));
			pc.body.applyCentralImpulse(pc.force);
			//pc.force = null;
		}

	}


	@Override
	public void removeEntity(AbstractEntity e) {
		super.removeEntity(e);

		PhysicsComponent pc = (PhysicsComponent)e.getComponent(PhysicsComponent.class);
		game.dynamicsWorld.removeRigidBody(pc.body);
		pc.body.dispose();
	}


	public void explosion(Vector3 pos, float range) {
		Settings.p("Explosion at " + pos);
		// Loop through ents
		Matrix4 mat = new Matrix4();
		Vector3 vec = new Vector3();
		for(AbstractEntity e : this.entities) {
			AffectedByExplosionComponent aff = (AffectedByExplosionComponent)e.getComponent(AffectedByExplosionComponent.class);
			if (aff != null) {
				PhysicsComponent pc = (PhysicsComponent)e.getComponent(PhysicsComponent.class);
				pc.body.getWorldTransform(mat);
				mat.getTranslation(vec);
				float distance = vec.dst(pos);
				if (distance <= range) {
					// Todo - check the explision can see the target?
					pc.body.activate();
					pc.body.applyCentralImpulse(vec.cpy().sub(pos).nor().scl(3));
					Settings.p("Moving " + e.name);
				}
			}
		}
	}


}
