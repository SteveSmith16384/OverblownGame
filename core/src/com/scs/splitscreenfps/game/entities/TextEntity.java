package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.DrawTextData;

public class TextEntity extends AbstractEntity {

	public TextEntity(BasicECS ecs, String text, float _x, float _y, float _duration, Color col, int _viewId, int size) {
		super(ecs, "Text");

		DrawTextData dtd = new DrawTextData(size);
		dtd.text = text;
		dtd.x = _x;
		dtd.y = _y;
		dtd.timeRemaining = _duration;
		dtd.colour = col;

		this.addComponent(dtd);
	}


	public TextEntity(BasicECS ecs, String text, float _y, float _duration_secs, Color col, int _viewId, int size) {
		super(ecs, "Text");

		DrawTextData dtd = new DrawTextData(size);
		dtd.text = text;
		dtd.centre_x = true;
		dtd.x = -1;
		dtd.y = _y;
		dtd.timeRemaining = _duration_secs;
		dtd.colour = col;
		dtd.drawOnViewId = _viewId;

		this.addComponent(dtd);
	}

}
