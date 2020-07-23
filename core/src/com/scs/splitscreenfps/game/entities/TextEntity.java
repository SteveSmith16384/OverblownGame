package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.DrawTextData;
import com.scs.splitscreenfps.game.components.RemoveEntityAfterTimeComponent;

public class TextEntity extends AbstractEntity {

	public TextEntity(BasicECS ecs, String text, float _x, float _y, float _duration_secs, Color col, int _viewId, BitmapFont font) {
		super(ecs, "Text");

		DrawTextData dtd = new DrawTextData(font, _viewId);
		dtd.text = text;
		dtd.x_pcent = _x;
		dtd.y_pcent = _y;

		dtd.colour = col;
		
		this.addComponent(dtd);
		
		if (_duration_secs > 0) {
			this.addComponent(new RemoveEntityAfterTimeComponent(_duration_secs));
		}
	}

}
