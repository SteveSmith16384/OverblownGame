package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.EntityFactory;
import com.scs.splitscreenfps.game.entities.Wall;
import com.scs.splitscreenfps.game.gamemodes.DeathmatchSystem;

import ssmith.lang.NumberFunctions;

public class TowerBlocksLevel extends AbstractLevel {

	private float floor_size = 35f;

	private ISystem deathmatchSystem;

	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);		
		if (Settings.TEST_VOX == false) {
			this.deathmatchSystem = new DeathmatchSystem(game, game.ecs, true);
		}
	}


	@Override
	public void load() {
		try {
			Wall floor = new Wall(game, "Floor", game.getTexture("textures/tones/brown1.png"), floor_size/2, -0.1f, floor_size/2, 
					floor_size, .2f, floor_size, 
					0f, true, false);
			game.ecs.addEntity(floor);

			if (Settings.TEST_VOX) {
				this.startPositions.add(new Vector3(0, 10f, 0));
				this.startPositions.add(new Vector3(0, 10f, 0));

				//loadVox("vox/voxelbuildings/1.vox", 1, new Vector3(5, .1f, 5), .2f);
				//loadVox("vox/skyscraper1.vox", 1, new Vector3(5, .25f, 5), .5f);
				
				AbstractEntity model = EntityFactory.createStaticModel(game.ecs, "Castle", "vox/graveyard.obj", 5, 0, 5, 0, true);
				game.ecs.addEntity(model);
			} else {
				this.startPositions.add(new Vector3(1, 2f, 1));
				this.startPositions.add(new Vector3(floor_size-2, 2f, floor_size-2));
				this.startPositions.add(new Vector3(1, 2f, floor_size-2));
				this.startPositions.add(new Vector3(floor_size-2, 2f, 1));

				for (int z=0 ; z<2 ; z++) {
					for (int x=0 ; x<2 ; x++) {
						//super.loadJsonFile("maps/map_editor.json", false, new Vector3(x*8+4, 0, z*8+4), 2);
						super.loadJsonFile("maps/skyscraper" + NumberFunctions.rnd(1, 2) + ".json", false, new Vector3(x*10+7, 0.05f, z*10+7), 1);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error loading map file", e);
		}
	}


	@Override
	public void update() {
		if (Settings.TEST_VOX == false) {
			deathmatchSystem.process();
		}
	}


	@Override
	public String getName() {
		return "Tower Block Deathmatch";
	}


}
