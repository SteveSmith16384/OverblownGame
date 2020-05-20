package com.scs.splitscreenfps.game.gamemodes;

public interface IScoreSystem {
	
	int getWinningPlayer();
	
	void playerIsOnPoint(int side);

	String getHudText(int side);
}
