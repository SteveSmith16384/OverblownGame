package com.scs.splitscreenfps.game.components;

import com.scs.basicecs.AbstractEntity;

public class IsBulletComponent {

	public int side;
	public AbstractEntity shooter;
	
	public IsBulletComponent(AbstractEntity _shooter, int _side) {
		shooter = _shooter;
		side = _side;
	}
	
}
