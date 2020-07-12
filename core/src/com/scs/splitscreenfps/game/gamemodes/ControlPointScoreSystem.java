package com.scs.splitscreenfps.game.gamemodes;

import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractEvent;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.EventCollision;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;

public class ControlPointScoreSystem implements ISystem {

	private static final long CHANGE_DURATION = 3000;
	private static final long WINNING_TIME = 100;

	private Game game;
	private AbstractEntity controlpoint;

	private AbstractPlayersAvatar current_owner = null;
	private AbstractPlayersAvatar last_player_to_touch = null;
	private long time_to_change;
	private long[] time_on_point;

	public ControlPointScoreSystem(Game _game) {
		game = _game;

		time_on_point = new long[game.players.length];
	}


	@Override
	public void process() {
		if (controlpoint == null) {
			// Find the control point entity - 
			Iterator<AbstractEntity> it = game.ecs.getEntityIterator();
			while (it.hasNext()) {
				AbstractEntity e = it.next();
				if (e.tags != null && e.tags.contains("controlpoint")) {
					this.controlpoint = e;
					break;
				}
			}

			if (this.controlpoint == null) {
				// We won't find it straight away, or until the entities are loaded
				return;
			}
		}

		if (this.last_player_to_touch != this.current_owner) {
			if (time_to_change < System.currentTimeMillis()) {
				this.current_owner = this.last_player_to_touch;
				this.changeTex(Settings.getColourForSide(current_owner.playerIdx));
				BillBoardFPS_Main.audio.play("controlpoint.mp3");
			}
		}

		if (current_owner != null) {
			this.time_on_point[current_owner.playerIdx] += Gdx.graphics.getDeltaTime();
			if (this.time_on_point[current_owner.playerIdx] >= WINNING_TIME) {
				game.playerHasWon(current_owner);
			}
		}

		// Check for collision events
		List<AbstractEvent> events = game.ecs.getEventsForEntity(EventCollision.class, this.controlpoint);
		for (AbstractEvent evt : events) {
			EventCollision coll = (EventCollision)evt;
			if (coll.entity2 instanceof AbstractPlayersAvatar) {
				AbstractPlayersAvatar player = (AbstractPlayersAvatar)coll.entity2;
				if (this.last_player_to_touch != player) {
					//Settings.p("Player " + )
					this.last_player_to_touch = player;
					this.time_to_change = System.currentTimeMillis() + CHANGE_DURATION;
					Color col = Color.GRAY;//Settings.getColourForSide(last_player_to_touch.playerIdx);
					this.changeTex(col);

				}
			}
		}
		
		// Update HUD
		

	}


	private void changeTex(Color col) {
		// change tex
		HasModelComponent hasModel = (HasModelComponent)controlpoint.getComponent(HasModelComponent.class);
		ModelInstance instance = hasModel.model;
		for (int i=0 ; i<instance.materials.size ; i++) {
			instance.materials.get(i).set(ColorAttribute.createDiffuse(col));
			instance.materials.get(i).set(ColorAttribute.createAmbient(col));
		}

	}

}
