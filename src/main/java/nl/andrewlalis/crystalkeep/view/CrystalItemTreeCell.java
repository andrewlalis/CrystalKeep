package nl.andrewlalis.crystalkeep.view;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nl.andrewlalis.crystalkeep.model.Cluster;
import nl.andrewlalis.crystalkeep.model.CrystalItem;
import nl.andrewlalis.crystalkeep.model.Model;
import nl.andrewlalis.crystalkeep.model.Shard;

import java.io.InputStream;

public class CrystalItemTreeCell extends TreeCell<CrystalItem> {
	private final Model model;

	public CrystalItemTreeCell(Model model) {
		this.model = model;
	}


	@Override
	protected void updateItem(CrystalItem item, boolean empty) {
		super.updateItem(item, empty);

		if (!empty) {
			ContextMenu menu = new ContextMenu();
			if (item instanceof Cluster) {
				var addShardItem = new MenuItem("Add Shard");
				var addClusterItem = new MenuItem("Add Cluster");
				menu.getItems().addAll(addShardItem, addClusterItem);
			}
			var deleteItem = new MenuItem("Delete");
			deleteItem.setOnAction(event -> {
				if (this.getTreeItem().getParent() != null && this.getTreeItem().getParent() instanceof ClusterTreeItem) {
					var cluster = ((ClusterTreeItem) this.getTreeItem().getParent()).getCluster();
					if (item instanceof Shard) {
						cluster.removeShard((Shard) item);
					} else if (item instanceof Cluster) {
						cluster.removeCluster((Cluster) item);
					}
					this.model.notifyListeners();
				}
			});
			menu.getItems().add(deleteItem);
			this.setText(item.getName());
			InputStream is = getClass().getResourceAsStream(item.getIconPath());
			if (is != null) {
				ImageView icon = new ImageView(new Image(is));
				icon.setFitHeight(24);
				icon.setPreserveRatio(true);
				this.setGraphic(icon);
			}
			this.setContextMenu(menu);
		} else {
			this.setText(null);
			this.setGraphic(null);
			this.setContextMenu(null);
		}
	}
}
