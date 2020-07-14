package com.scs.splitscreenfps.game.components;

import com.scs.splitscreenfps.game.data.ExplosionData;

public class ExplodeAfterTimeComponent {

	public long explode_time;
	public ExplosionData explData;
	
	public ExplodeAfterTimeComponent(long time, ExplosionData _explData) {
		this.explode_time = System.currentTimeMillis() + time;
		explData = _explData;
	}
	
}
