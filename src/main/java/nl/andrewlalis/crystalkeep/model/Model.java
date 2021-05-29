package nl.andrewlalis.crystalkeep.model;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Model {
	private Cluster activeCluster;

	private final Set<ModelListener> listeners =  new HashSet<>();

	public void addListener(ModelListener listener) {
		this.listeners.add(listener);
	}

	public void setActiveCluster(Cluster activeCluster) {
		this.activeCluster = activeCluster;
		this.listeners.forEach(ModelListener::activeClusterUpdated);
	}
}
