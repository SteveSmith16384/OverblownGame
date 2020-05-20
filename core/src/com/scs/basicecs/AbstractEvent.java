package com.scs.basicecs;

public abstract class AbstractEvent {

	public int type;
	
	public abstract boolean isForEntity(AbstractEntity e);
	/*public Event(int _type) {
		type = _type;
	}*/
	
}
