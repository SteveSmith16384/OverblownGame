package com.scs.splitscreenfps.game.systems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.entities.EquipmentEntityFactory;

import ssmith.lang.NumberFunctions;

public class DispensePackageSystem implements ISystem {

	private static final long INTERVAL = 4000;

	private Game game;
	private long next_dispense_time;
	private List<Vector3> dispensers = new ArrayList<Vector3>();
	private int next_disp;

	public DispensePackageSystem(Game _game) {
		game = _game;
	}


	@Override
	public void process() {
		if (dispensers.size() == 0) {
			// Find the dispensers 
			Iterator<AbstractEntity> it = game.ecs.getEntityIterator();
			while (it.hasNext()) {
				AbstractEntity e = it.next();
				if (e.tags != null && e.tags.contains("dispenser")) {
					PositionComponent posData = (PositionComponent)e.getComponent(PositionComponent.class);
					this.dispensers.add(new Vector3(posData.position));
					e.remove();
				}
			}

			if (this.dispensers.size() == 0) {
				// We won't find it straight away, at least until the entities are loaded
				return;
			}
		}

		if (this.next_dispense_time < System.currentTimeMillis()) {
			this.next_dispense_time = System.currentTimeMillis() + INTERVAL;

			if (Settings.DEBUG_DISPENSER) {
				this.next_dispense_time = Long.MAX_VALUE;
			}
			
			Vector3 pos = this.dispensers.get(this.next_disp);
			AbstractEntity pkg = EquipmentEntityFactory.createPackage(game, pos.x, pos.y, pos.z, NumberFunctions.rnd(0, 3));
			game.ecs.addEntity(pkg);

			this.next_disp++;
			if (this.next_disp >= this.dispensers.size()) {
				this.next_disp = 0;
			}

		}

	}

}
