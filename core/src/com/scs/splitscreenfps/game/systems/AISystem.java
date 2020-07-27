package com.scs.splitscreenfps.game.systems;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.HasAIComponent;

public class AISystem extends AbstractSystem {

	public AISystem(BasicECS ecs) {
		super(ecs,  HasAIComponent.class);
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		// todo
	}

}
