package com.scs.splitscreenfps.game.gamemodes;

import com.badlogic.gdx.graphics.Color;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.DrawTextComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.entities.AvatarFactory;
import com.scs.splitscreenfps.game.entities.TextEntity;

public class PiggyGameMode implements ISystem {

	private Game game;
	private long end_time;
	private AbstractEntity piggy;
	private AbstractEntity time_text;
	private int kills_required;
	private int prev_kills;

	public PiggyGameMode(Game _game, BasicECS ecs) {
		game = _game;
		kills_required = 6;
		end_time = System.currentTimeMillis() + 3 * 60 * 1000;
	}


	@Override
	public void process() {
		if (game.game_stage != 0) {
			return;
		}

		if (time_text == null) {
			time_text = new TextEntity(game.ecs, "", 37, 52, -1, Color.WHITE, 0, game.font_med, true);
			game.ecs.addEntity(time_text);
		}

		if (piggy == null) {
			// Sewt up game
			for(AbstractPlayersAvatar player : game.players) {
				if (player.hero_id == AvatarFactory.CHAR_PIGGY) {
					piggy = player;
				}
			}

			// Set colours
			for(AbstractPlayersAvatar avatar : game.players) {
				if (avatar.hero_id == AvatarFactory.CHAR_PIGGY) {
					avatar.setColour(Color.RED);
				} else {
					avatar.setColour(Color.GREEN);
				}
			}
		} else {
			PlayerData playerData = (PlayerData)piggy.getComponent(PlayerData.class);

			if (this.prev_kills != playerData.num_kills) {
				BillBoardFPS_Main.audio.play("sfx/so thats coming along.wav");
				this.prev_kills = playerData.num_kills;
			}
			
			DrawTextComponent dtd = (DrawTextComponent)time_text.getComponent(DrawTextComponent.class);
			dtd.dirty = true;
			dtd.text = "Time Left: " + ((end_time-System.currentTimeMillis())/1000) + "  Kills: " + playerData.num_kills + "/" + this.kills_required;

			checkIfPiggyHasWon(playerData);

			if (this.end_time < System.currentTimeMillis()) {
				time_text.remove();
				time_text = null;
				
				TextEntity text = new TextEntity(game.ecs, "PIGGY HAS LOST!", 37, 52, -1, Color.GREEN, 0, game.font_large, true);
				game.ecs.addEntity(text);
				game.playerHasLost(piggy);
			}
		}
	}


	private void checkIfPiggyHasWon(PlayerData piggyPlayerData) {
		if (piggyPlayerData.num_kills >= kills_required) {
			time_text.remove();
			time_text = null;
			
			TextEntity text = new TextEntity(game.ecs, "PIGGY HAS WON!", 37, 52, -1, Color.RED, 0, game.font_large, true);
			game.ecs.addEntity(text);
			game.playerHasWon(piggy);
		}
	}


}
