package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.AvatarFactory;

public class TerrainTestLevel extends AbstractLevel {

	private float floor_size = 15f;

	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);


		floor_size = 10 + (game.players.length * 2f);
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

		// todo
	}

	
	@Override
	public void update() {
	}


}
