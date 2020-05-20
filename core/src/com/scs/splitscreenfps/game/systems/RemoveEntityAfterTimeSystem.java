package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.Gdx;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.components.RemoveEntityAfterTimeComponent;

public class RemoveEntityAfterTimeSystem extends AbstractSystem {

	public RemoveEntityAfterTimeSystem(BasicECS ecs) {
		super(ecs, RemoveEntityAfterTimeComponent.class);
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		RemoveEntityAfterTimeComponent hdc = (RemoveEntityAfterTimeComponent)entity.getComponent(RemoveEntityAfterTimeComponent.class);

		float dt = Gdx.graphics.getDeltaTime();

		hdc.timeRemaining_secs -= dt;
		if(hdc.timeRemaining_secs <= 0) {
			Settings.p("Removed " + entity);
			entity.remove();
		}
	}

}
