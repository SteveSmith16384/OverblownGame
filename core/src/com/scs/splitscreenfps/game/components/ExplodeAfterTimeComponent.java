package com.scs.splitscreenfps.game.components;

import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.data.ExplosionData;

public class ExplodeAfterTimeComponent {

	public long explode_time;
	public ExplosionData explData;
	public AbstractEntity shooter;
	
	public ExplodeAfterTimeComponent(long time, ExplosionData _explData, AbstractEntity _shooter) {
		this.explode_time = System.currentTimeMillis() + time;
		explData = _explData;
		shooter = _shooter;
	}
	
}
