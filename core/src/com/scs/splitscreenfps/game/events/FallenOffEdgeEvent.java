package com.scs.splitscreenfps.game.events;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractEvent;

public class FallenOffEdgeEvent extends AbstractEvent {

	public AbstractEntity entity1;

	public FallenOffEdgeEvent(AbstractEntity e1) {
		entity1 = e1;
	}

	@Override
	public boolean isForEntity(AbstractEntity e) {
		return (entity1 == e);
	}

}
