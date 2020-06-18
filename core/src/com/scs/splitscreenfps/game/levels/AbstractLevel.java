package com.scs.splitscreenfps.game.levels;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.EntityFactory;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.Wall;
import com.scs.splitscreenfps.game.mapdata.MapBlockComponent;
import com.scs.splitscreenfps.game.mapdata.MapData;

public abstract class AbstractLevel implements ILevelInterface {

	public Game game;
	protected List<Vector3> startPositions = new ArrayList<Vector3>();

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
		MapData object = gson.fromJson(new FileReader(filename), MapData.class);

		for (MapBlockComponent block : object.blocks) {
			if (block.filename.length() > 0) {
				AbstractEntity doorway = EntityFactory.Model(game.ecs, block.name, block.filename, 
						8, -2f, 7, 
						block.mass);
				game.ecs.addEntity(doorway);

			} else {
				Wall wall = new Wall(game.ecs, block.name, block.texture, block.posX, block.posY, block.posZ, 
						block.lenX, block.lenY, block.lenZ, 
						block.mass);
				game.ecs.addEntity(wall);
			}
		}

	}


	public void saveFilename(String filename, MapData mapdata) throws JsonIOException, IOException {
		Gson gson = new Gson();

		// Todo - backup old file

		// 1. Java object to JSON file
		gson.toJson(mapdata, new FileWriter(filename));

		// 2. Java object to JSON string
		//String json = gson.toJson(obj);
	}
}
