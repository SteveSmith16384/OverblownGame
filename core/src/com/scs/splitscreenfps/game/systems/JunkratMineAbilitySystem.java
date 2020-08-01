package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.Gdx;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.JunkratMineAbilityComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.data.ExplosionData;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.entities.BulletEntityFactory;

public class JunkratMineAbilitySystem extends AbstractSystem {

	private Game game;

	public JunkratMineAbilitySystem(BasicECS ecs, Game _game) {
		super(ecs, JunkratMineAbilityComponent.class);

		game = _game;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		JunkratMineAbilityComponent ability = (JunkratMineAbilityComponent)entity.getComponent(JunkratMineAbilityComponent.class);

		if (ability.count_available < ability.max_count) {
			ability.current_cooldown += Gdx.graphics.getDeltaTime();
			if (ability.current_cooldown >= ability.cooldown_duration) {
				BillBoardFPS_Main.audio.play("sfx/teleport.mp3");
				ability.current_cooldown = 0;
				ability.count_available++;
				if (ability.count_available > ability.max_count) {
					ability.count_available = ability.max_count;
				}
			}
		}


		AbstractPlayersAvatar player = (AbstractPlayersAvatar)entity;
		PlayerData playerData = (PlayerData)entity.getComponent(PlayerData.class);
		if (ability.count_available > 0 || ability.entity != null) {
			playerData.ability1Ready = true;
			playerData.ability1text ="Concussion Mine Ready! (" + ability.count_available + ") ";
			if (player.inputMethod.isAbility1Pressed()) {
				if (ability.button_released) {
					ability.button_released = false;
					if (ability.entity == null || ability.entity.isMarkedForRemoval()) {
						//Settings.p("Launching mine");
						ability.entity = BulletEntityFactory.createJunkratMine(game, player);
						game.ecs.addEntity(ability.entity);
						ability.count_available--;
					} else {
						//Settings.p("Exploding current mine");
						// Explode current bomb
						PositionComponent posData = (PositionComponent)ability.entity.getComponent(PositionComponent.class);
						game.explosion(posData.position, new ExplosionData(2, 100, 5), player, false);
						ability.entity.remove();
						ability.entity = null;
					}
				}
			} else { // Button released?
				if (ability.button_released == false) {
					//Settings.p("Button released");
					ability.button_released = true;
				}
			}
		} else {			
			playerData.ability1Ready = false;

			int pcent = (int)((ability.current_cooldown / ability.cooldown_duration) * 100);
			playerData.ability1text = "Concussion Mine: " + pcent + "%";
		}
	}


}
