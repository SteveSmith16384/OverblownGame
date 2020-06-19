package com.scs.splitscreenfps.game.mapdata;

import com.badlogic.gdx.math.Vector3;

public class MapBlockComponent {

	public String name = "";
	public String texture_filename = "";
	public String model_filename = "";
	public Vector3 size = new Vector3();
	public Vector3 position = new Vector3();
	public Vector3 rotation = new Vector3();
	public float mass = 1;
	
	public MapBlockComponent clone() {
		MapBlockComponent tmp = new MapBlockComponent();
		tmp.name = this.name + "_new";
		tmp.texture_filename = this.texture_filename;
		tmp.model_filename = this.model_filename;
		tmp.size = new Vector3(this.size);
		tmp.position = new Vector3(this.position);
		tmp.rotation = new Vector3(this.rotation);
		tmp.mass = this.mass;
		
		tmp.position.y += 5f;
		
		return tmp;
	}
}
