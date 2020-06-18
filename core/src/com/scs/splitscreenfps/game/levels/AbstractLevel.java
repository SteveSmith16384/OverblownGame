package com.scs.splitscreenfps.game.levels;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.EntityFactory;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.Wall;
import com.scs.splitscreenfps.game.mapdata.MapBlockComponent;
import com.scs.splitscreenfps.game.mapdata.MapData;

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

		for (MapBlockComponent block : mapdata.blocks) {
			if (block.filename.length() > 0) {
				AbstractEntity doorway = EntityFactory.Model(game.ecs, block.name, block.filename, 
						8, -2f, 7, 
						block.mass);
				doorway.addComponent(block);
				game.ecs.addEntity(doorway);
			} else if (block.texture.length() > 0) {
				Wall wall = new Wall(game.ecs, block.name, block.texture, block.posX, block.posY, block.posZ, 
						block.lenX, block.lenY, block.lenZ, 
						block.mass);
				wall.addComponent(block);
				game.ecs.addEntity(wall);
			} else {
				Settings.p("Ignoring line");
			}
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

		// Todo - backup old file

		Writer writer = new FileWriter(mapdata.filename);
		Gson gson = new GsonBuilder().create();
		gson.toJson(mapdata, writer);
		writer.flush();
		writer.close();

	}
}
