package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.DrawTextComponent;
import com.scs.splitscreenfps.game.components.RemoveEntityAfterTimeComponent;

public class TextEntity extends AbstractEntity {

	public TextEntity(BasicECS ecs, String text, float _xPcent, float _yPcent, float _duration_secs, Color col, int _viewId, BitmapFont font, boolean centre_x) {
		super(ecs, "Text");

		DrawTextComponent dtd = new DrawTextComponent(font, _viewId);
		dtd.text = text;
		dtd.x_pcent = _xPcent;
		dtd.y_pcent = _yPcent;
		dtd.centre_x = centre_x;
		dtd.colour = col;
		
		this.addComponent(dtd);
		
		if (_duration_secs > 0) {
			this.addComponent(new RemoveEntityAfterTimeComponent(_duration_secs));
		}
	}

}
