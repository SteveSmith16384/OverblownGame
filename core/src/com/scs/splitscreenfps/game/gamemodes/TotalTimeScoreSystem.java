package com.scs.splitscreenfps.game.gamemodes;

import com.badlogic.gdx.Gdx;
import com.scs.splitscreenfps.game.Game;

public class TotalTimeScoreSystem implements IScoreSystem {
	
	private Game game;
	private float[] timeOnPoint = new float[game.players.length];
	
	public TotalTimeScoreSystem(Game _game) {
		game = _game;
	}

	@Override
	public int getWinningPlayer() {
		if (game.players.length > 1) {
			if ((int)this.timeOnPoint[0] == (int)this.timeOnPoint[1]) {
				return -1;
			} else if ((int)this.timeOnPoint[0] < (int)this.timeOnPoint[1]) {
				return 1;
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}

	@Override
	public void playerIsOnPoint(int side) {
		timeOnPoint[side] += Gdx.graphics.getDeltaTime();
	}

	@Override
	public String getHudText(int side) {
		return  "Score: " + (int)(this.timeOnPoint[side]);
	}
	
}
