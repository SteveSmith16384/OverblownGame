package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;

/**
 * This needs chaning so the texture is on the inside
 *
 */
public class SkyboxSphere extends AbstractEntity {

	// Positions are from the centre
	public SkyboxSphere(Game game, String name, String tex_filename, float diam) {
		super(game.ecs, name);

		//Texture tex = new Texture(tex_filename);
		Texture tex = game.getTexture("textures/sky.png");// new Texture("textures/sky.png");
		tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		Material black_material = new Material(TextureAttribute.createDiffuse(tex));
		ModelBuilder modelBuilder = new ModelBuilder();
		Model sphere_model = modelBuilder.createSphere(diam,  diam,  diam, 10, 10, black_material, VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);
		
		ModelInstance instance = new ModelInstance(sphere_model);
		
		HasModelComponent model = new HasModelComponent(instance, 1f, false);
		this.addComponent(model);

		this.addComponent(new PositionComponent());
	}

}
