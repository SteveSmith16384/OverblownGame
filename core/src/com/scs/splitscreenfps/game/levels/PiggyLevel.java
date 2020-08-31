package com.scs.splitscreenfps.game.levels;

import java.io.FileNotFoundException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
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
	public void load() throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		super.loadJsonFile("maps/complex.json", false);
	}


	@Override
	public void update() {
		piggyGameMode.process();
	}


	@Override
	public String getName() {
		return "Hunter";
	}


}
