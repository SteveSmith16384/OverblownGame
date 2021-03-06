package com.scs.splitscreenfps.game.components;

import com.scs.basicecs.AbstractEntity;

public class JunkratMineAbilityComponent {

	public final long cooldown_duration;
	public float current_cooldown;

	public boolean button_released = true; // Prevent using all abilities in one go
	
	public int count_available;
	public int max_count;
	
	public AbstractEntity entity; // e.g. mine
	
	public JunkratMineAbilityComponent(long _cooldown) {
		cooldown_duration = _cooldown;
		current_cooldown = cooldown_duration;
		this.count_available = 0;
		this.max_count = 1;
	}
	

}

