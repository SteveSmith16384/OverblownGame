package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
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

	/*
	@Override
	public void process() {
		// Do nothing!
	}*/
	
	
	@Override
	public void processEntity(AbstractEntity e) {
		PhysicsComponent pc = (PhysicsComponent)e.getComponent(PhysicsComponent.class);
		if (e.name == "Bullet") {
			//pc.body.applyCentralForce(new Vector3(0, 2, 0));
		}
		if (pc.force != null) {
			//pc.body.applyCentralForce(pc.force);
			pc.body.applyCentralImpulse(pc.force);
			pc.force = null;			
		}
	}

	
	@Override
	public void addEntity(AbstractEntity e) {
		super.addEntity(e);
		
		PhysicsComponent pc = (PhysicsComponent)e.getComponent(PhysicsComponent.class);
		game.dynamicsWorld.addRigidBody(pc.body);
	}

	
	@Override
	public void removeEntity(AbstractEntity e) {
		super.removeEntity(e);
		
		PhysicsComponent pc = (PhysicsComponent)e.getComponent(PhysicsComponent.class);
		game.dynamicsWorld.removeRigidBody(pc.body);
		pc.body.dispose();
	}
	
}
