package com.scs.splitscreenfps.game.components;

import com.scs.splitscreenfps.Settings;

public class PlayerData {

	public float health = Settings.START_HEALTH;
	public int playerIdx;
	public long restartTime;
	
	public PlayerData(int _playerIdx) {
		playerIdx = _playerIdx;
	}
	
}
