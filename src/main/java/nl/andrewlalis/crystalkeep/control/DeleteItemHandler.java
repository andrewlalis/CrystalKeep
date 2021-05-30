package nl.andrewlalis.crystalkeep.control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeItem;
import nl.andrewlalis.crystalkeep.model.Cluster;
import nl.andrewlalis.crystalkeep.model.CrystalItem;
import nl.andrewlalis.crystalkeep.model.Model;
import nl.andrewlalis.crystalkeep.model.Shard;
import nl.andrewlalis.crystalkeep.view.ClusterTreeItem;

import java.util.Optional;

public class DeleteItemHandler implements EventHandler<ActionEvent> {
	private final TreeItem<CrystalItem> treeItem;
	private final Model model;

	public DeleteItemHandler(TreeItem<CrystalItem> treeItem, Model model) {
		this.treeItem = treeItem;
		this.model = model;
	}

	@Override
	public void handle(ActionEvent event) {
		if (this.treeItem.getParent() != null && this.treeItem.getParent() instanceof ClusterTreeItem) {
			Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this item?").showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				var cluster = ((ClusterTreeItem) this.treeItem.getParent()).getCluster();
				var item = this.treeItem.getValue();
				if (item instanceof Shard) {
					cluster.removeShard((Shard) item);
				} else if (item instanceof Cluster) {
					cluster.removeCluster((Cluster) item);
				}
				this.model.notifyListeners();
			}
		}
	}
}
