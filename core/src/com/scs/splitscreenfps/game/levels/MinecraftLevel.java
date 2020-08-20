package com.scs.splitscreenfps.game.levels;

import java.util.Iterator;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.entities.Wall;
import com.scs.splitscreenfps.game.gamemodes.DeathmatchSystem;
import com.scs.splitscreenfps.game.mapdata.MapBlockComponent;

import ssmith.lang.NumberFunctions;

public class MinecraftLevel extends AbstractLevel {

	private ISystem deathmatchSystem;

	private Vector3 tmpVec = new Vector3();
	
	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);

		this.deathmatchSystem = new DeathmatchSystem(game, game.ecs, true);
	}


	@Override
	public void load() {

		try {
			super.loadJsonFile("maps/minecraft.json", false);
			game.ecs.addAndRemoveEntities();
			Iterator<AbstractEntity> it = game.ecs.getEntityIterator();
			while (it.hasNext()) {
				AbstractEntity e = it.next();
				if (e.tags.contains("church")) {
					e.remove();
					PhysicsComponent pc = (PhysicsComponent)e.getComponent(PhysicsComponent.class);
					pc.getTranslation(tmpVec);
					super.loadJsonFile("maps/minecraft_church.json", false, tmpVec, 5);
				} else if (e.tags.contains("house")) {
					e.remove();
					PhysicsComponent pc = (PhysicsComponent)e.getComponent(PhysicsComponent.class);
					pc.getTranslation(tmpVec);
					super.loadJsonFile("maps/minecraft_house.json", false, tmpVec, 5);
				} else if (e.tags.contains("skip")) {
					e.remove();
				}
			}

			// Remove entities with "skip"
			game.ecs.addAndRemoveEntities();
			it = game.ecs.getEntityIterator();
			while (it.hasNext()) {
				AbstractEntity e = it.next();
				if (e.tags.contains("skip")) {
					e.remove();
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error loading map file", e);
		}
	}


	@Override
	public void update() {
		deathmatchSystem.process();
	}


	@Override
	public String getName() {
		return "Minecraft Deathmatch";
	}


}
