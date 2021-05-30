package nl.andrewlalis.crystalkeep.model;


import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class Model {
	private Cluster activeCluster;
	private Path activeClusterPath;

	private final Set<ModelListener> listeners =  new HashSet<>();

	public Cluster getActiveCluster() {
		return activeCluster;
	}

	public Path getActiveClusterPath() {
		return activeClusterPath;
	}

	public void addListener(ModelListener listener) {
		this.listeners.add(listener);
	}

	public void setActiveCluster(Cluster activeCluster) {
		this.activeCluster = activeCluster;
		this.notifyListeners();
	}

	public void setActiveClusterPath(Path activeClusterPath) {
		this.activeClusterPath = activeClusterPath;
	}

	public void notifyListeners() {
		this.listeners.forEach(ModelListener::activeClusterUpdated);
	}
}
