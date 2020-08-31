package com.scs.splitscreenfps.game.systems;

import java.util.List;

import com.scs.basicecs.AbstractEvent;
import com.scs.basicecs.BasicECS;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.game.components.IsPackageComponent;
import com.scs.splitscreenfps.game.events.FallenOffEdgeEvent;
import com.scs.splitscreenfps.game.gamemodes.ScoreAndTimeLimitSystem;

public class PackagesFallenOffEdgeSystem implements ISystem {

	private ScoreAndTimeLimitSystem scoreSystem;
	private BasicECS ecs;

	public PackagesFallenOffEdgeSystem(BasicECS _ecs, ScoreAndTimeLimitSystem _scoreSystem) {
		ecs = _ecs;
		scoreSystem = _scoreSystem;
	}


	@Override
	public void process() { // We can't process by entity cos if they've fallen off the edge, they won't exist any more!
		List<AbstractEvent> colls = ecs.getEvents(FallenOffEdgeEvent.class);
		for (AbstractEvent evt : colls) {
			FallenOffEdgeEvent foee = (FallenOffEdgeEvent)evt;
			IsPackageComponent pkg = (IsPackageComponent)foee.entity1.getComponent(IsPackageComponent.class);
			if (pkg != null) {
				this.scoreSystem.incScore(-1);
				BillBoardFPS_Main.audio.play("sfx/Hero_Death_00.mp3");
			}
		}

	}


}
