package com.scs.splitscreenfps.game.components;

import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.Settings;

public class PlayerData {

	public float health = Settings.START_HEALTH;
	public int playerIdx;
	public long restartTime;
	public int points;
	public AbstractEntity last_person_to_hit_them;
	
	public PlayerData(int _playerIdx) {
		playerIdx = _playerIdx;
	}
	
}
