package com.scs.splitscreenfps.game;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractEvent;

public class EventCollision extends AbstractEvent {

	public AbstractEntity entity1;
	public AbstractEntity entity2;
	public float force;
	
	public EventCollision(AbstractEntity e1, AbstractEntity e2, float _force) {
		entity1 = e1;
		entity2 = e2;
		force = _force;
	}
	

	@Override
	public boolean isForEntity(AbstractEntity e) {
		if (entity1 == e || entity2 == e) {
			// Make the entity we search for the moving one
			if (e == entity2) {
				AbstractEntity tmp = entity1;
				entity1 = entity2;
				entity2 = tmp;
			}
			return true;
		}
		return false;
	}


	/*
	 * Helper function, since a collision takes two entities and you need to know which is which.
	 * Returns an array: [0] = entity with component of clazz1, [1] = entity with component of clazz2
	 */
	public AbstractEntity[] getEntitiesByComponent(Class<?> clazz1, Class<?> clazz2) {
		if (this.entity2 == null) {
			return null;
		}

		AbstractEntity result[] = new AbstractEntity[2];

		if (this.entity1.getComponent(clazz1) != null) {
			result[0] = this.entity1;
			if (this.entity2.getComponent(clazz2) != null) {
				result[1] = this.entity2;
				return result;
			}
		} else if (this.entity1.getComponent(clazz2) != null) {
			result[1] = this.entity1;
			if (this.entity2.getComponent(clazz1) != null) {
				result[0] = this.entity2;
				return result;
			}
		}

		return null;
	}
}
