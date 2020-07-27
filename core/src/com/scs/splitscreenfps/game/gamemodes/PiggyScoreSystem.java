package com.scs.splitscreenfps.game.gamemodes;

import com.badlogic.gdx.graphics.Color;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.DrawTextComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.entities.AvatarFactory;
import com.scs.splitscreenfps.game.entities.TextEntity;

public class PiggyScoreSystem implements ISystem {

	private Game game;
	private long end_time;
	private AbstractEntity piggy;
	private AbstractEntity time_text;

	public PiggyScoreSystem(Game _game, BasicECS ecs) {
		game = _game;

		end_time = System.currentTimeMillis() + 2 * 60 * 1000;
	}


	@Override
	public void process() {
		if (game.game_stage != 0) {
			return;
		}

		if (time_text == null) {
			time_text = new TextEntity(game.ecs, "", 37, 52, -1, Color.WHITE, 0, game.font_large, true);
			game.ecs.addEntity(time_text);
		}
		DrawTextComponent dtd = (DrawTextComponent)time_text.getComponent(DrawTextComponent.class);
		dtd.text = "Time Left: " + ((end_time-System.currentTimeMillis())/1000);
		
		if (piggy == null) {
			for(AbstractPlayersAvatar player : game.players) {
				if (player.hero_id == AvatarFactory.CHAR_PIGGY) {
					piggy = player;
				}
			}
		} else {
			checkPiggy();

			if (this.end_time < System.currentTimeMillis()) {
				TextEntity text = new TextEntity(game.ecs, "PIGGY HAS LOST!", 37, 52, -1, Color.GREEN, 0, game.font_large, true);
				game.ecs.addEntity(text);
				game.playerHasLost(piggy);
			}
		}
	}


	private void checkPiggy() {
		PlayerData playerData = (PlayerData)piggy.getComponent(PlayerData.class);
		if (playerData.num_kills >= 10) {
			TextEntity text = new TextEntity(game.ecs, "PIGGY HAS WON!", 37, 52, -1, Color.RED, 0, game.font_large, true);
			game.ecs.addEntity(text);
			game.playerHasWon(piggy);
		}
	}


}
