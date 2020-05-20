package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.HasDecal;
import com.scs.splitscreenfps.game.components.PositionComponent;

public class GenericWallScenery extends AbstractEntity {
	
	public enum Side {Left, Right, Front, Back}
	
	private static final float TINY = 0.01f;

	public GenericWallScenery(BasicECS ecs, String name, String filename, int x, int y, Side side) {
		super(ecs, name);
		
        PositionComponent pos = new PositionComponent(x, 0, y);
        this.addComponent(pos);
        
        switch (side) {
        case Left:
        	pos.position.z -= 0.5f - TINY;
        	break;
        case Right:
        	pos.position.z += 0.5f + TINY;
        	break;
        case Front:
        	pos.position.x -= 0.5f - TINY;
        	break;
        case Back:
        	pos.position.x += 0.5f + TINY;
        	break;
        }
        
		HasDecal hasDecal = new HasDecal();
		Texture tex = new Texture(Gdx.files.internal(filename));
		TextureRegion tr = new TextureRegion(tex, 0, 0, tex.getWidth(), tex.getHeight());
        hasDecal.decal = Decal.newDecal(tr, true);
        hasDecal.decal.setScale(1f / tr.getRegionWidth());
        hasDecal.decal.setPosition(pos.position);
        hasDecal.faceCamera = false;
        if (side == Side.Front || side == Side.Back) {
        	hasDecal.rotation = 90f;
        }
        this.addComponent(hasDecal);
	}

}
