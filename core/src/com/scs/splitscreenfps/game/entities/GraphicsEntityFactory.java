package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.HasDecal;
import com.scs.splitscreenfps.game.components.HasDecalCycle;
import com.scs.splitscreenfps.game.components.HasGuiSpriteComponent;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;

import ssmith.libgdx.GraphicsHelper;
import ssmith.libgdx.ModelFunctions;

public class GraphicsEntityFactory {

	private GraphicsEntityFactory() {
	}


	public static AbstractEntity createRedFilter(BasicECS ecs, int viewId) {
		AbstractEntity entity = new AbstractEntity(ecs, "RedFilter");

		Texture weaponTex = new Texture(Gdx.files.internal("colours/red.png"));		
		Sprite sprite = new Sprite(weaponTex);
		sprite.setColor(1, 0, 0, .5f);

		HasGuiSpriteComponent hgsc = new HasGuiSpriteComponent(sprite, HasGuiSpriteComponent.Z_FILTER, new Rectangle(0, 0, 1, 1));
		entity.addComponent(hgsc);
		hgsc.onlyViewId = viewId;
		
		return entity;	

	}


	public static AbstractEntity createWhiteFilter(BasicECS ecs, int viewId) {
		AbstractEntity entity = new AbstractEntity(ecs, "WhiteFilter");

		Texture weaponTex = new Texture(Gdx.files.internal("colours/white.png"));		
		Sprite sprite = new Sprite(weaponTex);
		sprite.setColor(.8f, .8f, .8f, .3f);

		HasGuiSpriteComponent hgsc = new HasGuiSpriteComponent(sprite, HasGuiSpriteComponent.Z_FILTER, new Rectangle(0, 0, 1, 1));
		entity.addComponent(hgsc);
		hgsc.onlyViewId = viewId;

		return entity;	

	}

/*
	public static AbstractEntity createModel(Game game, String name, String filename, float posX, float posY, float posZ, float height) {
		AbstractEntity entity = new AbstractEntity(game.ecs, name);

		PositionComponent posData = new PositionComponent(posX, posY, posZ);
		entity.addComponent(posData);

		ModelInstance instance = ModelFunctions.loadModel(filename, false);

		HasModelComponent hasModel = new HasModelComponent(instance);
		float scl = ModelFunctions.getScaleForHeight(instance, height);				
		instance.transform.scl(scl);
		ModelFunctions.getOrigin(instance, hasModel.offset);
		hasModel.offset.scl(-1f);
		hasModel.scale = scl;
		hasModel.always_draw = true;
		entity.addComponent(hasModel);

		return entity;
	}

*/
	public static AbstractEntity createNormalExplosion(BasicECS ecs, Vector3 pos, float width_height) {
		AbstractEntity entity = new AbstractEntity(ecs, "Explosion");

		// todo - the -.5f here .  Is that always the case?
		PositionComponent posData = new PositionComponent(pos.x, pos.y-.5f, pos.z);
		entity.addComponent(posData);

		TextureRegion[][] trs = GraphicsHelper.createSheet("Explosion21.png", 4, 4);

		HasDecal hasDecal = new HasDecal();
		TextureRegion tr = trs[0][0];
		hasDecal.decal = Decal.newDecal(tr, true);
		hasDecal.decal.setScale(width_height / tr.getRegionWidth());
		hasDecal.decal.setPosition(posData.position);
		hasDecal.faceCamera = true;
		hasDecal.dontLockYAxis = false;
		//hasDecal.decal.transformationOffset = new Vector2(0, pos.y/2);
		entity.addComponent(hasDecal);

		HasDecalCycle cycle = new HasDecalCycle(.02f, 4*4);
		cycle.remove_at_end_of_cycle = true;
		int idx = 0;
		for (int y=0 ; y<trs[0].length ; y++) {
			for (int x=0 ; x<trs.length ; x++) {
				cycle.decals[idx] = GraphicsHelper.DecalHelper(trs[x][y], width_height);
				idx++;
			}
		}
		entity.addComponent(cycle);

		return entity;	

	}


	public static AbstractEntity createBlueExplosion(BasicECS ecs, Vector3 pos) {
		AbstractEntity entity = new AbstractEntity(ecs, "BlueExplosion");

		PositionComponent posData = new PositionComponent(pos.x, pos.y, pos.z);
		entity.addComponent(posData);

		TextureRegion[][] trs = GraphicsHelper.createSheet("Effect95.png", 4, 4);

		HasDecal hasDecal = new HasDecal();
		TextureRegion tr = trs[0][0];
		hasDecal.decal = Decal.newDecal(tr, true);
		hasDecal.decal.setScale(1f / tr.getRegionWidth());
		hasDecal.decal.setPosition(posData.position);
		hasDecal.faceCamera = true;
		hasDecal.dontLockYAxis = false;
		entity.addComponent(hasDecal);

		HasDecalCycle cycle = new HasDecalCycle(.03f, 4*4);
		cycle.remove_at_end_of_cycle = true;
		int idx = 0;
		for (int y=0 ; y<trs[0].length ; y++) {
			for (int x=0 ; x<trs.length ; x++) {
				cycle.decals[idx] = GraphicsHelper.DecalHelper(trs[x][y], 1);
				idx++;
			}
		}
		entity.addComponent(cycle);

		return entity;	

	}

/*
	public static AbstractEntity createDebugSphere(BasicECS ecs, float x, float y, float z, float diam) {
		AbstractEntity e = new AbstractEntity(ecs, "LowWall");

		PositionComponent pos = new PositionComponent(x, y, z);
		pos.position = new Vector3(x, y, z);
		e.addComponent(pos);

		Material black_material = new Material(TextureAttribute.createDiffuse(new Texture("towerdefence/textures/ufo2_03.png")));

		ModelBuilder modelBuilder = new ModelBuilder();
		Model box_model = modelBuilder.createSphere(diam, diam, diam, 5, 5,  black_material, VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);

		ModelInstance instance = new ModelInstance(box_model, pos.position);
		//instance.transform.rotate(Vector3.Z, 90); // Rotates cube so textures are upright

		HasModelComponent model = new HasModelComponent("LowWall", instance);
		e.addComponent(model);

		return e;
	}
	*/
}
