package com.scs.splitscreenfps.game.systems.ql;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.AnimatedComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.components.ql.IsRecordable;
import com.scs.splitscreenfps.game.entities.ql.QuantumLeagueEntityFactory;
import com.scs.splitscreenfps.game.levels.QuantumLeagueLevel;
import com.scs.splitscreenfps.game.systems.ql.recorddata.AbstractRecordData;
import com.scs.splitscreenfps.game.systems.ql.recorddata.BulletFiredRecordData;
import com.scs.splitscreenfps.game.systems.ql.recorddata.EntityMovedRecordData;

public class QLRecordAndPlaySystem extends AbstractSystem {

	private QuantumLeagueLevel level;
	private LinkedList<AbstractRecordData> dataBeingRecordedThisPhase = new LinkedList<AbstractRecordData>();
	private LinkedList<AbstractRecordData> dataToBePlayedBack = new LinkedList<AbstractRecordData>();
	private LinkedList<AbstractRecordData> dataHasBeenPlayedBack = new LinkedList<AbstractRecordData>();

	private Iterator<AbstractRecordData> rewindIterator;

	public QLRecordAndPlaySystem(BasicECS _ecs, QuantumLeagueLevel _level) {
		super(_ecs, IsRecordable.class);

		level = _level;
	}


	public void loadNewRecordData() {
		this.dataToBePlayedBack.addAll(this.dataBeingRecordedThisPhase);
		this.dataBeingRecordedThisPhase.clear();
		this.dataToBePlayedBack.addAll(this.dataHasBeenPlayedBack);
		this.dataHasBeenPlayedBack.clear();

		Collections.sort(dataToBePlayedBack, new Comparator<AbstractRecordData>() {
			@Override
			public int compare(AbstractRecordData s1, AbstractRecordData s2) {
				return s1.time >= s2.time ? 1 : -1;
			} 
		});
		//Settings.p("Sorted");
	}


	@Override
	public void process() {
		super.process();

		if (level.isGamePhase()) {
			// Play from prev recording
			if (this.level.qlPhaseSystem.getPhaseNum012() > 0) { // Only play back if not first phase!
				if (this.dataToBePlayedBack.size() > 0) {
					AbstractRecordData next = this.dataToBePlayedBack.getFirst();
					float currentPhaseTime = level.getCurrentPhaseTime();
					while (next.time < currentPhaseTime) {
						next = this.dataToBePlayedBack.removeFirst();
						this.dataHasBeenPlayedBack.add(next);
						processForwardEvent(next);
						if (this.dataToBePlayedBack.size() == 0) {
							break;
						}
						next = this.dataToBePlayedBack.getFirst();
					}
				}
			}
		} else {
			if (rewindIterator != null) { // todo - why check this?
				for (int i=0 ; i<10 ; i++) {
					if (rewindIterator.hasNext()) {
						AbstractRecordData data = rewindIterator.next();
						processReverseEvent(data);
					} else {
						rewindIterator = null;
						level.nextGamePhase();
						break;
					}
				}
			}
		}
	}


	public void startRewind() {
		this.rewindIterator = this.dataBeingRecordedThisPhase.descendingIterator();
	}	


	private void processForwardEvent(AbstractRecordData abstract_data) {
		if (abstract_data.cmd == AbstractRecordData.CMD_MOVED) {
			EntityMovedRecordData data = (EntityMovedRecordData)abstract_data;
			AbstractEntity entity = level.getShadow(data.playerIdx, data.phase); // Make entity the correct shadow
			PositionComponent posData = (PositionComponent)entity.getComponent(PositionComponent.class);
			AnimatedComponent anim = (AnimatedComponent)entity.getComponent(AnimatedComponent.class);
			if (posData.position.equals(data.position)) {
				anim.next_animation = anim.idle_anim_name;
				//Settings.p("Shadow Idle");
			} else {
				anim.next_animation = anim.walk_anim_name;
				//Settings.p("Shadow walking");
			}
			posData.position.set(data.position);
			posData.angle_degs = data.direction;
		} else if (abstract_data.cmd == AbstractRecordData.CMD_BULLET_FIRED) {
			BulletFiredRecordData data = (BulletFiredRecordData)abstract_data;
			AbstractEntity shooter = level.getShadow(data.playerIdx, data.phase);
			AbstractEntity bullet = QuantumLeagueEntityFactory.createBullet(ecs, shooter, data.start, data.offset);
			ecs.addEntity(bullet);
		} else if (abstract_data.cmd == AbstractRecordData.CMD_REMOVED) {
			// Needed?
		} else {
			throw new RuntimeException("Unknown event type: " + abstract_data.cmd);
		}
	}


	private void processReverseEvent(AbstractRecordData abstract_data) {
		if (abstract_data.cmd == AbstractRecordData.CMD_MOVED) {
			EntityMovedRecordData data = (EntityMovedRecordData)abstract_data;
			AbstractEntity entity = data.entity;
			PositionComponent posData = (PositionComponent)entity.getComponent(PositionComponent.class);
			AnimatedComponent anim = (AnimatedComponent)entity.getComponent(AnimatedComponent.class);
			if (posData.position.equals(data.position)) {
				anim.next_animation = anim.idle_anim_name;
			} else {
				anim.next_animation = anim.walk_anim_name;
			}
			posData.position.set(data.position);
			posData.angle_degs = data.direction;
			
			// Point camera in right directioon
			IsRecordable isRecordable = (IsRecordable)entity.getComponent(IsRecordable.class);
			Camera camera = level.game.viewports[isRecordable.playerIdx].camera;
			//camera.direction =posData
			float angle_rads = (float)Math.toRadians(data.direction);
			camera.direction.x = (float)Math.cos(angle_rads);
			camera.direction.y = 0;
			camera.direction.z = -(float)Math.sin(angle_rads);
			camera.direction.nor();
			camera.up.set(Vector3.Y);
			//camera.rotate(Vector3.Y, -rotSpeedX * input.getLookRight() * dt);
			camera.update();
			//Settings.p("Putting " + entity + " at pos " + data.position);
		}
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		// Record entities position etc...
		if (level.isGamePhase()) {
			//Settings.p("Recording " + entity);
			if (level.qlPhaseSystem.getPhaseNum012() < 2) {
				float currentPhaseTime = level.getCurrentPhaseTime();
				IsRecordable isRecordable = (IsRecordable)entity.getComponent(IsRecordable.class);
				//Camera camera = level.game.viewports[isRecordable.playerIdx].camera;

				PositionComponent posData = (PositionComponent)entity.getComponent(PositionComponent.class);
				EntityMovedRecordData data = new EntityMovedRecordData(isRecordable.playerIdx, entity, this.level.qlPhaseSystem.getPhaseNum012(), currentPhaseTime, posData.position, posData.angle_degs);
				//data.cam_dir = camera.direction;
				this.dataBeingRecordedThisPhase.add(data);
			}
		}
	}


	public void addEvent(AbstractRecordData data) {
		this.dataBeingRecordedThisPhase.add(data);
	}

}
