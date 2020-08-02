package com.scs.splitscreenfps.game.gamemodes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.DrawTextComponent;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.entities.AudioEntityFactory;
import com.scs.splitscreenfps.game.entities.AvatarFactory;
import com.scs.splitscreenfps.game.entities.TextEntity;

import ssmith.libgdx.ModelFunctions;

public class PiggyGameMode implements ISystem {

	private Game game;
	private long end_time;
	private AbstractEntity piggy;
	private AbstractEntity time_text;
	private int kills_required;
	private int prev_kills;
	private long game_duration;

	public PiggyGameMode(Game _game, BasicECS ecs) {
		game = _game;
		kills_required = 6;
		
		game_duration = (4 * 60 * 1000) - (game.players.length * 30*1000);
		end_time = System.currentTimeMillis() + game_duration;
		game.show_kills = true;
		
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
			// Set up game
			for(AbstractPlayersAvatar player : game.players) {
				if (player.hero_id == AvatarFactory.CHAR_PIGGY) {
					piggy = player;
				}
			}

			// Set colours
			for(AbstractPlayersAvatar avatar : game.players) {
				HasModelComponent hasModel = (HasModelComponent)avatar.getComponent(HasModelComponent.class);
				ModelInstance instance = hasModel.model;
				if (avatar.hero_id == AvatarFactory.CHAR_PIGGY) {
					ModelFunctions.setColour(instance, Color.RED);
				} else {
					ModelFunctions.setColour(instance, Color.GREEN);
				}
			}
			
			AbstractEntity sfx = AudioEntityFactory.createSfxEntityWithDelay(game.ecs, "music/5 Open Surge score jingle - A.ogg", 1f, game_duration-7000);
			game.ecs.addEntity(sfx);
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
