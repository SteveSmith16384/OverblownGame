package com.scs.splitscreenfps.game.levels;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonWriter;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.EntityFactory;
import com.scs.splitscreenfps.game.entities.Wall;
import com.scs.splitscreenfps.game.mapdata.MapBlockComponent;
import com.scs.splitscreenfps.game.mapdata.MapData;

import ssmith.lang.IOFunctions;

public abstract class AbstractLevel {

	public static final int LEVEL_FACTORY = 0;
	public static final int LEVEL_VILLAGE = 1;
	public static final int MAX_LEVELS = 2;

	public Game game;
	protected List<Vector3> startPositions = new ArrayList<Vector3>();
	public MapData mapdata;

	public AbstractLevel(Game _game) {
		game = _game;
	}


	public static final String getName(int i) {
		switch (i) {
		case LEVEL_FACTORY:
			return "Deathmatch Factory";
		case LEVEL_VILLAGE:
			return "Control Point Village";
		default:
			throw new RuntimeException("Unknown level: " + i);
		}
	}


	public static final AbstractLevel factory(int i, Game game) {
		switch (i) {
		case LEVEL_FACTORY:
			return new FactoryLevel(game);
		case LEVEL_VILLAGE:
			return new VillageLevel(game);
		default:
			throw new RuntimeException("Unknown level: " + i);
		}
	}


	public void setBackgroundColour() {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1);
	}

	public abstract void load();

	public abstract void update();

	public abstract void renderUI(SpriteBatch batch2d, int currentViewId);

	public Vector3 getPlayerStartPoint(int idx) {
		return this.startPositions.get(idx);
	}


	public void loadJsonFile(String filename, boolean for_map_editor) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		Gson gson = new Gson();

		mapdata = gson.fromJson(new FileReader(filename), MapData.class);
		mapdata.filename = filename;

		if (mapdata.textures == null) {
			mapdata.textures = new HashMap<Integer, String>();
			mapdata.textures.put(1, "[texture filename]");
		}


		for (MapBlockComponent block : mapdata.blocks) {
			if (block.position.y < -4f) { // Skip any that have fallen off the edge
				continue;
			}
			if (block.tags.contains("playerstartposition")) {
				if (startPositions == null) {
					startPositions = new ArrayList<Vector3>();
				}
				startPositions.add(block.position);
				if (for_map_editor == false) {
					continue; // Don't add player start sphere!
				}
			} else if (block.tags.contains("healthpack")) {
				AbstractEntity health = EntityFactory.createHealthPack(game, block.position);
				//todo -re-add game.ecs.addEntity(health);
				continue;
			}
			game.currentLevel.createAndAddEntityFromBlockData(block, for_map_editor);
		}

	}


	public AbstractEntity createAndAddEntityFromBlockData(MapBlockComponent block) {
		return this.createAndAddEntityFromBlockData(block, true);
	}


	public AbstractEntity createAndAddEntityFromBlockData(MapBlockComponent block, boolean for_map_editor) {
		if (block.id == 0) {
			block.id = MapBlockComponent.next_id++;
		}
		if (block.model_filename != null && block.model_filename.length() > 0) {
			AbstractEntity model = EntityFactory.createModel(game.ecs, block.name, block.model_filename, 
					8, -2f, 7, 
					block.mass, null);
			model.tags = block.tags;
			if (for_map_editor) {
				model.addComponent(block);
			}
			game.ecs.addEntity(model);
			return model;
		} else {
			String tex = "textures/neon/tron_green_2x2.png"; // Default
			if (this.mapdata.textures.containsKey(block.texture_id)) {
				tex = this.mapdata.textures.get(block.texture_id);
			}
			AbstractEntity wall = null;
			if (block.type == null || block.type.length() == 0 || block.type.equalsIgnoreCase("cube")) {
				wall = new Wall(game, block.name, tex, block.position.x, block.position.y, block.position.z, 
						block.size.x, block.size.y, block.size.z, 
						block.mass * 5, // Hack to make walls heavier
						block.rotation.x, block.rotation.y, block.rotation.z, block.tiled, true);
			} else if (block.type.equalsIgnoreCase("sphere")) {
				wall = EntityFactory.createBall(game, tex, block.position.x, block.position.y, block.position.z, block.size.x, block.mass);
			} else if (block.type.equalsIgnoreCase("cylinder")) {
				wall = EntityFactory.createCylinder(game, tex, block.position.x, block.position.y, block.position.z, block.size.x, block.size.y, block.mass);
			} else if (block.type.equalsIgnoreCase("plane")) {
				wall = EntityFactory.createPlane(game, tex, block.position.x, block.position.y, block.position.z, block.size.x, block.size.y);
			} else {
				throw new RuntimeException("Unknown type: " + block.type);
			}
			wall.tags = block.tags;
			if (for_map_editor) {
				wall.addComponent(block);
			}
			game.ecs.addEntity(wall);
			return wall;
		}
	}


	public void saveFile() throws JsonIOException, IOException {
		if (mapdata == null) {
			mapdata = new MapData();
		}
		if (mapdata.blocks.size() == 0) {
			// Create dummy block
			MapBlockComponent block = new MapBlockComponent();
			mapdata.blocks.add(block);
		}

		// backup old file
		IOFunctions.copyFileUsingStream(mapdata.filename, mapdata.filename + "_old");

		JsonWriter writer = new JsonWriter(new FileWriter(mapdata.filename));
		writer.setIndent("  ");
		Gson gson = new GsonBuilder().create();
		gson.toJson(mapdata, MapData.class, writer);
		writer.flush();
		writer.close();

	}
}
