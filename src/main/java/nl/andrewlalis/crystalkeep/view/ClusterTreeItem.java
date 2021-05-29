package nl.andrewlalis.crystalkeep.view;

import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import lombok.Getter;
import nl.andrewlalis.crystalkeep.model.Cluster;
import nl.andrewlalis.crystalkeep.model.CrystalItem;

public class ClusterTreeItem extends TreeItem<CrystalItem> {
	@Getter
	private final Cluster cluster;

	public ClusterTreeItem(Cluster cluster) {
		super(cluster);
		this.cluster = cluster;
		ImageView clusterIcon = new ImageView("ui/images/cluster_node_icon.png");
		clusterIcon.setFitHeight(16);
		clusterIcon.setPreserveRatio(true);
		super.setGraphic(clusterIcon);
	}
}
