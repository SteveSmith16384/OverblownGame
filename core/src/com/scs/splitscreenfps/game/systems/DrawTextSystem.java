package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.DrawTextData;
import com.scs.splitscreenfps.game.systems.dependencies.IGetCurrentViewport;

public class DrawTextSystem extends AbstractSystem {

	private SpriteBatch batch2d;
	private IGetCurrentViewport getCurrent;

	public DrawTextSystem(BasicECS ecs, IGetCurrentViewport _getCurrent, SpriteBatch _batch2d) {
		super(ecs, DrawTextData.class);

		getCurrent = _getCurrent;
		batch2d = _batch2d;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		DrawTextData dtd = (DrawTextData)entity.getComponent(DrawTextData.class);
		if (dtd.drawOnViewId >= 0 && dtd.drawOnViewId != getCurrent.getCurrentViewportIdx()) {
			return;
		}

		BitmapFont font = dtd.font;
		
		/*if (dtd.centre_x && dtd.x < 0) { // todo - re-add and cache this
			GlyphLayout layout = new GlyphLayout(); //dont do this every frame! Store it as member
			layout.setText(font, dtd.text);
			float len = layout.width;// contains the width of the current set text
			dtd.x = Gdx.graphics.getWidth() / 2 - len/2;
		}*/

		float x = Gdx.graphics.getBackBufferWidth() * dtd.x_pcent / 100;
		float y = Gdx.graphics.getBackBufferHeight() * dtd.y_pcent / 100;
		font.setColor(0, 0, 0, 1);
		font.draw(batch2d, dtd.text, x+2, y);
		font.draw(batch2d, dtd.text, x-2, y);
		font.draw(batch2d, dtd.text, x, y+2);
		font.draw(batch2d, dtd.text, x, y-2);

		font.setColor(dtd.colour);
		font.draw(batch2d, dtd.text, x, y);
	}

}
