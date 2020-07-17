package com.scs.splitscreenfps.game.systems;

import java.util.List;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractEvent;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.RemoveOnContactComponent;
import com.scs.splitscreenfps.game.events.EventCollision;

public class RemoveOnContactSystem extends AbstractSystem {

	public RemoveOnContactSystem(BasicECS ecs) {
		super(ecs, RemoveOnContactComponent.class);
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		RemoveOnContactComponent bullet = (RemoveOnContactComponent)entity.getComponent(RemoveOnContactComponent.class);

		List<AbstractEvent> colls = ecs.getEventsForEntity(EventCollision.class, entity);
		for (AbstractEvent evt : colls) {
			EventCollision coll = (EventCollision)evt;
			if (coll.entity2 == bullet.ignore) { // Shooter can't shoot themselves
				continue;
			}
			entity.remove();

		}

	}

}
