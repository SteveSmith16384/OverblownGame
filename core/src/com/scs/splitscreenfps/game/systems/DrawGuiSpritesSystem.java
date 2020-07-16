package com.scs.splitscreenfps.game.systems;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.ViewportData;
import com.scs.splitscreenfps.game.components.HasGuiSpriteComponent;

public class DrawGuiSpritesSystem extends AbstractSystem implements Comparator<AbstractEntity> {

	private Game game;
	private SpriteBatch batch2d;

	public DrawGuiSpritesSystem(BasicECS ecs, Game _game, SpriteBatch _batch2d) {
		super(ecs, HasGuiSpriteComponent.class);
		game = _game;
		batch2d = _batch2d;
	}


	@Override
	public void addEntity(AbstractEntity e) {
		super.addEntity(e);
		Collections.sort(this.entities, this);
	}

	
	@Override
	public void processEntity(AbstractEntity entity) {
		HasGuiSpriteComponent hgsc = (HasGuiSpriteComponent)entity.getComponent(HasGuiSpriteComponent.class);
		if (hgsc.onlyViewId >= 0) {
			if (hgsc.onlyViewId != game.currentViewId) {
				return;
			}
		}
		if (hgsc.dirty) {
			Sprite sprite = hgsc.sprite;
			//sprite.setBounds(hgsc.scale.x * (Gdx.graphics.getWidth()), hgsc.scale.y * (Gdx.graphics.getHeight()), hgsc.scale.width * (Gdx.graphics.getWidth()), hgsc.scale.width * (Gdx.graphics.getHeight()));

			ViewportData v = game.viewports[game.currentViewId];
			float x = v.viewRect.x + (hgsc.scale.x * v.viewRect.width);
			float y = v.viewRect.y + (hgsc.scale.y * v.viewRect.height);
			//float w = v.viewRect.x + (hgsc.scale.width * v.viewRect.width);
			//float h = v.viewRect.y + (hgsc.scale.height * v.viewRect.height);
			float w = (hgsc.scale.width * v.viewRect.width);
			float h = w;
			if (hgsc.proportional == false) {
				h = (hgsc.scale.height * v.viewRect.height);
			}
			sprite.setBounds(x, y, w, h);
			//sprite.setBounds(hgsc.scale.x * v.viewPos.width, hgsc.scale.y * v.viewPos.height, hgsc.scale.width * v.viewPos.width, hgsc.scale.height * v.viewPos.height);
			hgsc.dirty = false;
		}
		//hgsc.sprite.rotate(1);
		hgsc.sprite.draw(batch2d);
	}


	@Override
	public int compare(AbstractEntity arg0, AbstractEntity arg1) {
		HasGuiSpriteComponent im0 = (HasGuiSpriteComponent)arg0.getComponent(HasGuiSpriteComponent.class);
		HasGuiSpriteComponent im1 = (HasGuiSpriteComponent)arg1.getComponent(HasGuiSpriteComponent.class);
		return im0.zOrder - im1.zOrder;
	}

	
	public void rescaleSprites() {
		Iterator<AbstractEntity> it = this.entities.iterator();
		while (it.hasNext()) {
			AbstractEntity entity = it.next();
			HasGuiSpriteComponent hgsc = (HasGuiSpriteComponent)entity.getComponent(HasGuiSpriteComponent.class);
			hgsc.dirty = true;
		}

	}
}
