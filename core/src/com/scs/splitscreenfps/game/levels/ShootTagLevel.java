package com.scs.splitscreenfps.game.levels;

import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.AvatarFactory;
import com.scs.splitscreenfps.game.gamemodes.ShootTagSystem;

public class ShootTagLevel extends AbstractLevel {

	private ISystem shootTagSystem;

	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);

		this.shootTagSystem = new ShootTagSystem(game, game.ecs, 60);

	}


	@Override
	public int[] getHeroSelection() {
		return new int[]{AvatarFactory.CHAR_PIGGY};
	}


	@Override
	public void load() {
		try {
			super.loadJsonFile("maps/complex.json", false);
		} catch (Exception e) {
			throw new RuntimeException("Error loading map file", e);
		}

	}


	@Override
	public void update() {
		shootTagSystem.process();
	}


	@Override
	public String getName() {
		return "Shoot-Tag";
	}


}
