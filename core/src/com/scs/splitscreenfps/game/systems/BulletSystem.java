package com.scs.splitscreenfps.game.systems;

import java.util.List;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractEvent;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.EventCollision;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.IsBulletComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.components.RemoveEntityAfterTimeComponent;
import com.scs.splitscreenfps.game.entities.GraphicsEntityFactory;

/**
 * Handles bullets, grenades and rockets
 *
 */
public class BulletSystem extends AbstractSystem {

	private Game game;

	public BulletSystem(BasicECS ecs, Game _game) {
		super(ecs, IsBulletComponent.class);

		game = _game;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		PositionComponent pos = (PositionComponent)entity.getComponent(PositionComponent.class);

		List<AbstractEvent> colls = ecs.getEventsForEntity(EventCollision.class, entity);
		for (AbstractEvent evt : colls) {
			EventCollision coll = (EventCollision)evt;

			AbstractEntity[] ents = coll.getEntitiesByComponent(IsBulletComponent.class, PlayerData.class);
			if (ents != null) {
				IsBulletComponent bullet = (IsBulletComponent)entity.getComponent(IsBulletComponent.class);
				// PlayerData shooterData = (PlayerData)bullet.shooter.getComponent(PlayerData.class);
				PlayerData playerHitData = (PlayerData)ents[1].getComponent(PlayerData.class);
				// Check if target is alive
				if (playerHitData.health > 0) {
					if (playerHitData.side != bullet.side) {
						//ents[0].remove(); // Remove bullet
						playerHitData.health -= 50; // Todo - bullet power						

						for (int id = 0 ; id<game.players.length ; id++) {
							if (ents[1] == game.players[id]) {
								if (playerHitData.health <= 0) {
									AbstractEntity whitefilter = GraphicsEntityFactory.createWhiteFilter(game.ecs, id);
									ecs.addEntity(whitefilter);
								} else {
									AbstractEntity redfilter = GraphicsEntityFactory.createRedFilter(game.ecs, id);
									redfilter.addComponent(new RemoveEntityAfterTimeComponent(1));
									ecs.addEntity(redfilter);
								}
								break;
							}
						}

						AbstractEntity expl = GraphicsEntityFactory.createNormalExplosion(ecs, pos.position, 1);
						ecs.addEntity(expl);

						return;
					}
				}
			}
		}

	}

}
