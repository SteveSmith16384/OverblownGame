package com.scs.splitscreenfps.game.components;

import com.scs.basicecs.AbstractEntity;

public class CanCarryComponent {

	public boolean wantsToCarry = false;
	public AbstractEntity carrying;
	public int viewId = -1; // So we know where to draw it when picked up
	public long lastPickupDropTime = 0;
	
	public CanCarryComponent(int _viewId) {
		viewId = _viewId;
	}

}
