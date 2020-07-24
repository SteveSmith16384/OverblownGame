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
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.DrawTextComponent;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.entities.TextEntity;
import com.scs.splitscreenfps.game.events.EventCollision;

public class ControlPointScoreSystem implements ISystem {

	private static final long CHANGE_DURATION = 3000;
	private static final long WINNING_TIME = 100;

	private Game game;
	private AbstractEntity controlpoint;

	private AbstractPlayersAvatar current_owner = null;
	//private AbstractPlayersAvatar last_player_to_touch = null;
	//private long time_to_change;
	private float[] time_on_point;
	private TextEntity text;
	private StringBuilder str = new StringBuilder();

	public ControlPointScoreSystem(Game _game) {
		game = _game;

		time_on_point = new float[game.players.length];
	}


	@Override
	public void process() {
		if (game.game_stage > 0) {
			return;
		}

		if (text == null) {
			text = new TextEntity(game.ecs, "Point Unclaimed", 37, 52, -1, Color.WHITE, 0, game.font_med, true);
			game.ecs.addEntity(text);
		}

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
				// We won't find it straight away, at least until the entities are loaded
				return;
			}
		}

		// Check for collision events
		List<AbstractEvent> events = game.ecs.getEventsForEntity(EventCollision.class, this.controlpoint);
		for (AbstractEvent evt : events) {
			EventCollision coll = (EventCollision)evt;
			if (coll.entity2 instanceof AbstractPlayersAvatar) {
				AbstractPlayersAvatar player = (AbstractPlayersAvatar)coll.entity2;
				if (this.current_owner != player) {
					this.current_owner = player;
					this.changeTex(Settings.getColourForSide(current_owner.playerIdx));
					BillBoardFPS_Main.audio.play("sfx/controlpoint.mp3");
					//this.last_player_to_touch = player;
					//this.time_to_change = System.currentTimeMillis() + CHANGE_DURATION;
					//Settings.p("Changing colour to grey");
					//this.changeTex(Color.GRAY);
				}
			}
		}

		/*if (time_to_change < System.currentTimeMillis()) {
			if (this.last_player_to_touch != this.current_owner) {
				this.current_owner = this.last_player_to_touch;
				Settings.p("Changing colour to side " + current_owner.playerIdx);
				this.changeTex(Settings.getColourForSide(current_owner.playerIdx));
				BillBoardFPS_Main.audio.play("sfx/controlpoint.mp3");
			}
		 */
		if (current_owner != null) {
			// Update HUD
			str.delete(0,  str.length());
			for (int i=0 ; i<game.players.length ; i++) {
				str.append(PlayerData.getName(i) + ": " + (int)(this.time_on_point[i]) + "  ");
			}
			DrawTextComponent dtd = (DrawTextComponent)text.getComponent(DrawTextComponent.class);
			dtd.text = str.toString();
			this.time_on_point[current_owner.playerIdx] += Gdx.graphics.getDeltaTime();
			if (this.time_on_point[current_owner.playerIdx] >= WINNING_TIME) {
				//DrawTextData dtd = (DrawTextData)text.getComponent(DrawTextData.class);
				PlayerData playerData = (PlayerData)current_owner.getComponent(PlayerData.class);
				dtd.text = playerData.playerName + " HAS WON!";
				game.playerHasWon(current_owner);
				return;
			}
		}
		//}

	}


	private void changeTex(Color col) {
		HasModelComponent hasModel = (HasModelComponent)controlpoint.getComponent(HasModelComponent.class);
		ModelInstance instance = hasModel.model;
		for (int i=0 ; i<instance.materials.size ; i++) {
			instance.materials.get(i).set(ColorAttribute.createDiffuse(col));
			instance.materials.get(i).set(ColorAttribute.createAmbient(col));
		}

		DrawTextComponent dtd = (DrawTextComponent)text.getComponent(DrawTextComponent.class);
		dtd.colour = col;
	}

}
