package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.Gdx;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.AnimatedComponent;

public class AnimationSystem extends AbstractSystem {

	public AnimationSystem(BasicECS ecs) {
		super(ecs,  AnimatedComponent.class);
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		AnimatedComponent anim = (AnimatedComponent)entity.getComponent(AnimatedComponent.class);

		if (anim.next_animation != null && anim.next_animation.length() > 0) {
			if (anim.current_animation != anim.next_animation) {
				//Settings.p("Setting anim for " + entity + "to " + anim.next_animation);
				anim.current_animation = anim.next_animation;
				anim.animationController.animate(anim.current_animation, -1, 2f, null, 0f); // todo - move anim speed to model data
				anim.next_animation = null;
			}
		}		
		anim.animationController.update(Gdx.graphics.getDeltaTime());
	}

}
