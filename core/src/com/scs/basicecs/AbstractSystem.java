package com.scs.basicecs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractSystem implements ISystem {

	protected BasicECS ecs;
	protected List<AbstractEntity> entities;
	private String name;
	private Class<?> component_class;

	public long total_time;

	/**
	 * 
	 * @param _ecs
	 * @param _component_class The component that this system is interested in.
	 */
	public AbstractSystem(BasicECS _ecs,  Class<?> _component_class) {
		this.ecs = _ecs;
		component_class = _component_class;

		name = this.getClass().getSimpleName();

		this.ecs.addSystem(this);

		if (this.getComponentClass() != null) {
			entities = new ArrayList<AbstractEntity>();
		} else {
			throw new RuntimeException("This should not happen!");
		}
	}


	/**
	 * Note to future self: Do NOT change this to handle multiple component types.  If that is
	 * needed, create a separate system!
	 */
	public final Class<?> getComponentClass() {
		return component_class;
	}


	public String getName() {
		return this.getClass().getSimpleName();
	}


	/**
	 * This should only be called by the BasicECS class.
	 * @param e
	 */
	public void addEntity(AbstractEntity e) {
		if (entities.contains(e) == false) {
			entities.add(e);
		} else {
			//throw new RuntimeException("Entity " + e + " already exists in " + this.name);
		}

	}


	public void removeEntity(AbstractEntity e) {
		this.entities.remove(e);
	}


	public void process() {
		long start = System.currentTimeMillis();

		if (this.entities == null) {
			Iterator<AbstractEntity> it = ecs.getEntityIterator();
			while (it.hasNext()) {
				AbstractEntity entity = it.next();
				this.processEntity(entity);
			}
		} else {
			for (int i=entities.size()-1 ; i>=0 ; i--) { // In case we've added/remvoed a component while loping through components
				//Iterator<AbstractEntity> it = entities.iterator();
				//while (it.hasNext()) {
				//AbstractEntity entity = it.next();
				AbstractEntity entity = entities.get(i);
				this.processEntity(entity);
			}
		}

		long duration = System.currentTimeMillis() - start;
		this.total_time += duration;
	}


	public void processEntity(AbstractEntity entity) {
		// Override if you want to process all entities with required component.
	}


	public Iterator<AbstractEntity> getEntityIterator() {
		return this.entities.iterator();
	}


	public String toString() {
		return name;
	}


	public void dispose() {
		// Override if required
	}

}
