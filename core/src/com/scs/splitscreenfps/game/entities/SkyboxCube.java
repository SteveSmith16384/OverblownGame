package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;

public class SkyboxCube extends AbstractEntity {

	// Positions are from the centre
	public SkyboxCube(BasicECS ecs, String name, String tex_filename, float w, float h, float d) {
		super(ecs, name);

		//Texture tex = new Texture(tex_filename);
		Texture tex = new Texture("textures/sky3.jpg");
		tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		Material black_material = new Material(TextureAttribute.createDiffuse(tex));
		ModelBuilder modelBuilder = new ModelBuilder();

		int attr = VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates;
		modelBuilder.begin();
		modelBuilder.part("front", GL20.GL_TRIANGLES, attr, black_material)
		    .rect(-w/2,-h/2,d/2, -w/2,h/2,d/2,  w/2,h/2,d/2, w/2,-h/2,d/2, 0,0,1);
		modelBuilder.part("back", GL20.GL_TRIANGLES, attr, black_material)
		    .rect(-w/2,h/2,-d/2, -w/2,-h/2,-d/2,  w/2,-h/2,-d/2, w/2,h/2,-d/2, 0,0,-1);
		
		modelBuilder.part("bottom", GL20.GL_TRIANGLES, attr, black_material)
		    .rect(-w/2,h/2,d/2, -w/2,h/2,-d/2,  w/2,h/2,-d/2, w/2,h/2,d/2, 0,1,0);
		modelBuilder.part("top", GL20.GL_TRIANGLES, attr, black_material)
		    .rect(-w/2,-h/2,-d/2, -w/2,-h/2,d/2,  w/2,-h/2,d/2, w/2,-h/2,-d/2, 0,-1,0);
		
		modelBuilder.part("left", GL20.GL_TRIANGLES, attr, black_material)
		    .rect(w/2,-h/2,d/2, w/2,h/2,d/2, w/2,h/2,-d/2, w/2,-h/2,-d/2, 1,0,0);
		modelBuilder.part("right", GL20.GL_TRIANGLES, attr, black_material)
		    .rect(-w/2,-h/2,-d/2, -w/2,h/2,-d/2, -w/2,h/2,d/2, -w/2,-h/2,d/2, -1,0,0);

		Model box_model = modelBuilder.end();
		
		/*if (tile) {
			Matrix3 mat = new Matrix3();
			float max2 = Math.max(w, h);
			float max = Math.max(max2, d);
			mat.scl(max);//new Vector2(h, d));//, h));
			box_model.meshes.get(0).transformUV(mat);
		}*/
		
		ModelInstance instance = new ModelInstance(box_model);
		
		HasModelComponent model = new HasModelComponent(instance, 1f, false);
		this.addComponent(model);

		this.addComponent(new PositionComponent());
	}

}
