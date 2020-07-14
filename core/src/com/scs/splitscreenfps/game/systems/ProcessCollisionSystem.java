package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.ExplodeOnContactComponent;
import com.scs.splitscreenfps.game.components.IsBulletComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.events.EventCollision;

public class ProcessCollisionSystem {

	private Game game;

	private Matrix4 mat = new Matrix4();
	private Vector3 vec = new Vector3();

	public ProcessCollisionSystem(Game _game) {
		game = _game;
	}


	public void processCollision(AbstractEntity e1, AbstractEntity e2, float force) {
		checkForExplosion(e1, e2);
		checkForExplosion(e2, e1);

		game.ecs.events.add(new EventCollision(e1, e2, force));

	}


	private void checkForExplosion(AbstractEntity rocket, AbstractEntity hit) {
		ExplodeOnContactComponent explodes = (ExplodeOnContactComponent)rocket.getComponent(ExplodeOnContactComponent.class);
		if (explodes != null) {
			IsBulletComponent bullet = (IsBulletComponent)rocket.getComponent(IsBulletComponent.class);
			if (bullet != null && hit == bullet.shooter) {
				return;
			}
			//Settings.p("Rocket hit " + hit);
			PhysicsComponent phys = (PhysicsComponent)rocket.getComponent(PhysicsComponent.class);
			phys.body.getWorldTransform(mat);
			//game.explosion(mat.getTranslation(vec), bullet.settings.expl_range, bullet.settings.expl_force, 4);
			rocket.remove();
			game.explosion(mat.getTranslation(vec), explodes.explData, explodes.shooter);
		}


	}
}
