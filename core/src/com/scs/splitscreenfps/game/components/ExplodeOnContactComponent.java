package com.scs.splitscreenfps.game.components;

import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.data.ExplosionData;

public class ExplodeOnContactComponent {

	public ExplosionData explData;
	public AbstractEntity shooter;

	public ExplodeOnContactComponent(ExplosionData _explData, AbstractEntity _shooter) {
		explData = _explData;
		shooter = _shooter;
	}
}
