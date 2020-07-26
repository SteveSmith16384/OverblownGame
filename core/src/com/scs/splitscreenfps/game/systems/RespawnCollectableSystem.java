package com.scs.splitscreenfps.game.systems;

import java.util.LinkedList;
import java.util.List;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.game.components.HasDecal;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.IsCollectableComponent;

public class RespawnCollectableSystem implements ISystem {

	private List<RespawnData> points = new LinkedList<RespawnData>();

	public RespawnCollectableSystem() {
	}

	
	@Override
	public void process() {
		synchronized (points) {
			if (points.size() > 0) {
				RespawnData data = points.get(0);
				if (data.respawnTime < System.currentTimeMillis()) {
					points.remove(0);
					HasModelComponent model = (HasModelComponent)data.entity.getComponent(HasModelComponent.class);
					if (model != null) {
						model.invisible = false;
					}
					HasDecal decal = (HasDecal)data.entity.getComponent(HasDecal.class);
					if (decal != null) {
						decal.invisible = false;
					}
					IsCollectableComponent collectable = (IsCollectableComponent)data.entity.getComponent(IsCollectableComponent.class);
					collectable.can_be_collected = true;

				}
			}
		}
	}


	public void addEntity(AbstractEntity p) {
		RespawnData data = new RespawnData(p);
		synchronized (points) {
			points.add(data);
		}
	}
	

	class RespawnData {

		public AbstractEntity entity;
		public long respawnTime;

		public RespawnData(AbstractEntity _entity) {
			entity = _entity;
			respawnTime = System.currentTimeMillis() + 5000;
		}
	}



}
