package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;

public class AnimatedComponent {

	public String current_animation;
	public String next_animation;
	public AnimationController animationController;
	public String walk_anim_name, idle_anim_name;
	
	public AnimatedComponent(AnimationController _animationController, String _walk_anim_name, String _idle_anim_name) {
		animationController = _animationController;
		next_animation = _idle_anim_name;
		
		walk_anim_name = _walk_anim_name;
		idle_anim_name = _idle_anim_name;
	}
	
}
