package com.scs.splitscreenfps.game.systems;

import java.util.List;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractEvent;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.HasDecal;
import com.scs.splitscreenfps.game.components.HasModelComponent;
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
		if (collectable.can_be_collected == false) {
			return;
		}
		
		List<AbstractEvent> colls = ecs.getEventsForEntity(EventCollision.class, entity);
		for (AbstractEvent evt : colls) {
			EventCollision coll = (EventCollision)evt;
			boolean collected = false;
			
			switch (collectable.type) {
			case HealthPack:
				collected = this.handleHealthPack(coll.entity2, coll.entity1);
				break;
			default:
				if (Settings.STRICT) {
					throw new RuntimeException("Unknown collectable: " + collectable.type);
				}
			}
			if (collected) {
				HasModelComponent model = (HasModelComponent)entity.getComponent(HasModelComponent.class);
				if (model != null) {
					model.invisible = true;
				}
				HasDecal decal = (HasDecal)entity.getComponent(HasDecal.class);
				if (decal != null) {
					decal.invisible = true;
				}
				collectable.can_be_collected = false;
				game.respawnHealthPackSystem.addEntity(entity);
			}
		}
	}


	/**
	 * Returns true if collected
	 * @param player
	 * @param coll
	 * @return
	 */
	private boolean handleHealthPack(AbstractEntity player, AbstractEntity coll) {
		PlayerData playerHitData = (PlayerData)player.getComponent(PlayerData.class);
		if (playerHitData != null) {
			if (playerHitData.health < playerHitData.max_health) {
				BillBoardFPS_Main.audio.play("sfx/Picked Coin Echo 2.wav");
				playerHitData.health += 75f;
				if (playerHitData.health > playerHitData.max_health) {
					playerHitData.health = playerHitData.max_health;
				}
				return  true;
			}
		}
		
		return false;
	}
	
}