package com.scs.splitscreenfps.game.systems;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.PlayersWeaponComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;

public class PositionPlayersWeaponSystem extends AbstractSystem {

	public PositionPlayersWeaponSystem(BasicECS ecs) {
		super(ecs, PlayersWeaponComponent.class);
	}
	
	
	public void processEntity(AbstractEntity entity) {
		PlayersWeaponComponent weapon = (PlayersWeaponComponent)entity.getComponent(PlayersWeaponComponent.class);
		//PlayerData playerData = (PlayerData)weapon.player.getComponent(PlayerData.class);
		PositionComponent playerPos = (PositionComponent)weapon.player.getComponent(PositionComponent.class);
		PositionComponent weaponPos = (PositionComponent)entity.getComponent(PositionComponent.class);
		weaponPos.position.set(playerPos.position);
		weaponPos.angle_degs = playerPos.angle_degs + 90;
		//weaponPos.position.set(3f, 3f, 3f);
		
		// todo
	}

}
