package com.scs.splitscreenfps.game.systems;

import java.util.Iterator;
import java.util.List;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractEvent;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.CollectPackageComponent;
import com.scs.splitscreenfps.game.components.IsPackageComponent;
import com.scs.splitscreenfps.game.events.EventCollision;
import com.scs.splitscreenfps.game.gamemodes.ScoreAndTimeLimitSystem;

public class CollectPackageSystem extends AbstractSystem {

	private Game game;

	private int num_collectors = 0;
	private ScoreAndTimeLimitSystem scoreSystem;

	public CollectPackageSystem(Game _game, BasicECS ecs, ScoreAndTimeLimitSystem _scoreSystem) {
		super(ecs, CollectPackageComponent.class);

		game = _game;
		scoreSystem = _scoreSystem;
	}


	@Override
	public void process() {
		super.process();

		if (num_collectors == 0) {
			// Find the collectors
			Iterator<AbstractEntity> it = game.ecs.getEntityIterator();
			while (it.hasNext()) {
				AbstractEntity e = it.next();
				//if (num_collectors <= game.players.length) { // One dispenser per player
					if (e.tags != null && e.tags.contains("collector")) {
						int type = Integer.parseInt(e.tags.substring(e.tags.length()-1));
						e.addComponent(new CollectPackageComponent(type));
						num_collectors++;
					}
				//} else {
					//e.remove();
				//}
			}
		}
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		CollectPackageComponent collector = (CollectPackageComponent)entity.getComponent(CollectPackageComponent.class);

		List<AbstractEvent> colls = ecs.getEventsForEntity(EventCollision.class, entity);
		for (AbstractEvent evt : colls) {
			EventCollision coll = (EventCollision)evt;

			IsPackageComponent pkg = (IsPackageComponent)coll.entity2.getComponent(IsPackageComponent.class);
			if (pkg == null) {
				continue;
			}

			if (collector.type == pkg.type) {
				coll.entity2.remove();
				scoreSystem.incScore(2);
			}
		}
	}

}
