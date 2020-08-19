package com.scs.basicecs;

import java.util.HashMap;

public class AbstractEntity {

	private static int next_id = 0;

	private BasicECS ecs;

	public int entityId;
	public String name;
	public String tags; // todo - remove this
	private HashMap<Class<?>, Object> components = new HashMap<Class<?>, Object>();
	private HashMap<Class<?>, Object> hiddenComponents = new HashMap<Class<?>, Object>(); // For temporarily removing components, e.g. collision
	private boolean markForRemoval = false;
	
	public AbstractEntity(BasicECS _ecs, String _name) {
		ecs = _ecs;
		this.entityId = next_id++;
		this.name = _name;
	}


	public void addComponent(Object component) {
		this.components.put(component.getClass(), component);
		if (this.ecs.containsEntity(this)) { // Don't add to system if entity hasn't been added to main list yet
			ecs.addEntityToSystems(this, component.getClass());
		}
	}


	public void removeComponent(Class<?> clazz) {
		this.components.remove(clazz);
		ecs.removeEntityFromSystems(this, clazz);
	}


	public void hideComponent(Class<?> clazz) {
		Object component = this.components.remove(clazz);
		this.hiddenComponents.put(clazz, component);
		ecs.removeEntityFromSystems(this, clazz);
	}


	public void restoreComponent(Class<?> clazz) {
		Object component = this.hiddenComponents.remove(clazz);
		if (component != null) { // Just in case the component doesn't exist
			this.components.put(clazz, component);
			ecs.addEntityToSystems(this, clazz);
		}
	}


	public Object getComponent(Class<?> name) {
		if (this.components.containsKey(name)) {
			return this.components.get(name);
		} else {
			return null;
		}
	}


	public HashMap<Class<?>, Object> getComponents() {
		return this.components;
	}


	public boolean isMarkedForRemoval() {
		return this.markForRemoval;
	}


	public void remove() {
		this.markForRemoval = true;
	}


	protected void unmarkForRemoval() {
		this.markForRemoval = false;
	}


	@Override
	public String toString() {
		return name;
	}

}
