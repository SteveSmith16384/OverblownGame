package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.EventCollision;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.ExplodeOnContactComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.RemoveOnContactComponent;

public class ProcessCollisionSystem {

	private Game game;
	
	private Matrix4 mat = new Matrix4();
	private Vector3 vec = new Vector3();
	
	public ProcessCollisionSystem(Game _game) {
		game = _game;
	}
	
	
	public void processCollision(AbstractEntity e1, AbstractEntity e2) {
		if (e1.getComponent(RemoveOnContactComponent.class) != null) {
			e1.remove();
		}
		if (e2.getComponent(RemoveOnContactComponent.class) != null) {
			e2.remove();
		}
		
		checkExplosion(e1);
		checkExplosion(e2);
		
		game.ecs.events.add(new EventCollision(e1, e2));
		
	}
	
	
	private void checkExplosion(AbstractEntity entity) {
		ExplodeOnContactComponent explodes = (ExplodeOnContactComponent)entity.getComponent(ExplodeOnContactComponent.class);
		if (explodes != null) {
			PhysicsComponent phys = (PhysicsComponent)entity.getComponent(PhysicsComponent.class);
			phys.body.getWorldTransform(mat);
			game.explosion(mat.getTranslation(vec), 2, 4);
		}

		
	}
}
