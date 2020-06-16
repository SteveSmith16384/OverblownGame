package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;

public class IsBulletComponent {

	public int side;
	public AbstractEntity shooter;
	public Vector3 start;
	public WeaponSettingsComponent settings;
	
	public IsBulletComponent(AbstractEntity _shooter, int _side, Vector3 _start, WeaponSettingsComponent _settings) {
		shooter = _shooter;
		side = _side;
		start = new Vector3(_start);
		settings = _settings;
	}
	
}
