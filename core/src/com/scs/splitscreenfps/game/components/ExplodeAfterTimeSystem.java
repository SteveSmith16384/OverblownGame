package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.Game;

public class ExplodeAfterTimeSystem extends AbstractSystem {

	private Game game;

	// Temp vars
	private Matrix4 mat = new Matrix4();
	private Vector3 vec = new Vector3();

	public ExplodeAfterTimeSystem(Game _game, BasicECS ecs) {
		super(ecs, ExplodeAfterTimeComponent.class);
		
		game = _game;
	}
	
	
	public void processEntity(AbstractEntity entity) {
		ExplodeAfterTimeComponent ex = (ExplodeAfterTimeComponent)entity.getComponent(ExplodeAfterTimeComponent.class);
		if (ex.explode_time < System.currentTimeMillis()) {
			entity.remove();
			PhysicsComponent phys = (PhysicsComponent)entity.getComponent(PhysicsComponent.class);
			phys.body.getWorldTransform(mat);
			game.explosion(mat.getTranslation(vec), 2, 4);
		}
	}
	
}
