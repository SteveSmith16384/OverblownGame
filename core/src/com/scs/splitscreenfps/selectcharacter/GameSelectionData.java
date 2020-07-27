package com.scs.splitscreenfps.selectcharacter;

public class GameSelectionData {

	public int[] character;
	public boolean[] has_selected_character;
	public int level;
	
	public GameSelectionData() {//int num_players) {
		this.character = new int[4]; // Always have 4 in case we need to add some AI
		this.has_selected_character = new boolean[4];
	}
	
}
