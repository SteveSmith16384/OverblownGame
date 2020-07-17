package com.scs.splitscreenfps.game.components;

import com.scs.basicecs.AbstractEntity;

public class RemoveOnContactComponent {

	public AbstractEntity ignore;
	
	public RemoveOnContactComponent(AbstractEntity _ignore) {
		ignore = _ignore;
	}
	
}
