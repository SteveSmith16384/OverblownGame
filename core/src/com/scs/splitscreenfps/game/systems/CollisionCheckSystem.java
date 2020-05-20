package com.scs.splitscreenfps.game.systems;

import java.util.Iterator;

import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.EventCollision;
import com.scs.splitscreenfps.game.components.CollidesComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;

public class CollisionCheckSystem extends AbstractSystem {

	private Vector3 tmp = new Vector3();
	
	public CollisionCheckSystem(BasicECS ecs) {
		super(ecs);
	}


	@Override
	public Class<?> getComponentClass() {
		return CollidesComponent.class;
	}


	/**
	 * Returns true/false depending if movement blocked
	 */
	public boolean collided(AbstractEntity mover, PositionComponent ourPos, boolean raise_event) {
		boolean blocked = false;
		CollidesComponent moverCC = (CollidesComponent)mover.getComponent(CollidesComponent.class);
		if (moverCC == null) {
			throw new RuntimeException("Mover has no CollidesComponent");
		}
		Iterator<AbstractEntity> it = entities.iterator();
		while (it.hasNext()) {
			AbstractEntity e = it.next();
			if (e != mover) {
				CollidesComponent cc = (CollidesComponent)e.getComponent(CollidesComponent.class);
				if (cc != null) {
					if (cc.dont_collide_with == mover || moverCC.dont_collide_with == e) {
						continue;
					}
					
					PositionComponent theirPos = (PositionComponent)e.getComponent(PositionComponent.class);
					if (Settings.STRICT) {
						if (theirPos == null) {
							throw new RuntimeException("Entity " + e + " does not have a " + PositionComponent.class.getSimpleName());
						}
					}
					float len = tmp.set(theirPos.position).sub(ourPos.position).len();
					
					if (Settings.STRICT) {
						if (moverCC.rad <= 0) {
							throw new RuntimeException(mover + " has no radius");
						}
						if (cc.rad <= 0) {
							throw new RuntimeException(e + " has no radius");
						}
					}
					
					if (len < moverCC.rad + cc.rad) {
						//Settings.p(mover + " collided with " + e);
						blocked = cc.blocksMovement || blocked;
						if (raise_event) {
							ecs.events.add(new EventCollision(mover, e));
						}
					}
				}
			}
		}
		return blocked;
	}

}
