package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.EquipmentEntityFactory;
import com.scs.splitscreenfps.game.entities.Wall;
import com.scs.splitscreenfps.game.systems.ShootingSystem;

public class StarshipLevel extends AbstractLevel {

	private float floor_size = 35f;

	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);
		
		game.ecs.removeSystem(ShootingSystem.class);
	}


	@Override
	public void load() {
		this.startPositions.add(new Vector3(1, 2f, 1));
		this.startPositions.add(new Vector3(floor_size-2, 2f, floor_size-2));
		this.startPositions.add(new Vector3(1, 2f, floor_size-2));
		this.startPositions.add(new Vector3(floor_size-2, 2f, 1));

		try {
			super.loadJsonFile("maps/starship.json", false);

			/*Wall floor = new Wall(game, "Floor", game.getTexture("textures/tones/brown1.png"), floor_size/2, -0.1f, floor_size/2, 
					floor_size, .2f, floor_size, 
					0f, true, false);
			game.ecs.addEntity(floor);*/
			
			AbstractEntity pickup = EquipmentEntityFactory.createPickup(game, 3, 3, 3);
			game.ecs.addEntity(pickup);

		} catch (Exception e) {
			throw new RuntimeException("Error loading map file", e);
		}
	}


	@Override
	public void update() {
	}


	@Override
	public String getName() {
		return "Starship Pulsar";
	}


}
