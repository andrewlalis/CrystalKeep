package nl.andrewlalis.crystalkeep.view;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import nl.andrewlalis.crystalkeep.model.Cluster;
import nl.andrewlalis.crystalkeep.model.ClusterListener;
import nl.andrewlalis.crystalkeep.model.CrystalItem;
import nl.andrewlalis.crystalkeep.model.Shard;

public class ClusterTreeItem extends TreeItem<CrystalItem> implements ClusterListener {
	private final Cluster cluster;
	private final TreeView<CrystalItem> treeView;

	public ClusterTreeItem(Cluster cluster, TreeView<CrystalItem> treeView) {
		super(cluster);
		this.cluster = cluster;
		this.treeView = treeView;
		this.cluster.addListener(this);
	}

	public Cluster getCluster() {
		return cluster;
	}

	@Override
	public void clusterAdded(Cluster cluster) {
		var item = new ClusterTreeItem(cluster, treeView);
		this.getChildren().add(item);
		this.treeView.getSelectionModel().select(item);
	}

	@Override
	public void clusterRemoved(Cluster cluster) {
		for (var child : this.getChildren()) {
			if (child.getValue().equals(cluster)) {
				this.getChildren().remove(child);
				if (child instanceof ClusterListener) cluster.removeListener((ClusterListener) child);
				return;
			}
		}
	}

	@Override
	public void shardAdded(Shard shard) {
		var item = new ShardTreeItem(shard);
		this.getChildren().add(item);
		this.treeView.getSelectionModel().select(item);
	}

	@Override
	public void shardRemoved(Shard shard) {
		for (var child : this.getChildren()) {
			if (child.getValue().equals(shard)) {
				this.getChildren().remove(child);
				return;
			}
		}
	}
}
