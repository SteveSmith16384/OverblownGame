package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.DrawTextData;
import com.scs.splitscreenfps.game.components.RemoveEntityAfterTimeComponent;

public class TextEntity extends AbstractEntity {

	public TextEntity(BasicECS ecs, String text, float _x, float _y, float _duration_secs, Color col, int _viewId, int size) {
		super(ecs, "Text");

		DrawTextData dtd = new DrawTextData(size);
		dtd.text = text;
		dtd.x = _x;
		dtd.y = _y;
		dtd.centre_x = _x < 0;
		dtd.colour = col;
		
		this.addComponent(dtd);
		
		if (_duration_secs > 0) {
			this.addComponent(new RemoveEntityAfterTimeComponent(_duration_secs));
		}
	}


	public TextEntity(BasicECS ecs, String text, float _y, float _duration_secs, Color col, int _viewId, int size) {
		this(ecs, text, -1, _y, _duration_secs, col, _viewId, size);
	}

}
