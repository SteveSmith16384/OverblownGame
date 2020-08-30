package com.scs.splitscreenfps.game.systems;

import java.util.Iterator;

import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.entities.EquipmentEntityFactory;

import ssmith.lang.NumberFunctions;

public class DispensePackageSystem implements ISystem {

	private static final long INTERVAL = 4000;

	private Game game;
	private long next_dispense_time;
	private Vector3[] dispensers = new Vector3[4];
	private int next_disp;

	public DispensePackageSystem(Game _game) {
		game = _game;
	}


	@Override
	public void process() {
		if (dispensers[0] == null) {
			// Find the dispensers 
			Iterator<AbstractEntity> it = game.ecs.getEntityIterator();
			while (it.hasNext()) {
				AbstractEntity e = it.next();
				if (e.tags != null && e.tags.contains("dispenser")) {
					int type = Integer.parseInt(e.tags.substring(e.tags.length()-1));
					if (type <= game.players.length) { // One more dispensers than players
						PositionComponent posData = (PositionComponent)e.getComponent(PositionComponent.class);
						this.dispensers[type] = new Vector3(posData.position);
					}
					e.remove();
				}
			}

			if (dispensers[0] == null) {
				// We won't find it straight away, at least until the entities are loaded
				return;
			}
		}

		if (this.next_dispense_time < System.currentTimeMillis()) {
			this.next_dispense_time = System.currentTimeMillis() + INTERVAL;

			if (game.ecs.getNumEntities() < 100) { // Prevent too many
				if (Settings.DEBUG_DISPENSER) {
					this.next_dispense_time = Long.MAX_VALUE;
				}

				Vector3 pos = this.dispensers[this.next_disp];
				if (pos == null) {
					throw new RuntimeException("Here");
				}
				int pkg_type = NumberFunctions.rnd(0, game.players.length);
				AbstractEntity pkg = EquipmentEntityFactory.createPackage(game, pos.x, pos.y, pos.z, pkg_type);
				game.ecs.addEntity(pkg);

				BillBoardFPS_Main.audio.play("sfx/Menu_Navigate_00.mp3");

				this.next_disp++;
				if (this.next_disp > game.players.length) {
					this.next_disp = 0;
				}

			} else {
				Settings.p("Too many entities to dispense package");
			}

		}

	}

}
