package nl.andrewlalis.crystalkeep.view;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.image.ImageView;
import nl.andrewlalis.crystalkeep.control.AddClusterHandler;
import nl.andrewlalis.crystalkeep.control.AddShardHandler;
import nl.andrewlalis.crystalkeep.control.DeleteItemHandler;
import nl.andrewlalis.crystalkeep.model.Cluster;
import nl.andrewlalis.crystalkeep.model.CrystalItem;
import nl.andrewlalis.crystalkeep.model.Model;
import nl.andrewlalis.crystalkeep.model.Shard;
import nl.andrewlalis.crystalkeep.util.ImageCache;
import nl.andrewlalis.crystalkeep.view.shard_details.ViewModels;

public class CrystalItemTreeCell extends TreeCell<CrystalItem> {
	private static final String CLUSTER_ICON = "/nl/andrewlalis/crystalkeep/ui/images/cluster_node_icon.png";

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
				deleteItem.setOnAction(new DeleteItemHandler(this.getTreeItem()));
				menu.getItems().add(deleteItem);
			}
			this.setText(item.getName());

			String iconPath = CLUSTER_ICON;
			if (item instanceof Shard) {
				var vm = ViewModels.get((Shard) item);
				if (vm.isPresent()) {
					iconPath = vm.get().getIconPath();
				}
			}
			ImageCache.get(iconPath, 16, 16).ifPresent(img -> {
				ImageView icon = new ImageView(img);
				icon.setFitHeight(16);
				icon.setPreserveRatio(true);
				this.setGraphic(icon);
			});
			this.setContextMenu(menu);
		} else {
			this.setText(null);
			this.setGraphic(null);
			this.setContextMenu(null);
		}
	}
}
