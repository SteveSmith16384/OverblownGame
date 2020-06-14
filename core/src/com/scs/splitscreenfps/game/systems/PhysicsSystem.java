package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
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


	// todo - re-add
/*	@Override
	public void process() {
		// Do nothing!
	}
*/

	@Override
	public void processEntity(AbstractEntity e) {
		PhysicsComponent pc = (PhysicsComponent)e.getComponent(PhysicsComponent.class);
		if (pc.body.isDisposed()) {
			Settings.pe("Body disposed for " + e);
		}
		if (pc.body.getCollisionShape().isDisposed()) {
			Settings.pe("Shapedisposed for " + e);
		}
/*		if (pc.force != null) {
			//pc.body.applyCentralForce(pc.force.scl(1));
			pc.body.applyCentralImpulse(pc.force);
			pc.force = null;

		}*/
	}


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


}
