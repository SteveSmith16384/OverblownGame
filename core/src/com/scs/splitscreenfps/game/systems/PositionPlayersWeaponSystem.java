package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.PlayersWeaponComponent;

public class PositionPlayersWeaponSystem extends AbstractSystem {

	private Game game;

	private Vector3 tmpVector = new Vector3();
	//private Vector2 tmpVec2 = new Vector2();

	public PositionPlayersWeaponSystem(Game _game, BasicECS ecs) {
		super(ecs, PlayersWeaponComponent.class);

		game = _game;
	}

	@Override
	public void processEntity(AbstractEntity entity) {
		PlayersWeaponComponent weapon = (PlayersWeaponComponent)entity.getComponent(PlayersWeaponComponent.class);
		HasModelComponent model =(HasModelComponent)entity.getComponent(HasModelComponent.class);
		
		Matrix4 mat = new Matrix4();
		mat.set(weapon.camera.view);
		mat.mul(model.model.transform);
		
		model.model.transform.set(mat);
		
		mat.getTranslation(tmpVector);
		//Settings.p("Gun at " + tmpVector);
	}
}
