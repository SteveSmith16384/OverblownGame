package com.scs.splitscreenfps.selectcharacter;

public class GameSelectionData {

	public int[] character;
	public boolean[] selected_character;
	public int level;
	
	public GameSelectionData(int num_players) {
		this.character = new int[num_players];
		this.selected_character = new boolean[num_players];
	}
	
}
