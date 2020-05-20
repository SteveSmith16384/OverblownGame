package com.scs.splitscreenfps.game.systems.ql;

import java.util.List;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractEvent;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.game.EventCollision;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.components.RemoveEntityAfterTimeComponent;
import com.scs.splitscreenfps.game.components.ql.IsBulletComponent;
import com.scs.splitscreenfps.game.components.ql.QLPlayerData;
import com.scs.splitscreenfps.game.components.ql.RemoveAtEndOfPhase;
import com.scs.splitscreenfps.game.entities.EntityFactory;
import com.scs.splitscreenfps.game.levels.QuantumLeagueLevel;

public class QLBulletSystem extends AbstractSystem {

	private Game game;

	public QLBulletSystem(BasicECS ecs, Game _game) {
		super(ecs, IsBulletComponent.class);

		game = _game;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		PositionComponent pos = (PositionComponent)entity.getComponent(PositionComponent.class);

		List<AbstractEvent> colls = ecs.getEventsForEntity(EventCollision.class, entity);
		for (AbstractEvent evt : colls) {
			EventCollision coll = (EventCollision)evt;

			if (coll.hitEntity == null) { // Hit wall
				//Settings.p("Bullet removed after hitting wall");
				entity.remove();
				AbstractEntity expl = EntityFactory.createBlueExplosion(ecs, pos.position);
				ecs.addEntity(expl);
				BillBoardFPS_Main.audio.play("sfx/explosion_dull.wav");
				continue;
			}

			AbstractEntity[] ents = coll.getEntitiesByComponent(IsBulletComponent.class, QLPlayerData.class);
			if (ents != null) {
				IsBulletComponent bullet = (IsBulletComponent)entity.getComponent(IsBulletComponent.class);
				// Check if shooter is alive
				QLPlayerData shooterData = (QLPlayerData)bullet.shooter.getComponent(QLPlayerData.class);
				if (shooterData.health > 0) {
					QLPlayerData playerHitData = (QLPlayerData)ents[1].getComponent(QLPlayerData.class);
					// Check if target is alive
					if (playerHitData.health > 0) {
						if (playerHitData.side != bullet.side) {
							ents[0].remove(); // Remove bullet
							playerHitData.health -= 50;

							for (int id = 0 ; id<game.players.length ; id++) {
								if (ents[1] == game.players[id]) {
									if (playerHitData.health <= 0) {
										AbstractEntity whitefilter = EntityFactory.createWhiteFilter(game.ecs, id);
										whitefilter.addComponent(new RemoveAtEndOfPhase());
										ecs.addEntity(whitefilter);
									} else {
										AbstractEntity redfilter = EntityFactory.createRedFilter(game.ecs, id);
										redfilter.addComponent(new RemoveAtEndOfPhase());
										redfilter.addComponent(new RemoveEntityAfterTimeComponent(1));
										ecs.addEntity(redfilter);
									}
									break;
								}
							}

							if (playerHitData.health <= 0) {
								QuantumLeagueLevel.setAvatarColour(ents[1], false);

								BillBoardFPS_Main.audio.play("sfx/qubodup-PowerDrain.ogg");
							}

							AbstractEntity expl = EntityFactory.createNormalExplosion(ecs, pos.position);
							ecs.addEntity(expl);

							return;
						}
					}
				}
			}
		}
	}

}
