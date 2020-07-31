package com.scs.splitscreenfps.game.systems;

import java.util.List;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractEvent;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.IsStickyComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.events.EventCollision;

public class StickySystem extends AbstractSystem {

	public StickySystem(BasicECS ecs) {
		super(ecs, IsStickyComponent.class);
	}


	public void processEntity(AbstractEntity entity) {
		List<AbstractEvent> colls = ecs.getEventsForEntity(EventCollision.class, entity);
		for (AbstractEvent evt : colls) {
			EventCollision coll = (EventCollision)evt;
			
			PhysicsComponent pc_wall = (PhysicsComponent)coll.entity2.getComponent(PhysicsComponent.class);
			if (pc_wall != null && pc_wall.body.isStaticOrKinematicObject()) {
				coll.entity1.removeComponent(PhysicsComponent.class);
				break;
			}
		}
			
	}

}