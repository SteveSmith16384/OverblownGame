package com.scs.splitscreenfps.game.levels;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.Wall;

public class LoadCSVLevel extends AbstractLevel {

	private HashMap<String, String> textures = new HashMap<String, String>();
	private String filename;
	
	public LoadCSVLevel(Game _game, String _filename) {
		super(_game);
		
		filename = _filename;
	}


	@Override
	public void setBackgroundColour() {
		Gdx.gl.glClearColor(0, .6f, .8f, 1);
		//Gdx.gl.glClearColor(0, 0f, 0f, 1);
	}


	@Override
	public void load() {
		FileHandle file = Gdx.files.local(filename);//"maps/building_site.csv");
		String csv = file.readString();
		String rows[] = csv.split("\n");
		for (int row=0 ; row<rows.length ; row++) {
			boolean end = decodeRow(rows[row].toLowerCase(), rows, row);
			if (end) {
				break;
			}
		}
	}


	private boolean decodeRow(String data, String rows[], int current_row) {
		if (data.startsWith("\t")) { // Blank row
			// DO nothing
		} else if (data.startsWith("rem:")) {
		} else if (data.startsWith("texture:")) {
			String[] tokens = data.trim().split(":");
			this.textures.put(tokens[1], tokens[2]);
		} else if (data.startsWith("map")) {
			this.decodeMap(rows, current_row+1);
			return true; // Map is last
		} else {
			throw new RuntimeException("Unknown data: " + data);
		}
		return false;
	}


	private void decodeMap(String rows[], int start_row) {
		for (int row=start_row ; row<rows.length ; row++) {
			String cols[] = rows[row].split("\t");
			for (int col=0 ; col<cols.length ; col++) {
				if (cols[col].trim().length() > 0) {
					decodeMapItem(cols[col].trim(), col, row-start_row);
				}
			}
		}		
	}


	private void decodeMapItem(String data, int col, int row) {
		String items[] = data.trim().split("~");
		if (items[0].equalsIgnoreCase("Cube")) {
			parseCube(items, col, row);
		} else if (items[0].equalsIgnoreCase("start")) {
			this.startPositions.add(new Vector3(col, 1f, row));
		} else {
			throw new RuntimeException("Unknown code: " + items[0]);
		}
	}


	private void parseCube(String[] items, int col, int row) {
		boolean got_pos = false;
		Vector3 pos = new Vector3(col, 0, row);
		Vector3 size = new Vector3();
		String tex = null;
		float mass = 0;
		String name = "Todo";

		for (int i=1 ; i<items.length ; i++) {
			String[] split = items[i].split(":");
			if (split[0].equalsIgnoreCase("name")) {
				name = split[1];
			} else if (split[0].equalsIgnoreCase("x")) {
				pos.x = Float.parseFloat(split[1]);
				got_pos = true;
			} else if (split[0].equalsIgnoreCase("y")) {
				pos.y = Float.parseFloat(split[1]);
				got_pos = true;
			} else if (split[0].equalsIgnoreCase("z")) {
				pos.z = Float.parseFloat(split[1]);
				got_pos = true;
			} else if (split[0].equalsIgnoreCase("w")) {
				size.x = Float.parseFloat(split[1]);
			} else if (split[0].equalsIgnoreCase("h")) {
				size.y = Float.parseFloat(split[1]);
			} else if (split[0].equalsIgnoreCase("d")) {
				size.z = Float.parseFloat(split[1]);
			} else if (split[0].equalsIgnoreCase("s")) {
				size.x = Float.parseFloat(split[1]);
				size.y = Float.parseFloat(split[1]);
				size.z = Float.parseFloat(split[1]);
			} else if (split[0].equalsIgnoreCase("mass")) {
				mass = Float.parseFloat(split[1]);
			} else if (split[0].equalsIgnoreCase("tex")) {
				tex = this.textures.get(split[1]);
			} else {
				throw new RuntimeException("Unknown code: " + items[i]);
			}
		}

		if (got_pos == false) {
			pos.y = size.y/2;
		}
		if (tex == null) {
			tex = "colours/white.png";
		}

		Settings.p("Creating " + name + " at " + pos);
		AbstractEntity entity = new Wall(game.ecs, name, tex, pos.x, pos.y, pos.z, size.x, size.y, size.z, mass, 0, 0, 0, false, true);
		game.ecs.addEntity(entity);
	}


	@Override
	public void update() {
	}


	@Override
	public void renderUI(SpriteBatch batch2d, int currentViewId) {

	}



}
