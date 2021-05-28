package nl.andrewlalis.crystalkeep.model;

import lombok.Getter;

@Getter
public class Model {
	private Cluster activeCluster;

	public void setActiveCluster(Cluster activeCluster) {
		this.activeCluster = activeCluster;
	}
}
