package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.Gdx;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.HasDecal;
import com.scs.splitscreenfps.game.components.HasDecalCycle;

public class CycleThruDecalsSystem extends AbstractSystem {

	public CycleThruDecalsSystem(BasicECS ecs) {
		super(ecs);
	}


	@Override
	public Class<?> getComponentClass() {
		return HasDecalCycle.class;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		HasDecalCycle hdc = (HasDecalCycle)entity.getComponent(HasDecalCycle.class);

		float dt = Gdx.graphics.getDeltaTime();

		hdc.animTimer += dt;
		if(hdc.animTimer > hdc.interval) {
			hdc.animTimer -= hdc.interval;
			hdc.decalIdx++;
			if (hdc.decalIdx >= hdc.decals.length) {
				if (hdc.remove_at_end_of_cycle) {
					entity.remove();
					return;
				} else {
					hdc.decalIdx = 0;
				}
			}
			HasDecal hd = (HasDecal)entity.getComponent(HasDecal.class);
			hd.decal = hdc.decals[hdc.decalIdx];
		}
	}


}
