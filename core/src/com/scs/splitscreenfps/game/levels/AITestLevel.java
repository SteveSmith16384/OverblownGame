package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.Gdx;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.HasAIComponent;
import com.scs.splitscreenfps.game.entities.AvatarFactory;
import com.scs.splitscreenfps.game.input.AIInputMethod;

public class AITestLevel extends AbstractLevel {

	//private ISystem deathmatchSystem;
	
	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);

		//this.deathmatchSystem = new DeathmatchSystem(game, game.ecs, true);
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

		/*
		AIInputMethod ai_input = new AIInputMethod();
		AbstractEntity ai = AvatarFactory.createAvatar(game, 3, game.viewports[3].camera, ai_input, AvatarFactory.CHAR_PHARTAH);
		ai.addComponent(new HasAIComponent(ai_input));
		*/
	}


	@Override
	public void update() {
		//deathmatchSystem.process();
	}


	@Override
	public int[] getHeroSelection() {
		return new int[]{1, 2, 3, 4}; // todo
	}


}
