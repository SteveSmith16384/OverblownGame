package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.EquipmentEntityFactory;
import com.scs.splitscreenfps.game.systems.DispensePackageSystem;
import com.scs.splitscreenfps.game.systems.ShootingSystem;

public class SortItLevel extends AbstractLevel {

	private float floor_size = 35f;

	private DispensePackageSystem dispenserSystem;

	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);

		game.ecs.removeSystem(ShootingSystem.class);

		dispenserSystem = new DispensePackageSystem(game);
	}


	@Override
	public void load() {
		this.startPositions.add(new Vector3(1, 2f, 1));
		this.startPositions.add(new Vector3(floor_size-2, 2f, floor_size-2));
		this.startPositions.add(new Vector3(1, 2f, floor_size-2));
		this.startPositions.add(new Vector3(floor_size-2, 2f, 1));

		try {
			super.loadJsonFile("maps/sortit.json", false);

			/*Wall floor = new Wall(game, "Floor", game.getTexture("textures/tones/brown1.png"), floor_size/2, -0.1f, floor_size/2, 
					floor_size, .2f, floor_size, 
					0f, true, false);
			game.ecs.addEntity(floor);*/

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
	}


	@Override
	public String getName() {
		return "Sort It!";
	}


}
