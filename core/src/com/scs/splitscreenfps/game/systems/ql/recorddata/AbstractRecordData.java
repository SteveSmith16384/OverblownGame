package com.scs.splitscreenfps.game.systems.ql.recorddata;

public abstract class AbstractRecordData {

	public static final int CMD_BULLET_FIRED = 1; // todo - make enums
	public static final int CMD_MOVED = 2;
	public static final int CMD_REMOVED = 3;
	
	public int cmd;
	public float time;
	public int phase;
	
	public AbstractRecordData(int _cmd, int _phase, float _time) {
		cmd = _cmd;
		time = _time;
		phase = _phase;
	}

}
