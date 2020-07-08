package com.scs.splitscreenfps.selectcharacter;

public class GameSelectionData {

	public int[] character;
	public boolean[] has_selected_character;
	public int level;
	
	public GameSelectionData(int num_players) {
		this.character = new int[num_players];
		this.has_selected_character = new boolean[num_players];
	}
	
}
