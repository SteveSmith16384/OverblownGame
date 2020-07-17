package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;

public class AnimatedComponent {

	public AnimData current_animation;
	public AnimData next_animation;
	public AnimationController animationController;
	public String walk_anim_name, idle_anim_name;
	public String die_anim_name, jump_anim_name;
	
	public AnimatedComponent(AnimationController _animationController, String _walk_anim_name, String _idle_anim_name, String _die_anim_name, String _jump_anim_name) {
		animationController = _animationController;
		next_animation = new AnimData(_idle_anim_name, true);
		
		walk_anim_name = _walk_anim_name;
		idle_anim_name = _idle_anim_name;
		die_anim_name = _die_anim_name;
		this.jump_anim_name = _jump_anim_name;
	}
	
	
	public class AnimData {
		
		public String name;
		public boolean loop;
		
		public AnimData(String _name, boolean _loop) {
			name = _name;
			loop = _loop;
		}
	}
	
}
