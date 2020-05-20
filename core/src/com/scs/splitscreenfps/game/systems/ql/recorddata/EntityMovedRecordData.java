package com.scs.splitscreenfps.game.systems.ql.recorddata;

import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;

public class EntityMovedRecordData extends AbstractRecordData {

	public AbstractEntity entity;
	public int playerIdx;
	public Vector3 position = new Vector3();
	public float direction;
	
	public EntityMovedRecordData(int _playerIdx, AbstractEntity _entity, int phase, float _time, Vector3 pos, float dir) {
		super(CMD_MOVED, phase, _time);

		playerIdx = _playerIdx;
		entity = _entity;
		
		position.set(pos);
		direction = dir;
	}
	

}
