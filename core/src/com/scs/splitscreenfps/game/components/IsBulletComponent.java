package com.scs.splitscreenfps.game.components;

import com.scs.basicecs.AbstractEntity;

public class IsBulletComponent {

	public int side;
	public AbstractEntity shooter;
	public boolean removeOnContact;
	
	public IsBulletComponent(AbstractEntity _shooter, int _side, boolean _removeOnContact) {
		shooter = _shooter;
		side = _side;
		removeOnContact = _removeOnContact;
	}
	
}
