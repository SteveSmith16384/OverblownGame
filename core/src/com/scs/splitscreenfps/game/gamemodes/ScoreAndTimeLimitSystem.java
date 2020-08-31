package com.scs.splitscreenfps.game.gamemodes;

import com.badlogic.gdx.graphics.Color;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.DrawTextComponent;
import com.scs.splitscreenfps.game.entities.TextEntity;

public class ScoreAndTimeLimitSystem implements ISystem {

	private boolean setup = false;
	private TextEntity time_text;
	private Game game;
	private int score;
	private int score_required;
	private long end_time;

	public ScoreAndTimeLimitSystem(Game _game, int _score_required, long millis) {
		game =_game;
		score_required = _score_required;
		this.end_time = System.currentTimeMillis() + millis;
	}


	public void incScore(int amt) {
		this.score += amt;

		updateScore();

		if (score >= this.score_required) {
			this.time_text.remove();
			TextEntity text = new TextEntity(game.ecs, "YOU HAVE WON!", 37, 52, -1, Color.GREEN, 0, game.font_large, true);
			game.ecs.addEntity(text);

			game.nextLevel();
			game.playerHasWon(null);
		}
	}

	@Override
	public void process() {
		if (game.game_stage != 0) {
			return;
		}
		if (setup == false) {
			setup = true;

			time_text = new TextEntity(game.ecs, "", 37, 52, -1, Color.YELLOW, 0, game.font_med, true);
			game.ecs.addEntity(time_text);

		}

		this.updateScore();

		if (System.currentTimeMillis() > end_time) {
			this.time_text.remove();
			TextEntity text = new TextEntity(game.ecs, "YOU HAVE LOST!", 37, 52, -1, Color.RED, 0, game.font_large, true);
			game.ecs.addEntity(text);
			game.playerHasLost(null);
		}
	}


	private void updateScore() {
		if (time_text != null) {
			DrawTextComponent dtd = (DrawTextComponent)time_text.getComponent(DrawTextComponent.class);
			dtd.dirty = true;
			long time_left = (this.end_time - System.currentTimeMillis()) / 1000;
			dtd.text = "Score: " + this.score + "/" + this.score_required + "   Time: " + time_left;
		}
	}
}
