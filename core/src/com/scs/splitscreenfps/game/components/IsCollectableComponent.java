package com.scs.splitscreenfps.game.components;

import com.scs.splitscreenfps.game.systems.CollectableSystem;

public class IsCollectableComponent {

	public CollectableSystem.CollectableType type;
	public boolean can_be_collected = true;

	public IsCollectableComponent(CollectableSystem.CollectableType _type) {
		type = _type;
	}
	
}
