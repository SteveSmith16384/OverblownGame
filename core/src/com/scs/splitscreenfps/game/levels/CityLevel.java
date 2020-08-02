package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.AvatarFactory;
import com.scs.splitscreenfps.game.entities.Wall;
import com.scs.splitscreenfps.game.gamemodes.DeathmatchSystem;

public class CityLevel extends AbstractLevel {

	private float floor_size = 18f;

	private ISystem deathmatchSystem;

	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);		
		this.deathmatchSystem = new DeathmatchSystem(game, game.ecs, true);
	}


	@Override
	public int[] getHeroSelection() {
		return new int[]{AvatarFactory.CHAR_PHARTAH, AvatarFactory.CHAR_BOOMFIST, AvatarFactory.CHAR_BOWLING_BALL, AvatarFactory.CHAR_RACER, AvatarFactory.CHAR_RUBBISHRODENT};
	}


	@Override
	public void setBackgroundColour() {
		Gdx.gl.glClearColor(0, .6f, .8f, 1);
	}


	@Override
	public void load() {
		this.startPositions.add(new Vector3(1, 2f, 1));
		this.startPositions.add(new Vector3(floor_size-2, 2f, floor_size-2));
		this.startPositions.add(new Vector3(1, 2f, floor_size-2));
		this.startPositions.add(new Vector3(floor_size-2, 2f, 1));

/*
 		Wall floor = new Wall(game, "Floor", "colours/white.png", floor_size/2, -0.1f, floor_size/2, 
				floor_size, .2f, floor_size, 
				0f, true, false);
		game.ecs.addEntity(floor);

		try {
			//super.loadJsonFile("maps/skyscraper.json", false);
			//super.loadJsonFile("maps/map_editor.json", false, new Vector3(4, 0, 4));
		} catch (Exception e) {
			e.printStackTrace();
		}
*/
		for (int z=0 ; z<2 ; z++) {
			for (int x=0 ; x<2 ; x++) {
				try {
					//super.loadJsonFile("maps/skyscraper.json", false);
					super.loadJsonFile("maps/map_editor.json", false, new Vector3(x*8+4, 0, z*8+4), 2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}


	@Override
	public void update() {
		deathmatchSystem.process();
	}


}
