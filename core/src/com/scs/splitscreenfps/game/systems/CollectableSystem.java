package com.scs.splitscreenfps.game.systems;

import java.util.List;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractEvent;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.HarmOnContactComponent;
import com.scs.splitscreenfps.game.components.IsCollectableComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.events.EventCollision;

public class CollectableSystem extends AbstractSystem {

	public enum CollectableType {HealthPack};
	private Game game;

	public CollectableSystem(Game _game, BasicECS ecs) {
		super(ecs, IsCollectableComponent.class);

		game = _game;
	}


	public void processEntity(AbstractEntity entity) {
		IsCollectableComponent collectable = (IsCollectableComponent)entity.getComponent(IsCollectableComponent.class);

		List<AbstractEvent> colls = ecs.getEventsForEntity(EventCollision.class, entity);
		for (AbstractEvent evt : colls) {
			EventCollision coll = (EventCollision)evt;

			switch (collectable.type) {
			case HealthPack:
				this.handleHealthPack(coll.entity2);
				break;
			default:
				if (Settings.STRICT) {
					throw new RuntimeException("todo");
				}
			}

			if (collectable.disappearsWhenCollected) {
				entity.remove();
			}
		}
	}


	private void handleHealthPack(AbstractEntity player) {
		PlayerData playerHitData = (PlayerData)player.getComponent(PlayerData.class);
		if (playerHitData != null) {
			//BillBoardFPS_Main.audio.play("todo");
			playerHitData.health += 10f;
		}
	}
}