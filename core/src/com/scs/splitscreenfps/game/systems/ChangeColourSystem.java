package com.scs.splitscreenfps.game.systems;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.ChangeColourComponent;
import com.scs.splitscreenfps.game.components.DrawTextComponent;

public class ChangeColourSystem extends AbstractSystem {

	public ChangeColourSystem(BasicECS ecs) {
		super(ecs, ChangeColourComponent.class);
	}


	public void processEntity(AbstractEntity entity) {
		ChangeColourComponent colComp = (ChangeColourComponent)entity.getComponent(ChangeColourComponent.class);
		if (colComp.timeUntilChange < System.currentTimeMillis()) {
			colComp.timeUntilChange = System.currentTimeMillis() + colComp.interval;
			
			if (colComp.current == null || colComp.current == colComp.col2) {
				colComp.current = colComp.col1;
			} else {
				colComp.current = colComp.col2;
			}

			DrawTextComponent text = (DrawTextComponent)entity.getComponent(DrawTextComponent.class);
			if (text != null) {
				text.colour = colComp.current;
			}
		}
	}

}