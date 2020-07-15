package com.scs.splitscreenfps.game.systems;

import java.util.List;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractEvent;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.HarmOnContactComponent;
import com.scs.splitscreenfps.game.components.IsCollectableComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.events.EventCollision;

public class CollectableSystem extends AbstractSystem {

	private Game game;

	public CollectableSystem(Game _game, BasicECS ecs) {
		super(ecs, IsCollectableComponent.class);

		game = _game;
	}


	public void processEntity(AbstractEntity entity) {
		List<AbstractEvent> colls = ecs.getEventsForEntity(EventCollision.class, entity);
		for (AbstractEvent evt : colls) {
			EventCollision coll = (EventCollision)evt;

			PlayerData playerHitData = (PlayerData)coll.entity2.getComponent(PlayerData.class);
			if (playerHitData != null) {
				//BillBoardFPS_Main.audio.play("todo");
				playerHitData.health += 10f;
				entity.remove();
				break;
			}
		}
	}

}