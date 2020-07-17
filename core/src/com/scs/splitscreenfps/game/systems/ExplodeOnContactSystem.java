package com.scs.splitscreenfps.game.systems;

import java.util.List;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractEvent;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.ExplodeOnContactComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.events.EventCollision;

public class ExplodeOnContactSystem extends AbstractSystem {

	private Game game;
	
	public ExplodeOnContactSystem(Game _game, BasicECS ecs) {
		super(ecs, ExplodeOnContactComponent.class);
		
		game = _game;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		ExplodeOnContactComponent bullet = (ExplodeOnContactComponent)entity.getComponent(ExplodeOnContactComponent.class);

		List<AbstractEvent> colls = ecs.getEventsForEntity(EventCollision.class, entity);
		for (AbstractEvent evt : colls) {
			EventCollision coll = (EventCollision)evt;
			if (coll.entity2 == bullet.shooter) { // Shooter can't shoot themselves
				continue;
			}

			if (bullet.remove) {
				entity.remove();
			}
			//Settings.p("Rocket hit " + hit);
			PositionComponent posData = (PositionComponent)entity.getComponent(PositionComponent.class);
			game.explosion(posData.position, bullet.explData, bullet.shooter);

		}

	}

}
