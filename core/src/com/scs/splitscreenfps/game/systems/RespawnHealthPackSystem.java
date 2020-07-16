package com.scs.splitscreenfps.game.systems;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.BasicECS;
import com.scs.basicecs.ISystem;

public class RespawnHealthPackSystem implements ISystem {

	private List<RespawnData> points = new LinkedList<RespawnData>();
	private BasicECS ecs;

	public RespawnHealthPackSystem(BasicECS _ecs) {
		ecs = _ecs;
	}

	@Override
	public void process() {
		synchronized (points) {
			if (points.size() > 0) {
				RespawnData data = points.get(0);
				if (data.respawnTime < System.currentTimeMillis()) {
					points.remove(0);
					// todo - add healthpack
				}
			}
		}
	}


	public void addPoint(Vector3 p) {
		RespawnData data = new RespawnData(p);
		synchronized (points) {
			points.add(data);
		}
	}

	class RespawnData {

		public Vector3 pos;
		public long respawnTime;

		public RespawnData(Vector3 _pos) {
			pos = _pos;
			respawnTime = System.currentTimeMillis() + 5000;
		}
	}



}
