package com.scs.splitscreenfps.game.systems;

import java.util.List;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractEvent;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.ExplodeOnContactComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.events.EventCollision;

public class ExplodeOnContactSystem extends AbstractSystem {

	private Game game;

	public ExplodeOnContactSystem(Game _game, BasicECS ecs) {
		super(ecs, ExplodeOnContactComponent.class);

		game = _game;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		ExplodeOnContactComponent bullet = (ExplodeOnContactComponent)entity.getComponent(ExplodeOnContactComponent.class);

		List<AbstractEvent> colls = ecs.getEventsForEntity(EventCollision.class, entity);
		for (AbstractEvent evt : colls) {
			EventCollision coll = (EventCollision)evt;
			if (coll.entity2 == bullet.shooter) { // Shooter can't shoot themselves
				continue;
			}
			PhysicsComponent hitPhysics = (PhysicsComponent)coll.entity2.getComponent(PhysicsComponent.class);
			if (hitPhysics.isRigidBody() == false) {
				continue;
			}

			boolean is_player = coll.entity2 instanceof AbstractPlayersAvatar;

			if (bullet.only_if_player == false || is_player) {
				if (bullet.remove) {
					entity.remove();
				}
				Settings.p(bullet + " hit " + coll.entity2);
				PositionComponent posData = (PositionComponent)entity.getComponent(PositionComponent.class);
				
				//AbstractEntity shooter = bullet.shooter;
				//game.explosion(posData.position, bullet.explData, bullet.harm_shooter ? null : bullet.shooter);
				game.explosion(posData.position, bullet.explData, bullet.shooter, bullet.harm_shooter);
			}

		}

	}

}
