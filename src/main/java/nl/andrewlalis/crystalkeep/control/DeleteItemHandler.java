package nl.andrewlalis.crystalkeep.control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeItem;
import nl.andrewlalis.crystalkeep.model.Cluster;
import nl.andrewlalis.crystalkeep.model.CrystalItem;
import nl.andrewlalis.crystalkeep.model.Shard;
import nl.andrewlalis.crystalkeep.view.ClusterTreeItem;

import java.util.Optional;

public class DeleteItemHandler implements EventHandler<ActionEvent> {
	private final TreeItem<CrystalItem> treeItem;

	public DeleteItemHandler(TreeItem<CrystalItem> treeItem) {
		this.treeItem = treeItem;
	}

	@Override
	public void handle(ActionEvent event) {
		if (this.treeItem.getParent() != null && this.treeItem.getParent() instanceof ClusterTreeItem) {
			Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this item?").showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				ClusterTreeItem parentTreeItem = (ClusterTreeItem) this.treeItem.getParent();
				var parentCluster = parentTreeItem.getCluster();
				var item = this.treeItem.getValue();
				if (item instanceof Shard) {
					parentCluster.removeShard((Shard) item);
				} else if (item instanceof Cluster) {
					var cluster = (Cluster) item;
					parentCluster.removeCluster(cluster);
				}
			}
		}
	}
}
