package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;

public class AnimatedComponent {

	public AnimData current_animation;
	private AnimData next_animation;
	public AnimationController animationController;
	
	public final AnimData walk_anim, idle_anim;
	public final AnimData die_anim, jump_anim;
	
	public AnimatedComponent(AnimationController _animationController, String _walk_anim_name, String _idle_anim_name, String _die_anim_name, String _jump_anim_name) {
		animationController = _animationController;
		next_animation = new AnimData(_idle_anim_name, true);
		
		walk_anim = new AnimData(_walk_anim_name, true);
		idle_anim = new AnimData(_idle_anim_name, true);
		die_anim = new AnimData(_die_anim_name, false);
		this.jump_anim = new AnimData(_jump_anim_name, false);
	}
	
	
	public AnimData getNextAnim() {
		return this.next_animation;
		
	}
	
	
	public void setNextAnim(AnimData a) {
		if (this.next_animation != a) {
			this.next_animation = a;
		}
	}
	
	
	public class AnimData {
		
		public String name;
		public boolean loop;
		
		public AnimData(String _name, boolean _loop) {
			name = _name;
			loop = _loop;
		}
		
		@Override
		public String toString() {
			return "Anim:" + name;
		}
	}
	
}
