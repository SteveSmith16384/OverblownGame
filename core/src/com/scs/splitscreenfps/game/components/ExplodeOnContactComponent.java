package com.scs.splitscreenfps.game.components;

import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.data.ExplosionData;

public class ExplodeOnContactComponent {

	public ExplosionData explData;
	public AbstractEntity shooter;
	public boolean remove;
	public boolean only_if_player;
	public boolean harm_shooter;

	public ExplodeOnContactComponent(ExplosionData _explData, AbstractEntity _shooter, boolean _remove, boolean _only_if_player, boolean _harm_shooter) {
		explData = _explData;
		shooter = _shooter;
		remove = _remove;
		only_if_player = _only_if_player;
		harm_shooter = _harm_shooter;
	}
}
