package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.MoveInDirectionComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;

public class MoveInDirectionSystem extends AbstractSystem { // todo - delete this

	private Game game;
	private Matrix4 tmpMat = new Matrix4();
	private Vector3 tmpVec = new Vector3();

	public MoveInDirectionSystem(Game _game, BasicECS ecs) {
		super(ecs, MoveInDirectionComponent.class);

		game = _game;
	}


	public void processEntity(AbstractEntity entity) {
		MoveInDirectionComponent move = (MoveInDirectionComponent)entity.getComponent(MoveInDirectionComponent.class);
		
		PhysicsComponent physics = (PhysicsComponent)entity.getComponent(PhysicsComponent.class);
		if (physics != null) {
			physics.body.getWorldTransform(tmpMat);
			tmpMat.getTranslation(tmpVec);
			tmpVec.mulAdd(move.offset, Gdx.graphics.getDeltaTime());
			tmpMat.setTranslation(tmpVec);
			physics.body.setWorldTransform(tmpMat);
		} else {
			PositionComponent posData = (PositionComponent)entity.getComponent(PositionComponent.class);
			posData.position.mulAdd(move.offset, Gdx.graphics.getDeltaTime());
		}
	}
	
	
}