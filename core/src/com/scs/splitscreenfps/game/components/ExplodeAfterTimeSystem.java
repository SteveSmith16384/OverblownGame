package com.scs.splitscreenfps.game.components;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.Game;

public class ExplodeAfterTimeSystem extends AbstractSystem {

	private Game game;

	public ExplodeAfterTimeSystem(Game _game, BasicECS ecs) {
		super(ecs, ExplodeAfterTimeComponent.class);
		
		game = _game;
	}
	
	
	public void processEntity(AbstractEntity entity) {
		ExplodeAfterTimeComponent ex = (ExplodeAfterTimeComponent)entity.getComponent(ExplodeAfterTimeComponent.class);
		if (ex.explode_time < System.currentTimeMillis()) {
			entity.remove();
			PhysicsComponent phys = (PhysicsComponent)entity.getComponent(PhysicsComponent.class);
			game.explosion(phys.getTranslation(), ex.explData);
		}
	}
	
}
