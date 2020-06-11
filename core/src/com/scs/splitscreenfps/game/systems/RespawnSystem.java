package com.scs.splitscreenfps.game.systems;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.components.WillRespawnComponent;

public class RespawnSystem implements ISystem {

	private List<AbstractEntity> entities = new ArrayList<AbstractEntity>();
	private BasicECS ecs;
	private Game game;

	public RespawnSystem(BasicECS _ecs, Game _game) {
		ecs = _ecs;
		game = _game;
	}


	public void addEntity(AbstractEntity e, Vector3 spawnPoint) {
		e.addComponent(new WillRespawnComponent(spawnPoint));
		this.entities.add(e);
		e.remove();
	}	


	@Override
	public void process() {
		for (int i=this.entities.size()-1 ; i>= 0 ; i--) {
			AbstractEntity e = this.entities.get(i);
			WillRespawnComponent wrc = (WillRespawnComponent)e.getComponent(WillRespawnComponent.class);
			if (wrc.respawn_time < System.currentTimeMillis()) {
				// Set position
				PositionComponent posData = (PositionComponent)e.getComponent(PositionComponent.class);
				posData.position.set(wrc.respawnPoint);


				e.removeComponent(WillRespawnComponent.class);
				ecs.addEntity(e);
				this.entities.remove(i);
			}
		}
	}

}