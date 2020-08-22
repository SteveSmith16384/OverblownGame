package com.scs.splitscreenfps.game.levels;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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

import me.lignum.jvox.VoxFile;
import me.lignum.jvox.VoxModel;
import me.lignum.jvox.VoxReader;
import me.lignum.jvox.Voxel;
import ssmith.lang.IOFunctions;

public abstract class AbstractLevel {

	public static final int LEVEL_FACTORY = 0;
	public static final int LEVEL_VILLAGE = 1;
	public static final int LEVEL_TEMPLE_OF_THE_NOOBIES = 2;
	public static final int LEVEL_ASSASSINS = 3;
	public static final int LEVEL_PIGGY = 4;
	public static final int LEVEL_SHOOT_TAG = 5;
	public static final int LEVEL_WHAT_THE_BALL = 6;
	public static final int LEVEL_CITY = 7;
	public static final int LEVEL_MINECRAFT = 8;
	public static final int LEVEL_MAP_EDITOR = 9;
	public static final int LEVEL_AI_TEST = 10;

	public static final int MAX_LEVEL_ID = 9;

	public Game game;
	protected List<Vector3> startPositions = new ArrayList<Vector3>();
	public MapData mapdata;
	
	public void getReadyForGame(Game _game) {
		game = _game;
	}


	public abstract String getName();

	public int[] getHeroSelection() {
		return new int[]{AvatarFactory.CHAR_PHARTAH, AvatarFactory.CHAR_BOOMFIST, AvatarFactory.CHAR_BOWLING_BALL, AvatarFactory.CHAR_RACER, AvatarFactory.CHAR_RUBBISHRODENT, AvatarFactory.CHAR_TOBLERONE};
	}


	public static final AbstractLevel factory(int i) {
		switch (i) {
		case LEVEL_FACTORY:
			return new FactoryLevel();
		case LEVEL_VILLAGE:
			return new VillageLevel();
		case LEVEL_TEMPLE_OF_THE_NOOBIES:
			return new TempleOfTheNoobiesLevel();
		case LEVEL_ASSASSINS:
			return new AssassinsLevel();
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
		case LEVEL_MINECRAFT:
			return new MinecraftLevel();
		default:
			throw new RuntimeException("Unknown level: " + i);
		}
	}


	public void setBackgroundColour() {
		Gdx.gl.glClearColor(0, .6f, .8f, 1);
	}


	public abstract void load() throws IOException; // todo - dopn't cvatch these in levels

	public abstract void update();

	public Vector3 getPlayerStartPoint(int idx) {
		if (this.startPositions.size() <= idx) {
			throw new RuntimeException("Not enough start positions; " + (idx+1) + " needed but only have " + this.startPositions.size());
		}
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


		if (mapdata.textures == null) {
			mapdata.textures = new HashMap<Integer, String>();
			mapdata.textures.put(1, "[texture filename]");
		}

		for (MapBlockComponent block : mapdata.blocks) {
			block.position.add(offset);

			if (block.position.y < -4f) { // Skip any that have fallen off the edge
				Settings.p("Ignoring" + block.name + " as it is too low");
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
				this.startPositions.add(new Vector3(1, 30f, 1)); // High we don't start inside any buildings
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
					block.mass);
			model.tags = block.tags;
			if (for_map_editor) {
				model.addComponent(block);
			}
			game.ecs.addEntity(model);
			return model;
		} else {
			String tex_data = this.mapdata.textures.get(block.texture_id);
			Texture tex = null;
			if (tex_data != null) {
				tex = game.getTexture(tex_data);
			} else {
				// Use dummy tex
				tex = game.getTexture("textures/neon/tron_green_2x2.png");
			}

			AbstractEntity wall = null;
			if (block.type == null || block.type.length() == 0 || block.type.equalsIgnoreCase("cube")) {
				wall = new Wall(game, block.name, tex, null, block.position.x, block.position.y, block.position.z, 
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


	public void saveFile(String filename) throws JsonIOException, IOException {
		if (mapdata == null) {
			mapdata = new MapData();
		}
		if (mapdata.blocks.size() == 0) {
			// Create dummy block
			MapBlockComponent block = new MapBlockComponent();
			mapdata.blocks.add(block);
		}

		// backup old file
		IOFunctions.copyFileUsingStream(filename, filename + "_old");

		JsonWriter writer = new JsonWriter(new FileWriter(filename));
		writer.setIndent("  ");
		Gson gson = new GsonBuilder().create();
		gson.toJson(mapdata, MapData.class, writer);
		writer.flush();
		writer.close();

	}
	
	
	public void loadVox(String filename, int mass, Vector3 offset, float scale) {//, int trunc) {
		try (VoxReader reader = new VoxReader(new FileInputStream(filename))) {
			VoxFile voxFile = reader.read();
			int count = 0;
			int num_removed = 0;
			for (VoxModel model : voxFile.getModels()) {
				
				int c = 0;
				boolean exists[][][] = new boolean[model.getSize().getX()][model.getSize().getY()][model.getSize().getZ()];
				for (Voxel voxel : model.getVoxels()) {
					exists[voxel.getPosition().getX()][voxel.getPosition().getY()][voxel.getPosition().getZ()] = true;
					c++;
				}
				
				/*boolean remove[][][] = new boolean[model.getSize().getX()][model.getSize().getY()][model.getSize().getZ()];
				for (int z=1 ; z<model.getSize().getZ()-1 ; z++) {
					for (int y=1 ; y<model.getSize().getY()-1 ; y++) {
						for (int x=1 ; x<model.getSize().getX()-1 ; x++) {
							if (exists[x][y][z]) {
								if (exists[x-1][y][z] && exists[x+1][y][z] && exists[x][y-1][z] && exists[x][y+1][z] && exists[x][y][z-1] && exists[x][y][z+1]) {
									remove[x][y][z] = true;
								}
							}
						}
					}
				}*/
				
				for (Voxel voxel : model.getVoxels()) {
					// Remove any voxels if they are surrounded by other voxels
					/*if (remove[voxel.getPosition().getX()][voxel.getPosition().getY()][voxel.getPosition().getZ()]) {
						exists[voxel.getPosition().getX()][voxel.getPosition().getY()][voxel.getPosition().getZ()] = false;
						num_removed++;
						continue;
					}*/
					int colour_id = voxel.getColourIndex() & 0xff;
					int colour = voxFile.getPalette()[colour_id];
					// Note that y and z seem to be reversed
					String hexColor = String.format("#%06X", (colour & 0xFFFFFF));
					String hexColor_rev = "#" + hexColor.substring(5) + hexColor.substring(3, 5) + hexColor.substring(1, 3);
					Color color = Color.valueOf(hexColor_rev);
					int tmp_mass = mass;
					if (voxel.getPosition().getZ() > 0 && exists[voxel.getPosition().getX()][voxel.getPosition().getY()][voxel.getPosition().getZ()-1] == false) {
						tmp_mass = 0; // Only give them mass if they are supported by another voxel
					}
					Wall wall = new Wall(game, "Voxel", null, color, (voxel.getPosition().getX() * scale)+offset.x, (voxel.getPosition().getZ()*scale)+offset.y, (voxel.getPosition().getY()*scale)+offset.z, 
							scale-.001f, scale-.001f, scale-.001f, 
							tmp_mass,
							0, 0, 0, false, true);
					game.ecs.addEntity(wall);
					count++;
				}
			}
			Settings.p(count + " voxels loaded");
			Settings.p(num_removed + " voxels removed");
		} catch (IOException e) {
		    e.printStackTrace();
		}

	}
}
