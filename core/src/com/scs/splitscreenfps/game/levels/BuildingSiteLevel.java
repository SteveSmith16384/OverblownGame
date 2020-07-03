package com.scs.splitscreenfps.game.levels;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.Wall;

public class BuildingSiteLevel extends AbstractLevel {

	private static final float FLOOR_SIZE = 15f; // todo - read from map data

	private HashMap<String, String> textures = new HashMap<String, String>();

	public BuildingSiteLevel(Game _game) {
		super(_game);
	}


	@Override
	public void setBackgroundColour() {
		Gdx.gl.glClearColor(0, .6f, .8f, 1);
	}


	@Override
	public void load() {
		this.startPositions.add(new Vector3(1, 2f, 1));
		this.startPositions.add(new Vector3(FLOOR_SIZE-2, 2f, FLOOR_SIZE-2));
		this.startPositions.add(new Vector3(1, 2f, FLOOR_SIZE-2));
		this.startPositions.add(new Vector3(FLOOR_SIZE-2, 2f, 1));

		/*
		Wall floor = new Wall(game.ecs, "Floor", "colours/white.png", FLOOR_SIZE/2, -0.1f, FLOOR_SIZE/2, 
				FLOOR_SIZE, .2f, FLOOR_SIZE, 
				0f, true, false);
		game.ecs.addEntity(floor);

		AbstractEntity crate = EntityFactory.createCrate(game.ecs, "colours/red.png", 3, 3, 3, 1, 1, 1);
		game.ecs.addEntity(crate);
		 */
		FileHandle file = Gdx.files.local("maps/building_site.csv");
		String csv = file.readString();
		String rows[] = csv.split("\n");
		for (int row=0 ; row<rows.length ; row++) {
			decodeRow(rows[row].toLowerCase(), rows, row);
		}
	}


	private void decodeRow(String data, String rows[], int current_row) {
		if (data.startsWith("\t")) { // Blank row
			// DO nothing
		} else if (data.startsWith("texture")) {
			String[] tokens = data.split(":");
			this.textures.put(tokens[1], tokens[2]);
		} else if (data.startsWith("map")) {
			this.decodeMap(rows, current_row);
		} else {
			throw new RuntimeException("Todo");
		}
	}


	private void decodeMap(String rows[], int start_row) {
		for (int row=start_row+1 ; row<rows.length ; row++) {
			String cols[] = rows[row].split("\t");
			for (int col=0 ; col<cols.length ; col++) {
				//String tokens[] = cols[col].split("~");
				//for (int t=0 ; t<tokens.length ; t++) {
					if (cols[col].trim().length() > 0) {
						decodeMapItem(cols[col].trim(), col, row);
					}
				//}
			}
		}		
	}


	private void decodeMapItem(String data, int col, int row) {
		String items[] = data.split("~");
		for (int i=0 ; i<items.length ; i++) {
			if (items[i].equalsIgnoreCase("Cube")) {
				parseCube(items, col, row);
			} else {
				throw new RuntimeException("Unknown code: " + items[i]);
			}
		}
	}


	private void parseCube(String[] items, int col, int row) {
		Vector3 pos = new Vector3(col, 0, row);
		Vector3 size = new Vector3();
		String tex = null;
		float mass = 0;
		
		//String items[] = data.split("~");
		for (int i=1 ; i<items.length ; i++) {
			String[] split = items[i].split(":");
			if (split[0].equalsIgnoreCase("w")) {
				size.x = Float.parseFloat(split[1]);
			} else if (split[0].equalsIgnoreCase("h")) {
				size.y = Float.parseFloat(split[1]);
			} else if (split[0].equalsIgnoreCase("d")) {
				size.z = Float.parseFloat(split[1]);
			} else if (split[0].equalsIgnoreCase("mass")) {
				mass = Float.parseFloat(split[1]);
			} else if (split[0].equalsIgnoreCase("tex")) {
				tex = this.textures.get(split[1]);
			} else {
				throw new RuntimeException("Unknown code: " + items[i]);
			}
		}

		AbstractEntity entity = new Wall(game.ecs, "Name", tex, pos.x, pos.y, pos.z, size.x, size.y, size.z, mass, 0, 0, 0, false, true);
		game.ecs.addEntity(entity);
	}


	@Override
	public void update() {
	}


	@Override
	public void renderUI(SpriteBatch batch2d, int currentViewId) {

	}



}
