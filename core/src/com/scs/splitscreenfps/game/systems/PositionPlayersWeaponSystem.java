package com.scs.splitscreenfps.game.systems;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.PlayersWeaponComponent;

public class PositionPlayersWeaponSystem extends AbstractSystem {

	private Game game;
	
	public PositionPlayersWeaponSystem(int do_not_use, Game _game, BasicECS ecs) {
		super(ecs, PlayersWeaponComponent.class);
		
		game = _game;
	}
	
	
	public void processEntity(AbstractEntity entity) {
/*		PlayersWeaponComponent weapon = (PlayersWeaponComponent)entity.getComponent(PlayersWeaponComponent.class);
		PlayerData playerData = (PlayerData)weapon.player.getComponent(PlayerData.class);
		PositionComponent playerPos = (PositionComponent)weapon.player.getComponent(PositionComponent.class);
		PositionComponent weaponPos = (PositionComponent)entity.getComponent(PositionComponent.class);
		HasModelComponent model = (HasModelComponent)entity.getComponent(HasModelComponent.class);
		
		Camera cam = game.viewports[playerData.playerIdx].camera;
		model.angleOffset
		weaponPos.position.set(playerPos.position);
		weaponPos.angle_Y_degs = playerPos.angle_Y_degs + 90;
		//weaponPos.position.set(3f, 3f, 3f);
	*/	
	}

}
