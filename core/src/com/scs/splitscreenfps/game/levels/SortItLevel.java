package com.scs.splitscreenfps.game.levels;

import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.AvatarFactory;
import com.scs.splitscreenfps.game.gamemodes.ScoreAndTimeLimitSystem;
import com.scs.splitscreenfps.game.systems.CollectPackageSystem;
import com.scs.splitscreenfps.game.systems.DispensePackageSystem;
import com.scs.splitscreenfps.game.systems.PackagesFallenOffEdgeSystem;
import com.scs.splitscreenfps.game.systems.ShootingSystem;

public class SortItLevel extends AbstractLevel {

	private DispensePackageSystem dispenserSystem;
	private CollectPackageSystem collectorSystem;
	private ScoreAndTimeLimitSystem scoreSystem;
	private PackagesFallenOffEdgeSystem fallenOffEdgeSystem;
	
	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);

		game.ecs.removeSystem(ShootingSystem.class);
		
		game.show_health = false;

		this.scoreSystem = new ScoreAndTimeLimitSystem(game, 15+(game.getLevelNum()*5), 2*60*1000);
		dispenserSystem = new DispensePackageSystem(game);
		this.collectorSystem = new CollectPackageSystem(game, game.ecs, scoreSystem);
		this.fallenOffEdgeSystem = new PackagesFallenOffEdgeSystem(game.ecs, this.scoreSystem);
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
		if (game.game_stage != 0) {
			return;
		}
		this.dispenserSystem.process();
		this.collectorSystem.process();
		fallenOffEdgeSystem.process();
		scoreSystem.process();
	}


	@Override
	public String getName() {
		return "Sort It!";
	}


}
