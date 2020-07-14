package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;

public class IsBulletComponent {

	public AbstractEntity shooter;
	public Vector3 start;
	public WeaponSettingsComponent settings;
	public boolean remove_on_contact;
	
	public IsBulletComponent(AbstractEntity _shooter, Vector3 _start, WeaponSettingsComponent _settings, boolean _remove_on_contact) {
		shooter = _shooter;
		start = new Vector3(_start);
		settings = _settings;
		remove_on_contact = _remove_on_contact;
	}
	
}
