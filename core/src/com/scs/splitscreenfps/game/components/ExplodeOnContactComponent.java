package com.scs.splitscreenfps.game.components;

import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.data.ExplosionData;

public class ExplodeOnContactComponent {

	public ExplosionData explData;
	public AbstractEntity shooter;
	public boolean remove; // e.g. don't remove rockets as we still need the collision with a player

	public ExplodeOnContactComponent(ExplosionData _explData, AbstractEntity _shooter, boolean _remove) {
		explData = _explData;
		shooter = _shooter;
		remove = _remove;
	}
}
