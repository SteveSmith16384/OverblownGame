package com.scs.splitscreenfps.game.systems;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractEvent;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.EventCollision;
import com.scs.splitscreenfps.game.components.DoorComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;

public class DoorSystem extends AbstractSystem {

	public DoorSystem(BasicECS ecs) {
		super(ecs, DoorComponent.class);
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		PositionComponent posData = (PositionComponent)entity.getComponent(PositionComponent.class);
		DoorComponent dc = (DoorComponent)entity.getComponent(DoorComponent.class);
		if (dc.is_opening) {
			if (posData.position.y < dc.max_height) {
				posData.position.y += .6f * Gdx.graphics.getDeltaTime();
				//CollidesComponent cc = (CollidesComponent)entity.getComponent(CollidesComponent.class);
				//cc.bb_dirty = true;
			} else {
				posData.position.y = dc.max_height;
				dc.is_opening = false;
				dc.time_to_close = System.currentTimeMillis() + 4000;
			}
		} else {
			// Time to close
			if (posData.position.y > 0) {
				if (System.currentTimeMillis() > dc.time_to_close) {
					posData.position.y -= .4f * Gdx.graphics.getDeltaTime();
					if (posData.position.y < 0) {
						posData.position.y = 0;
					}
					//CollidesComponent cc = (CollidesComponent)entity.getComponent(CollidesComponent.class);
					//cc.bb_dirty = true;
				}
			}
			List<AbstractEvent> it = ecs.getEventsForEntity(EventCollision.class, entity);
			if (it.size() > 0) {
				dc.is_opening = true;
			}
		}
	}

}