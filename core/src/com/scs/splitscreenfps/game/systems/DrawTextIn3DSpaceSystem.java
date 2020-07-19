package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.ViewportData;
import com.scs.splitscreenfps.game.components.DrawTextIn3DSpaceComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;

public class DrawTextIn3DSpaceSystem extends AbstractSystem {

	private Game game;
	private SpriteBatch batch2d;

	private Vector3 tmp = new Vector3();

	public DrawTextIn3DSpaceSystem(BasicECS ecs, Game _game, SpriteBatch _batch2d) {
		super(ecs, DrawTextIn3DSpaceComponent.class);

		game = _game;
		batch2d = _batch2d;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		DrawTextIn3DSpaceComponent data = (DrawTextIn3DSpaceComponent)entity.getComponent(DrawTextIn3DSpaceComponent.class);
		if (data.dontDrawInViewId == game.currentViewId) {
			return;
		}
		
		PositionComponent posData = (PositionComponent)entity.getComponent(PositionComponent.class);
		tmp.set(posData.position);
		tmp.add(data.offset);

		ViewportData viewport = game.viewports[game.currentViewId];
		Camera camera = viewport.camera;
		
		if (!camera.frustum.pointInFrustum(tmp)) {
			return;
		}

		float dist = tmp.dst(camera.position);
		if (data.range < 0 || dist <= data.range) {
			camera.project(tmp, viewport.viewRect.x, viewport.viewRect.y, viewport.viewRect.width, viewport.viewRect.height);
			//Settings.p("Pos: " + pos);
			BitmapFont font = game.font_med;
			font.setColor(new Color(.5f, .5f, .5f, 1f));
			font.draw(batch2d, data.text, tmp.x-20, tmp.y + 40);
		}
	}

}
