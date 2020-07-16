package com.scs.splitscreenfps.game.systems;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.components.HasRangeComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;

public class CheckRangeSystem extends AbstractSystem {

	public CheckRangeSystem(BasicECS ecs) {
		super(ecs, HasRangeComponent.class);
	}


	public void processEntity(AbstractEntity entity) {
		HasRangeComponent rangeComp = (HasRangeComponent)entity.getComponent(HasRangeComponent.class);
		PositionComponent bulletPos = (PositionComponent)entity.getComponent(PositionComponent.class);
		
		// Check range
		float dist = rangeComp.start.dst(bulletPos.position);
		if (dist > rangeComp.range) {
			Settings.p(entity + " reached range");
			entity.remove();
		}

	}

}