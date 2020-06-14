package com.scs.splitscreenfps.game;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractEvent;

public class EventCollision extends AbstractEvent {

	public AbstractEntity movingEntity; // todo - rename
	public AbstractEntity hitEntity; // todo - rename

	public EventCollision(AbstractEntity _movingEntity, AbstractEntity _hitEntity) {
		movingEntity = _movingEntity;
		hitEntity = _hitEntity;
	}

	@Override
	public boolean isForEntity(AbstractEntity e) {
		return movingEntity == e || hitEntity == e;
	}


	/*
	 * Helper function, since a collision takes two entities and you need to know which is which.
	 * Returns an array: [0] = entity with component of clazz1, [1] = entity with component of clazz2
	 */
	public AbstractEntity[] getEntitiesByComponent(Class<?> clazz1, Class<?> clazz2) {
		if (this.hitEntity == null) {
			return null;
		}

		AbstractEntity result[] = new AbstractEntity[2];
		
		if (this.movingEntity.getComponent(clazz1) != null) {
			result[0] = this.movingEntity;
			if (this.hitEntity.getComponent(clazz2) != null) {
				result[1] = this.hitEntity;
				return result;
			}
		} else if (this.movingEntity.getComponent(clazz2) != null) {
			result[1] = this.movingEntity;
			if (this.hitEntity.getComponent(clazz1) != null) {
				result[0] = this.hitEntity;
				return result;
			}
		}

		return null;
	}
}
