package com.scs.splitscreenfps.game.mapdata;

import com.badlogic.gdx.math.Vector3;

public class MapBlockComponent {

	public static int next_id = 1;

	public int id;
	public String type = "";
	public String name = "";
	public int texture_id;
	public String model_filename = "";
	public boolean tiled = true;
	
	public Vector3 size = new Vector3();
	public Vector3 position = new Vector3();
	public Vector3 rotation = new Vector3();
	
	public float mass = 0;

	public MapBlockComponent() {
		id = next_id++;
	}
	

	public MapBlockComponent clone() {
		MapBlockComponent tmp = new MapBlockComponent();
		tmp.type = this.type;
		tmp.name = this.name + "_new";
		tmp.texture_id = this.texture_id;
		tmp.model_filename = this.model_filename;
		tmp.size = new Vector3(this.size);
		tmp.position = new Vector3(this.position);
		tmp.rotation = new Vector3(this.rotation);
		tmp.mass = this.mass;
		tmp.tiled = this.tiled;

		tmp.position.y += tmp.size.y;

		return tmp;
	}
}
