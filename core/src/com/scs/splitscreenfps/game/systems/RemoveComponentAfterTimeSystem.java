package com.scs.splitscreenfps.game.systems;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.RemoveComponentAfterTimeComponent;

public class RemoveComponentAfterTimeSystem extends AbstractSystem {

	public RemoveComponentAfterTimeSystem(BasicECS ecs) {
		super(ecs, RemoveComponentAfterTimeComponent.class);
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		RemoveComponentAfterTimeComponent rc = (RemoveComponentAfterTimeComponent)entity.getComponent(RemoveComponentAfterTimeComponent.class);
		if (System.currentTimeMillis() > rc.expires) {
			entity.removeComponent(rc.clazz);
		}
	}
	
}
