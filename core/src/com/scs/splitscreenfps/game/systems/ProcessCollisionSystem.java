package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.EventCollision;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.ExplodeOnContactComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;

public class ProcessCollisionSystem {

	private Game game;
	
	private Matrix4 mat = new Matrix4();
	private Vector3 vec = new Vector3();
	
	public ProcessCollisionSystem(Game _game) {
		game = _game;
	}
	
	
	public void processCollision(AbstractEntity e1, AbstractEntity e2) {
		checkExplosion(e1, e2);
		checkExplosion(e2, e1);
		
		game.ecs.events.add(new EventCollision(e1, e2));
		
	}
	
	
	private void checkExplosion(AbstractEntity rocket, AbstractEntity hit) {
		ExplodeOnContactComponent explodes = (ExplodeOnContactComponent)rocket.getComponent(ExplodeOnContactComponent.class);
		if (explodes != null) {
			//Settings.p("Rocket hit " + hit);
			PhysicsComponent phys = (PhysicsComponent)rocket.getComponent(PhysicsComponent.class);
			phys.body.getWorldTransform(mat);
			game.explosion(mat.getTranslation(vec), 2, 4, 4);
		}

		
	}
}
