package com.scs.splitscreenfps.game.systems;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.DrawTextComponent;
import com.scs.splitscreenfps.game.components.HasGuiSpriteComponent;
import com.scs.splitscreenfps.game.systems.dependencies.IGetCurrentViewport;

public class DrawTextSystem extends AbstractSystem {

	private SpriteBatch spriteBatch;
	private IGetCurrentViewport getCurrent;

	public DrawTextSystem(BasicECS ecs, IGetCurrentViewport _getCurrent, SpriteBatch _batch2d) {
		super(ecs, DrawTextComponent.class);

		getCurrent = _getCurrent;
		spriteBatch = _batch2d;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		DrawTextComponent dtd = (DrawTextComponent)entity.getComponent(DrawTextComponent.class);
		if (dtd.drawOnViewId >= 0 && dtd.drawOnViewId != getCurrent.getCurrentViewportIdx()) {
			return;
		}

		BitmapFont font = dtd.font;
		
		if (dtd.dirty && dtd.centre_x) {
			GlyphLayout layout = new GlyphLayout(); //dont do this every frame! Store it as member
			layout.setText(font, dtd.text);
			float len = layout.width;// contains the width of the current set text
			dtd.x_pcent = (Gdx.graphics.getWidth() / 2 - len/2) / Gdx.graphics.getWidth();// (1-(len / Gdx.graphics.getWidth())) * 100;//;
			dtd.x_pcent *= 100;
			dtd.dirty = false;
		}

		float x = Gdx.graphics.getBackBufferWidth() * dtd.x_pcent / 100;
		float y = Gdx.graphics.getBackBufferHeight() * dtd.y_pcent / 100;
		font.setColor(0, 0, 0, 1);
		font.draw(spriteBatch, dtd.text, x+2, y);
		font.draw(spriteBatch, dtd.text, x-2, y);
		font.draw(spriteBatch, dtd.text, x, y+2);
		font.draw(spriteBatch, dtd.text, x, y-2);

		font.setColor(dtd.colour);
		font.draw(spriteBatch, dtd.text, x, y);
	}


	public void rescaleText() {
		Iterator<AbstractEntity> it = this.entities.iterator();
		while (it.hasNext()) {
			AbstractEntity entity = it.next();
			DrawTextComponent hgsc = (DrawTextComponent)entity.getComponent(DrawTextComponent.class);
			hgsc.dirty = true;
		}

	}
}
