package com.scs.splitscreenfps.game.components;

public class RemoveComponentAfterTimeComponent {

	public Class<?> clazz;
	public long expires;
	
	public RemoveComponentAfterTimeComponent(Class<?> _clazz, long _expires) {
		clazz = _clazz;
		expires = _expires;
	}

}
