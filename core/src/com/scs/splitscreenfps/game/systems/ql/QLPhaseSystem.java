package com.scs.splitscreenfps.game.systems.ql;

import com.badlogic.gdx.Gdx;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.levels.QuantumLeagueLevel;

public class QLPhaseSystem implements ISystem {

	private QuantumLeagueLevel qlLevel;
	private boolean game_phase; // otherwise, rewind phase
	private float current_time;
	private int phase_num_012 = -1;

	public QLPhaseSystem(QuantumLeagueLevel _level) {
		qlLevel = _level;
	}


	public int getPhaseNum012() {
		return this.phase_num_012;
	}


	@Override
	public void process() {
		if (this.game_phase) {
			current_time += Gdx.graphics.getDeltaTime();
			if (current_time > Settings.GAME_PHASE_DURATION) {
				this.game_phase = false;
				if (phase_num_012 <= 1) {
					//Settings.p("Rewind phase!");
					game_phase = false;
					qlLevel.startRewindPhase();
				} else {
					qlLevel.allPhasesOver();
				}
			}
		}
	}


	public void startGamePhase() {
		//Settings.p("Game phase!");
		this.game_phase = true;
		current_time = 0;
		phase_num_012++;
	}


	public float getCurrentPhaseTime() {
		return current_time;
	}


	public boolean isGamePhase() {
		return this.game_phase;
	}

}
