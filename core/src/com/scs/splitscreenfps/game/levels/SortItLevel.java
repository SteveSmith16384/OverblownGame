package com.scs.splitscreenfps.game.levels;

import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.AvatarFactory;
import com.scs.splitscreenfps.game.gamemodes.ScoreAndTimeLimitSystem;
import com.scs.splitscreenfps.game.systems.CollectPackageSystem;
import com.scs.splitscreenfps.game.systems.DispensePackageSystem;
import com.scs.splitscreenfps.game.systems.ShootingSystem;

public class SortItLevel extends AbstractLevel {

	private DispensePackageSystem dispenserSystem;
	private CollectPackageSystem collectorSystem;
	private ScoreAndTimeLimitSystem scoreSystem;

	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);

		game.ecs.removeSystem(ShootingSystem.class);
		
		game.show_health = false;

		this.scoreSystem = new ScoreAndTimeLimitSystem(20, 60*1000);
		dispenserSystem = new DispensePackageSystem(game);
		this.collectorSystem = new CollectPackageSystem(game, game.ecs, scoreSystem);
	}


	@Override
	public int[] getHeroSelection() {
		return new int[]{AvatarFactory.CHAR_SORTIT};
	}


	@Override
	public void load() {
		try {
			super.loadJsonFile("maps/sortit.json", false);

			/*
			AbstractEntity pickup = EquipmentEntityFactory.createPickup(game, 3, 3, 3);
			game.ecs.addEntity(pickup);

			pickup = EquipmentEntityFactory.createPickup(game, 2, 3, 5);
			game.ecs.addEntity(pickup);

			pickup = EquipmentEntityFactory.createPickup(game, 3, 3, 7);
			game.ecs.addEntity(pickup);
*/
		} catch (Exception e) {
			throw new RuntimeException("Error loading map file", e);
		}
	}


	@Override
	public void update() {
		this.dispenserSystem.process();
		this.collectorSystem.process();
	}


	@Override
	public String getName() {
		return "Sort It!";
	}


}
