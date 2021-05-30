package nl.andrewlalis.crystalkeep.view;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nl.andrewlalis.crystalkeep.control.AddClusterHandler;
import nl.andrewlalis.crystalkeep.control.AddShardHandler;
import nl.andrewlalis.crystalkeep.control.DeleteItemHandler;
import nl.andrewlalis.crystalkeep.model.Cluster;
import nl.andrewlalis.crystalkeep.model.CrystalItem;
import nl.andrewlalis.crystalkeep.model.Model;

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
				var cluster = (Cluster) item;
				var addShardItem = new MenuItem("Add Shard");
				addShardItem.setOnAction(new AddShardHandler(cluster, model));
				var addClusterItem = new MenuItem("Add Cluster");
				addClusterItem.setOnAction(new AddClusterHandler(cluster, model));
				menu.getItems().addAll(addShardItem, addClusterItem);
			}
			if (this.getTreeItem().getParent() != null && this.getTreeItem().getParent() instanceof ClusterTreeItem) {
				var deleteItem = new MenuItem("Delete");
				deleteItem.setOnAction(new DeleteItemHandler(this.getTreeItem(), this.model));
				menu.getItems().add(deleteItem);
			}
			this.setText(item.getName());
			InputStream is = getClass().getResourceAsStream(item.getIconPath());
			if (is != null) {
				ImageView icon = new ImageView(new Image(is));
				icon.setFitHeight(16);
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
