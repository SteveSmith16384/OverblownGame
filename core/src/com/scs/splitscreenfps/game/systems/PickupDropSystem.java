package com.scs.splitscreenfps.game.systems;

import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractEvent;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.EventCollision;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.CanBeCarried;
import com.scs.splitscreenfps.game.components.CanCarryComponent;
import com.scs.splitscreenfps.game.components.CollidesComponent;
import com.scs.splitscreenfps.game.components.HasDecal;
import com.scs.splitscreenfps.game.components.HasGuiSpriteComponent;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;

public class PickupDropSystem extends AbstractSystem {

	private Game game;

	public PickupDropSystem(BasicECS ecs, Game _game) {
		super(ecs, CanCarryComponent.class);

		game = _game;
	}


	@Override
	public void processEntity(AbstractEntity carrier) {
		CanCarryComponent cc = (CanCarryComponent)carrier.getComponent(CanCarryComponent.class);
		if (cc.lastPickupDropTime + 1000 > System.currentTimeMillis()) {
			return;
		}
		if (cc.carrying == null) {
			List<AbstractEvent> it = ecs.getEventsForEntity(EventCollision.class, carrier);
			for (AbstractEvent e : it) {
				EventCollision evt = (EventCollision)e;
				//AbstractEntity player = evt.movingEntity;
				AbstractEntity key = evt.hitEntity;
				if (key != null) {
					CanBeCarried cbc = (CanBeCarried)key.getComponent(CanBeCarried.class);
					if (cbc != null) {
						if (cc.wantsToCarry || cbc.auto_pickedup) {
							//cc.wantsToCarry = false;
							Settings.p(key + " picked up");
							cc.lastPickupDropTime = System.currentTimeMillis();
							key.hideComponent(CollidesComponent.class);
							key.hideComponent(HasDecal.class);
							key.hideComponent(HasModelComponent.class);
							key.restoreComponent(HasGuiSpriteComponent.class);

							PositionComponent keyPos = (PositionComponent)key.getComponent(PositionComponent.class);
							cbc.original_y = keyPos.position.y;

							HasGuiSpriteComponent hgsc = (HasGuiSpriteComponent)key.getComponent(HasGuiSpriteComponent.class);
							hgsc.onlyViewId = cc.viewId;
							cc.carrying = key;
							break;
						}
					}
				}
			}
		} else {
			if (cc.wantsToCarry) {
				// Drop!
				CanBeCarried cbc = (CanBeCarried)cc.carrying.getComponent(CanBeCarried.class);
				if (cbc != null) {
					cc.lastPickupDropTime = System.currentTimeMillis();
					Settings.p(cc.carrying + " dropped");
					cc.carrying.restoreComponent(CollidesComponent.class);
					cc.carrying.restoreComponent(HasDecal.class);
					cc.carrying.restoreComponent(HasModelComponent.class);
					cc.carrying.hideComponent(HasGuiSpriteComponent.class);

					// Set position
					PositionComponent carrierPos = (PositionComponent)carrier.getComponent(PositionComponent.class);
					PositionComponent pos = (PositionComponent)cc.carrying.getComponent(PositionComponent.class);

					pos.originalPosition.set(carrierPos.position);
					if (cc.viewId >= 0) {
						// Drop in front of player
						Camera cam = game.viewports[cc.viewId].camera;
						Vector3 dir = new Vector3(cam.direction);
						dir.y = 0;
						dir.nor().scl(.2f);
						pos.originalPosition.add(dir);
					}

					pos.originalPosition.y = cbc.original_y;
					pos.position.set(pos.originalPosition);
					pos.position.y = cbc.original_y;

					cc.carrying = null;
				}
			}
		}
	}

}
