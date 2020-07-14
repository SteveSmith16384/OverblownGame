package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.DrawTextData;

public class DrawTextSystem extends AbstractSystem {

	private Game game;
	private SpriteBatch batch2d;

	public DrawTextSystem(BasicECS ecs, Game _game, SpriteBatch _batch2d) {
		super(ecs, DrawTextData.class);

		game = _game;
		batch2d = _batch2d;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		DrawTextData dtd = (DrawTextData)entity.getComponent(DrawTextData.class);
		if (dtd.drawOnViewId >= 0 && dtd.drawOnViewId != game.currentViewId) {
			return;
		}

		BitmapFont font = null;
		switch (dtd.size) {
		case 1:
			font = game.font_large;
			break;
		case 2:
			font = game.font_med;
			break;
		case 3:
			font = game.font_small;
			break;
		default:
			throw new RuntimeException("Unknown font size: " + dtd.size);
		}
		/*if (dtd.centre_x && dtd.x < 0) { // todo - re-add and cache this
			GlyphLayout layout = new GlyphLayout(); //dont do this every frame! Store it as member
			layout.setText(font, dtd.text);
			float len = layout.width;// contains the width of the current set text
			dtd.x = Gdx.graphics.getWidth() / 2 - len/2;
		}*/

		font.setColor(dtd.colour);
		float x = Gdx.graphics.getBackBufferWidth() * dtd.x_pcent / 100;
		float y = Gdx.graphics.getBackBufferHeight() * dtd.y_pcent / 100;
		font.draw(batch2d, dtd.text, x, y);
	}

}
