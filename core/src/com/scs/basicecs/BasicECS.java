package com.scs.basicecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class BasicECS {

	private HashMap<Class<?>, ISystem> systems = new HashMap<Class<?>, ISystem>();
	private HashMap<Integer, AbstractEntity> entities = new HashMap<Integer, AbstractEntity>();
	private List<AbstractEntity> to_add_entities = new ArrayList<AbstractEntity>();
	public List<AbstractEvent> events = new ArrayList<AbstractEvent>();

	public BasicECS() {
	}


	public void addSystem(ISystem system) {
		if (this.systems.containsKey(system.getClass()) == false) {
			this.systems.put(system.getClass(), system);
		}
	}


	public void removeSystem(Class<?> clazz) {
		this.systems.remove(clazz);
	}


	public ISystem getSystem(Class<?> clazz) {
		return this.systems.get(clazz);
	}


	public void processSystem(Class<?> clazz) {
		ISystem system = this.getSystem(clazz);
		if (system != null) {
			system.process();
		}
	}


	/**
	 * Do not call this directly.  It will be called automatically by AbstractEntity.
	 */
	protected void addEntityToSystems(AbstractEntity e, Class<?> component_class) {
		// Add to appropriate systems
		for(ISystem isystem : this.systems.values()) {
			if (isystem instanceof AbstractSystem) {
				AbstractSystem system = (AbstractSystem)isystem;
				Class<?> system_clazz = system.getComponentClass();
				if (system_clazz != null) {
					if (component_class.equals(system_clazz)) {
						system.addEntity(e);
					}
				}
			}
		}
	}


	/**
	 * Do not call this directly.  It will be called automatically by AbstractEntity.
	 */
	protected void removeEntityFromSystems(AbstractEntity e, Class<?> component_class) {
		// Remove from appropriate systems
		for(ISystem isystem : this.systems.values()) {
			if (isystem instanceof AbstractSystem) {
				AbstractSystem system = (AbstractSystem)isystem;
				Class<?> system_clazz = system.getComponentClass();
				if (system_clazz != null) {
					if (component_class.equals(system_clazz)) {
						system.entities.remove(e);
					}
				}
			}
		}
	}


	/**
	 * Call this in your main loop to avoid concurrency errors.
	 */
	public void addAndRemoveEntities() {
		// Remove any entities
		Iterator<AbstractEntity> it = this.getEntityIterator();
		while (it.hasNext()) {
			AbstractEntity entity = it.next();// this.entities.get(i);
			if (entity.isMarkedForRemoval()) {
				it.remove();

				// Remove from systems
				for(ISystem isystem : this.systems.values()) {
					if (isystem instanceof AbstractSystem) {
						AbstractSystem system = (AbstractSystem)isystem;
						Class<?> clazz = system.getComponentClass();
						if (clazz != null) {
							if (entity.getComponents().containsKey(clazz)) {
								//system.entities.remove(entity);
								system.removeEntity(entity);
							}
						}
					}
				}
			}
		}

		for(AbstractEntity e : this.to_add_entities) {
			for(ISystem isystem : this.systems.values()) {
				if (isystem instanceof AbstractSystem) {
					AbstractSystem system = (AbstractSystem)isystem;
					Class<?> clazz = system.getComponentClass();
					if (clazz != null) {
						if (e.getComponents().containsKey(clazz)) {
							system.addEntity(e);
						}
					}
				}
			}
			this.entities.put(e.entityId, e);
		}

		to_add_entities.clear();
	}


	public void addEntity(AbstractEntity e) {
		e.unmarkForRemoval(); // In case it's been re-added after being removed
		this.to_add_entities.add(e);
	}


	public void removeEntity(AbstractEntity e) {
		e.remove();
	}


	public AbstractEntity get(int i) {
		return this.entities.get(i);
	}


	public Iterator<AbstractEntity> getEntityIterator() {
		return this.entities.values().iterator();
	}


	public List<AbstractEvent> getEvents(Class<? extends AbstractEvent> clazz) {
		List<AbstractEvent> list = new ArrayList<AbstractEvent>();
		Iterator<AbstractEvent> it = this.events.iterator();
		while (it.hasNext()) {
			AbstractEvent evt = it.next();
			if (evt.getClass().equals(clazz)) {
				list.add(evt);
			}
		}
		return list;
	}


	public List<AbstractEvent> getEventsForEntity(Class<? extends AbstractEvent> clazz, AbstractEntity e) {
		List<AbstractEvent> list = new ArrayList<AbstractEvent>();
		Iterator<AbstractEvent> it = this.events.iterator();
		while (it.hasNext()) {
			AbstractEvent evt = it.next();
			if (evt.getClass().equals(clazz)) {
				if (evt.isForEntity(e)) {
					list.add(evt);
				}
			}
		}
		return list;
	}


	/**
	 * If you call this, you may want to call addAndRemoveEntities() to actually remove them.
	 */
	public void markAllEntitiesForRemoval() {
		for(AbstractEntity e : this.entities.values()) {
			e.remove();
		}
		this.to_add_entities.clear();
	}


	public boolean containsEntity(AbstractEntity e) {
		return this.entities.containsKey(e.entityId);
	}


	public boolean containsEntity(int id) {
		return this.entities.containsKey(id);
	}


	public void dispose() {
		markAllEntitiesForRemoval();
		addAndRemoveEntities();

		for (ISystem sys: this.systems.values()) {
			if (sys instanceof AbstractSystem) {
				AbstractSystem system = (AbstractSystem)sys;
				system.dispose();
				
				// Show total processing time for profiling
				if (system.total_time > 1000) {
					System.out.println(system.getClass().getSimpleName() + " = " + system.total_time);
				}
			}
		}
	}
	
	
	public int getNumEntities() {
		return this.entities.size();
	}

}
