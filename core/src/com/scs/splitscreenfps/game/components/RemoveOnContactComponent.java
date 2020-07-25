package com.scs.splitscreenfps.game.components;

import com.scs.basicecs.AbstractEntity;

public class RemoveOnContactComponent {

	public AbstractEntity ignore_collision_with;
	public boolean is_bullet; // Bullets won't collide with other bullets
	
	public RemoveOnContactComponent(AbstractEntity _ignore, boolean _is_bullet) {
		ignore_collision_with = _ignore;
		is_bullet = _is_bullet;
	}
	
}
