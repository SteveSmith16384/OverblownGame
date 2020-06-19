package com.scs.splitscreenfps.game.levels;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonWriter;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.mapdata.MapBlockComponent;
import com.scs.splitscreenfps.game.mapdata.MapData;

import ssmith.lang.IOFunctions;

public abstract class AbstractLevel implements ILevelInterface {

	public Game game;
	protected List<Vector3> startPositions = new ArrayList<Vector3>();
	public MapData mapdata;

	public AbstractLevel(Game _game) {
		game = _game;
	}


	public void setBackgroundColour() {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1);
	}

	public abstract void load();

	public abstract void update();

	public Vector3 getPlayerStartPoint(int idx) {
		return this.startPositions.get(idx);
	}


	public void loadJsonFile(String filename) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		Gson gson = new Gson();

		// 1. JSON file to Java object
		mapdata = gson.fromJson(new FileReader(filename), MapData.class);
		mapdata.filename = filename;
		
		for (MapBlockComponent block : mapdata.blocks) {
			/*if (block.model_filename != null && block.model_filename.length() > 0) {
				AbstractEntity doorway = EntityFactory.Model(game.ecs, block.name, block.model_filename, 
						8, -2f, 7, 
						block.mass);
				doorway.addComponent(block);
				game.ecs.addEntity(doorway);
			} else if (block.texture_filename != null && block.texture_filename.length() > 0) {
				Wall wall = new Wall(game.ecs, block.name, block.texture_filename, block.position.x, block.position.y, block.position.z, 
						block.size.x, block.size.y, block.size.z, 
						block.mass);
				wall.addComponent(block);
				game.ecs.addEntity(wall);
			} else {
				Settings.p("Ignoring line");
			}*/
			game.mapBuilderSystem.createAndAddEntityFromBlockData(block);
		}

	}


	public void saveFile() throws JsonIOException, IOException {
		if (mapdata == null) {
			mapdata = new MapData();
		}
		if (mapdata.blocks.size() == 0) {
			// Create dummy block
			MapBlockComponent block = new MapBlockComponent();
			//block.model_filename = "";
			//block.texture_filename = "";
			mapdata.blocks.add(block);
		}

		// Todo - backup old file
		IOFunctions.copyFileUsingStream(mapdata.filename, mapdata.filename + "_old");

		JsonWriter writer = new JsonWriter(new FileWriter(mapdata.filename));
		writer.setIndent("  ");
		Gson gson = new GsonBuilder().create();
		gson.toJson(mapdata, MapData.class, writer);
		writer.flush();
		writer.close();

	}
}
