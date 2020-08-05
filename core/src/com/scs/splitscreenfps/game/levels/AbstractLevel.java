package com.scs.splitscreenfps.game.levels;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonWriter;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.AvatarFactory;
import com.scs.splitscreenfps.game.entities.EntityFactory;
import com.scs.splitscreenfps.game.entities.Wall;
import com.scs.splitscreenfps.game.mapdata.MapBlockComponent;
import com.scs.splitscreenfps.game.mapdata.MapData;

import ssmith.lang.IOFunctions;

public abstract class AbstractLevel {

	public static final int LEVEL_FACTORY = 0;
	public static final int LEVEL_VILLAGE = 1;
	public static final int LEVEL_TEMPLE_OF_THE_NOOBIES = 2;
	public static final int LEVEL_BLOWPIPE_ASSASSINS = 3;
	public static final int LEVEL_PIGGY = 4;
	public static final int LEVEL_SHOOT_TAG = 5;
	public static final int LEVEL_WHAT_THE_BALL = 6;
	public static final int LEVEL_MAP_EDITOR = 7;
	public static final int LEVEL_CITY = 8;
	public static final int LEVEL_AI_TEST = 9;
	
	public static final int MAX_LEVEL_ID = 7;

	public Game game;
	protected List<Vector3> startPositions = new ArrayList<Vector3>();
	public MapData mapdata;

	public void getReadyForGame(Game _game) {
		game = _game;
	}


	public abstract String getName();
	
	public int[] getHeroSelection() {
		return new int[]{AvatarFactory.CHAR_PHARTAH, AvatarFactory.CHAR_BOOMFIST, AvatarFactory.CHAR_BOWLING_BALL, AvatarFactory.CHAR_RACER, AvatarFactory.CHAR_RUBBISHRODENT};
	}


	
	/*
	public static final String getName(int i) {
		switch (i) {
		case LEVEL_FACTORY:
			return "Factory - Deathmatch";
		case LEVEL_CITY:
			return "City - Deathmatch";
		case LEVEL_VILLAGE:
			return "Village - Control Point";
		case LEVEL_TEMPLE_OF_THE_NOOBIES:
			return "Temple of the Noobies - Deathmatch";
		case LEVEL_BLOWPIPE_ASSASSINS:
			return "The Assassins - Deathmatch";
		case LEVEL_PIGGY:
			return "Piggy";
		case LEVEL_AI_TEST:
			return "AI Test";
		case LEVEL_MAP_EDITOR:
			return "Map Editor";
		case LEVEL_SHOOT_TAG:
			return "Shoot-Tag";
		case LEVEL_WHAT_THE_BALL:
			return "What the Ball";
		default:
			throw new RuntimeException("Unknown level: " + i);
		}
	}
*/

	public static final AbstractLevel factory(int i) {
		switch (i) {
		case LEVEL_FACTORY:
			return new FactoryLevel();
		case LEVEL_VILLAGE:
			return new VillageLevel();
		case LEVEL_TEMPLE_OF_THE_NOOBIES:
			return new TempleOfTheNoobiesLevel();
		case LEVEL_BLOWPIPE_ASSASSINS:
			return new BlowpipeLevel();
		case LEVEL_PIGGY:
			return new PiggyLevel();
		case LEVEL_AI_TEST:
			return new AITestLevel();
		case LEVEL_SHOOT_TAG:
			return new ShootTagLevel();
		case LEVEL_WHAT_THE_BALL:
			return new WhatTheBallLevel();
		case LEVEL_MAP_EDITOR:
			return new MapEditorLevel();
		case LEVEL_CITY:
			return new CityLevel();
		default:
			throw new RuntimeException("Unknown level: " + i);
		}
	}


	public void setBackgroundColour() {
		Gdx.gl.glClearColor(0, .6f, .8f, 1);
	}


	public abstract void load();

	public abstract void update();

	public Vector3 getPlayerStartPoint(int idx) {
		return this.startPositions.get(idx);
	}


	public void loadJsonFile(String filename, boolean for_map_editor) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		this.loadJsonFile(filename, for_map_editor, Vector3.Zero, 5);
	}
	
	
	/**
	 * Load a map file but offset all the entities by the specified position.  Useful for when loading multiple
	 * map files for one level.
	 * 
	 * @param filename
	 * @param for_map_editor
	 * @param offset
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 * @throws FileNotFoundException
	 */
	public void loadJsonFile(String filename, boolean for_map_editor, Vector3 offset, float mass_mult) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		Gson gson = new Gson();

		String s = Gdx.files.internal(filename).readString();
		mapdata = gson.fromJson(s, MapData.class);
		mapdata.filename = filename;

		if (mapdata.textures == null) {
			mapdata.textures = new HashMap<Integer, String>();
			mapdata.textures.put(1, "[texture filename]");
		}


		for (MapBlockComponent block : mapdata.blocks) {
			block.position.add(offset);
			
			if (block.position.y < -4f) { // Skip any that have fallen off the edge
				Settings.p("Ignoreing" + block.name + " as it is too low");
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
			} else if (for_map_editor == false && block.tags.contains("healthpack")) {
				AbstractEntity health = EntityFactory.createHealthPack(game, block.position);
				game.ecs.addEntity(health);
				continue;
			}
			game.currentLevel.createAndAddEntityFromBlockData(block, for_map_editor, mass_mult);
		}
		
		if (startPositions.size() < 4) {
			Settings.pe("Warning: only " + startPositions.size() + " start positions");
			while (this.startPositions.size() < 4) {
				// Add default start positions
				//game.appendToLog("Adding default start position");
				this.startPositions.add(new Vector3(1, 2f, 1));
			}

		}
	}


	public AbstractEntity createAndAddEntityFromBlockData(MapBlockComponent block) {
		return this.createAndAddEntityFromBlockData(block, true, 5);
	}


	public AbstractEntity createAndAddEntityFromBlockData(MapBlockComponent block, boolean for_map_editor, float mass_mult) {
		if (block.id == 0) {
			block.id = MapBlockComponent.next_id++;
		}
		if (block.model_filename != null && block.model_filename.length() > 0) {
			AbstractEntity model = EntityFactory.createModel(game.ecs, block.name, block.model_filename, 
					block.position.x, block.position.y, block.position.z, 
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
						block.mass * mass_mult, // Hack to make walls heavier
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
