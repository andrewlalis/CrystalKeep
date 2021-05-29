package nl.andrewlalis.crystalkeep.view;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.image.ImageView;
import nl.andrewlalis.crystalkeep.model.CrystalItem;
import nl.andrewlalis.crystalkeep.model.Shard;

public class CrystalItemTreeCell extends TreeCell<CrystalItem> {
	private final ContextMenu contextMenu;

	public CrystalItemTreeCell() {
		MenuItem item = new MenuItem("Delete");
		this.contextMenu = new ContextMenu(item);
	}

	@Override
	protected void updateItem(CrystalItem item, boolean empty) {
		super.updateItem(item, empty);

		if (!empty) {
			this.setText(item.getName());
			ImageView icon = new ImageView(item.getIconPath());
			icon.setFitHeight(16);
			icon.setPreserveRatio(true);
			this.setGraphic(icon);
			if (item instanceof Shard) {
				this.setContextMenu(this.contextMenu);
			}
		} else {
			this.setText(null);
			this.setGraphic(null);
			this.setContextMenu(null);
		}
	}
}
