package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.ITextureProvider;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.DrawTextIn3DSpaceComponent;
import com.scs.splitscreenfps.game.components.HasDecal;
import com.scs.splitscreenfps.game.components.HasDecalCycle;
import com.scs.splitscreenfps.game.components.HasGuiSpriteComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.components.RemoveEntityAfterTimeComponent;

import ssmith.libgdx.GraphicsHelper;

public class GraphicsEntityFactory {

	private GraphicsEntityFactory() {
	}


	public static AbstractEntity createRedFilter(BasicECS ecs, ITextureProvider texProv, int viewId) {
		AbstractEntity entity = new AbstractEntity(ecs, "RedFilter");

		Texture weaponTex = texProv.getTexture("blood.png");
		//Texture weaponTex = new Texture(Gdx.files.internal("colours/red.png"));		
		Sprite sprite = new Sprite(weaponTex);
		//sprite.setColor(1, 0, 0, 1);

		HasGuiSpriteComponent hgsc = new HasGuiSpriteComponent(sprite, HasGuiSpriteComponent.Z_FILTER, new Rectangle(0, 0, 1, 1), false);
		entity.addComponent(hgsc);
		hgsc.onlyViewId = viewId;
		
		return entity;	

	}


	public static AbstractEntity createWhiteFilter(Game game, int viewId) {
		AbstractEntity entity = new AbstractEntity(game.ecs, "WhiteFilter");

		//Texture weaponTex = new Texture(Gdx.files.internal("colours/white.png"));		
		Texture weaponTex = game.getTexture("colours/white.png");
		Sprite sprite = new Sprite(weaponTex);
		sprite.setColor(.8f, .8f, .8f, .3f);

		HasGuiSpriteComponent hgsc = new HasGuiSpriteComponent(sprite, HasGuiSpriteComponent.Z_FILTER, new Rectangle(0, 0, 1, 1), false);
		entity.addComponent(hgsc);
		hgsc.onlyViewId = viewId;

		return entity;	

	}


	public static AbstractEntity createNormalExplosion(Game game, Vector3 pos, float width_height) {
		AbstractEntity entity = new AbstractEntity(game.ecs, "Explosion");

		//PositionComponent posData = new PositionComponent(pos.x, pos.y-(width_height/2), pos.z);
		PositionComponent posData = new PositionComponent(pos.x, pos.y, pos.z);
		entity.addComponent(posData);

		Texture tex = game.getTexture("Explosion21.png");
		TextureRegion[][] trs = GraphicsHelper.createSheet(tex, 4, 4);

		HasDecal hasDecal = new HasDecal();
		TextureRegion tr = trs[0][0];
		hasDecal.decal = Decal.newDecal(tr, true);
		hasDecal.decal.setScale(width_height / tr.getRegionWidth());
		hasDecal.decal.setPosition(posData.position);
		hasDecal.faceCamera = true;
		hasDecal.dontLockYAxis = true;
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


	public static AbstractEntity createBlueExplosion(Game game, Vector3 pos) {
		AbstractEntity entity = new AbstractEntity(game.ecs, "BlueExplosion");

		PositionComponent posData = new PositionComponent(pos.x, pos.y, pos.z);
		entity.addComponent(posData);

		Texture tex = game.getTexture("Effect95.png");
		TextureRegion[][] trs = GraphicsHelper.createSheet(tex, 4, 4);

		HasDecal hasDecal = new HasDecal();
		TextureRegion tr = trs[0][0];
		hasDecal.decal = Decal.newDecal(tr, true);
		hasDecal.decal.setScale(1f / tr.getRegionWidth());
		hasDecal.decal.setPosition(posData.position);
		hasDecal.faceCamera = true;
		hasDecal.dontLockYAxis = true;
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


	public static AbstractEntity createCrosshairs(BasicECS ecs, ITextureProvider texProv, int viewId) {
		AbstractEntity entity = new AbstractEntity(ecs, "Crosshairs");

		Texture weaponTex = texProv.getTexture("crosshairs2.png");
		Sprite sprite = new Sprite(weaponTex);

		float y = .4f;
		if (Settings.USE_MAP_EDITOR) {
			y = 0.45f;
		}
		HasGuiSpriteComponent hgsc = new HasGuiSpriteComponent(sprite, HasGuiSpriteComponent.Z_FILTER, new Rectangle(0.46f, y, .08f, .08f), true);
		entity.addComponent(hgsc);
		hgsc.onlyViewId = viewId;
		
		return entity;	

	}


	public static AbstractEntity createRisingText(BasicECS ecs, int viewId, Vector3 pos, String text, Color colour) {
		AbstractEntity entity = new AbstractEntity(ecs, "RisingHealth");

		entity.addComponent(new RemoveEntityAfterTimeComponent(1));

		DrawTextIn3DSpaceComponent dti3d = new DrawTextIn3DSpaceComponent(text, -1f, viewId, true, colour);
		entity.addComponent(dti3d);

		PositionComponent posData = new PositionComponent(pos.x, pos.y, pos.z);
		entity.addComponent(posData);

		return entity;	

	}


}
