package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.Gdx;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.AvatarFactory;
import com.scs.splitscreenfps.game.gamemodes.PiggyGameMode;

public class PiggyLevel extends AbstractLevel {

	private ISystem piggyGameMode;

	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);

		this.piggyGameMode = new PiggyGameMode(game, game.ecs);//, 2*60*1000, 6);

	}


	@Override
	public int[] getHeroSelection() {
		return new int[]{AvatarFactory.CHAR_PIGGY, AvatarFactory.CHAR_VICTIM};
	}


	@Override
	public void setBackgroundColour() {
		Gdx.gl.glClearColor(0, .6f, .8f, 1);
	}


	@Override
	public void load() {
		try {
			//super.loadJsonFile("maps/undergroundcomplex.json", false);
			super.loadJsonFile("maps/map_editor.json", false);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Override
	public void update() {
		piggyGameMode.process();
	}


}
