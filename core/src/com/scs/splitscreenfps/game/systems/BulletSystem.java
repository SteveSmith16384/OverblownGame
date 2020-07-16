package com.scs.splitscreenfps.game.systems;

import java.util.List;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractEvent;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.IsBulletComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.entities.GraphicsEntityFactory;
import com.scs.splitscreenfps.game.events.EventCollision;

/**
 * Handles bullets, grenades and rockets
 *
 */
public class BulletSystem extends AbstractSystem { // todo - split this up into other systems

	private Game game;

	public BulletSystem(BasicECS ecs, Game _game) {
		super(ecs, IsBulletComponent.class);

		game = _game;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		IsBulletComponent bullet = (IsBulletComponent)entity.getComponent(IsBulletComponent.class);
		PositionComponent bulletPos = (PositionComponent)entity.getComponent(PositionComponent.class);
		
		List<AbstractEvent> colls = ecs.getEventsForEntity(EventCollision.class, entity);
		for (AbstractEvent evt : colls) {
			EventCollision coll = (EventCollision)evt;
			if (coll.entity2 == bullet.shooter) { // Shooter can't shoot themselves
				continue;
			}
			if (bullet.remove_on_contact) {
				entity.remove(); // Don't remove grenades on contact!
			}

			PlayerData playerHitData = (PlayerData)coll.entity2.getComponent(PlayerData.class);
			if (playerHitData != null) {
				entity.remove();
				if (playerHitData.health > 0) {
					game.playerDamaged(coll.entity2, playerHitData, bullet.settings.damage, bullet.shooter);

					AbstractEntity expl = GraphicsEntityFactory.createNormalExplosion(game, bulletPos.position, 1);
					ecs.addEntity(expl);

					return;
				}
			}
		}

	}

}
