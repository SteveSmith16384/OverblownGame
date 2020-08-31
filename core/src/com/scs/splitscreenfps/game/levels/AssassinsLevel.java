package com.scs.splitscreenfps.game.levels;

import java.io.FileNotFoundException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.AvatarFactory;
import com.scs.splitscreenfps.game.gamemodes.DeathmatchSystem;

public class AssassinsLevel extends AbstractLevel {

	private ISystem deathmatchSystem;

	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);

		this.deathmatchSystem = new DeathmatchSystem(game, game.ecs, false);

	}


	@Override
	public int[] getHeroSelection() {
		return new int[]{AvatarFactory.CHAR_ASSASSIN};
	}


	@Override
	public void load() throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		super.loadJsonFile("maps/complex.json", false);
	}


	@Override
	public void update() {
		deathmatchSystem.process();
	}


	@Override
	public String getName() {
		return "The Assassins";
	}


}
