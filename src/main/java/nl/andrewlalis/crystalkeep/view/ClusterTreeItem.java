package nl.andrewlalis.crystalkeep.view;

import javafx.scene.control.TreeItem;
import nl.andrewlalis.crystalkeep.model.Cluster;
import nl.andrewlalis.crystalkeep.model.CrystalItem;

public class ClusterTreeItem extends TreeItem<CrystalItem> {
	private final Cluster cluster;

	public ClusterTreeItem(Cluster cluster) {
		super(cluster);
		this.cluster = cluster;
	}

	public Cluster getCluster() {
		return cluster;
	}
}
