package com.scs.splitscreenfps.game.systems;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.AddComponentAfterTimeComponent;

public class AddComponentAfterTimeSystem extends AbstractSystem {

	public AddComponentAfterTimeSystem(BasicECS ecs) {
		super(ecs, AddComponentAfterTimeComponent.class);
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		AddComponentAfterTimeComponent rc = (AddComponentAfterTimeComponent)entity.getComponent(AddComponentAfterTimeComponent.class);
		if (System.currentTimeMillis() > rc.time_to_add) {
			entity.addComponent(rc.component);
		}
	}
	
}
