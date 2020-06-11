package com.scs.splitscreenfps.game.systems;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.PhysicsComponent;

public class PhysicsSystem extends AbstractSystem {
	
	private Game game;
	
	public PhysicsSystem(Game _game, BasicECS ecs) {
		super(ecs, PhysicsComponent.class);
		
		game = _game;
	}

	
	@Override
	public void process() {
		// Do nothing!
	}
	

	@Override
	public void addEntity(AbstractEntity e) {
		super.addEntity(e);
		
		PhysicsComponent pc = (PhysicsComponent)e.getComponent(PhysicsComponent.class);
		game.dynamicsWorld.addCollisionObject(pc.body);
	}

	
	@Override
	public void removeEntity(AbstractEntity e) {
		super.removeEntity(e);
		
		PhysicsComponent pc = (PhysicsComponent)e.getComponent(PhysicsComponent.class);
		game.dynamicsWorld.removeCollisionObject(pc.body);
	}
	
}
