package com.scs.splitscreenfps.game.gamemodes;

import com.scs.basicecs.ISystem;

public class ScoreAndTimeLimitSystem implements ISystem {

	private int score;
	private int score_required;
	private long end_time;
	
	public ScoreAndTimeLimitSystem(int _score_required, long millis) {
		score_required = _score_required;
		
		this.end_time = System.currentTimeMillis() + millis;
	}
	
	
	public void incScore() {
		this.score++;
		if (score >= this.score_required) {
			// todo
		}
	}
	
	@Override
	public void process() {
		if (System.currentTimeMillis() > end_time) {
			//todo
		}
	}
	
	
	public String getScore() {
		return this.score + "/" + this.score_required;
	}

}
