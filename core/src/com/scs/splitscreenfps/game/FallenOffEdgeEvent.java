package com.scs.splitscreenfps.game;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractEvent;

public class FallenOffEdgeEvent extends AbstractEvent { // todo - move/delete

	public AbstractEntity entity1;

	public FallenOffEdgeEvent(AbstractEntity e1) {
		entity1 = e1;
	}

	@Override
	public boolean isForEntity(AbstractEntity e) {
		return (entity1 == e);
	}


	/*
	 * Helper function, since a collision takes two entities and you need to know which is which.
	 * Returns an array: [0] = entity with component of clazz1, [1] = entity with component of clazz2
	 */
	/*
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
	}*/
}
