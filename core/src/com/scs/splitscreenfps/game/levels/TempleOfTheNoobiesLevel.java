package com.scs.splitscreenfps.game.levels;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.EntityFactory;
import com.scs.splitscreenfps.game.gamemodes.DeathmatchSystem;
import com.scs.splitscreenfps.game.systems.CollectableSystem;

public class TempleOfTheNoobiesLevel extends AbstractLevel {

	private ISystem deathmatchSystem;
	
	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);

		this.deathmatchSystem = new DeathmatchSystem(game, game.ecs, true);
	}


	@Override
	public void load() {
		try {
			super.loadJsonFile("maps/templeofthenoobies.json", false);
		} catch (Exception e) {
			throw new RuntimeException("Error loading map file", e);
		}

		//AbstractEntity lootbox = EntityFactory.createLootBox(game,10, 10, 1, CollectableSystem.CollectableType.HealthPack);
		//game.ecs.addEntity(lootbox);

	}


	@Override
	public void update() {
		deathmatchSystem.process();
	}


	@Override
	public String getName() {
		return "Temple of the Noobies";
	}


}
