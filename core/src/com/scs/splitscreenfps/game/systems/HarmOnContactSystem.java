package com.scs.splitscreenfps.game.systems;

import java.util.List;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractEvent;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.game.EventCollision;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.HarmOnContactComponent;
import com.scs.splitscreenfps.game.components.PlayerData;

public class HarmOnContactSystem extends AbstractSystem {

	private Game game;

	public HarmOnContactSystem(Game _game, BasicECS ecs) {
		super(ecs, HarmOnContactComponent.class);

		game = _game;
	}


	public void processEntity(AbstractEntity entity) {
		List<AbstractEvent> colls = ecs.getEventsForEntity(EventCollision.class, entity);
		HarmOnContactComponent harm = null;
		if (colls.size() > 0) {
			harm = (HarmOnContactComponent)entity.getComponent(HarmOnContactComponent.class);
		}
		for (AbstractEvent evt : colls) {
			EventCollision coll = (EventCollision)evt;

			PlayerData playerHitData = (PlayerData)coll.entity2.getComponent(PlayerData.class);
			if (playerHitData != null) {
				//entity.remove();
				if (playerHitData.health > 0) {
					game.playerDamaged(coll.entity2, playerHitData, harm.damage, null);
					BillBoardFPS_Main.audio.play(harm.sfx);
					
				}
			}
		}
	}

}
