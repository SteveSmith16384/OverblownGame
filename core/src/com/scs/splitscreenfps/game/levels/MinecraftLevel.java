package com.scs.splitscreenfps.game.levels;

import java.io.FileNotFoundException;
import java.util.Iterator;

import com.badlogic.gdx.math.Vector3;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.entities.SkyboxCube;
import com.scs.splitscreenfps.game.gamemodes.DeathmatchSystem;

public class MinecraftLevel extends AbstractLevel {

	private ISystem deathmatchSystem;

	private Vector3 tmpVec = new Vector3();

	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);

		this.deathmatchSystem = new DeathmatchSystem(game, game.ecs, true);
	}


	@Override
	public void load() throws JsonSyntaxException, JsonIOException, FileNotFoundException {
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

		game.ecs.addEntity(new SkyboxCube(game, "Skybox", "textures/sky3.jpg", 90, 90, 90));
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
