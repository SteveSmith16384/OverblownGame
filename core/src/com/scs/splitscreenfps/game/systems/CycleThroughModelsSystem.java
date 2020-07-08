package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.Gdx;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.HasModelCycle;

public class CycleThroughModelsSystem extends AbstractSystem {

	public CycleThroughModelsSystem(BasicECS ecs) {
		super(ecs, HasModelCycle.class);
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		HasModelCycle hdc = (HasModelCycle)entity.getComponent(HasModelCycle.class);
		HasModelComponent hd = (HasModelComponent)entity.getComponent(HasModelComponent.class);

		float dt = Gdx.graphics.getDeltaTime();

		hdc.animTimer += dt;
		if(hdc.animTimer > hdc.interval){
			hdc.animTimer -= hdc.interval;
			hdc.modelIdx++;
			if (hdc.modelIdx >= hdc.models.length) {
				hdc.modelIdx = 0;
			}
			hd.model = hdc.models[hdc.modelIdx];

		}
	}


}
