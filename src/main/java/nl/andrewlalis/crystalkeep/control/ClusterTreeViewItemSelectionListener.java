package nl.andrewlalis.crystalkeep.control;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import nl.andrewlalis.crystalkeep.model.CrystalItem;
import nl.andrewlalis.crystalkeep.view.ShardTreeItem;
import nl.andrewlalis.crystalkeep.view.shards.ViewModels;

/**
 * This listener will update the shard detail container pane (the main center
 * content of the application) to show the contents of a selected shard.
 */
public class ClusterTreeViewItemSelectionListener implements ChangeListener<TreeItem<CrystalItem>> {
	private final VBox shardDetailContainer;

	public ClusterTreeViewItemSelectionListener(VBox shardDetailContainer) {
		this.shardDetailContainer = shardDetailContainer;
	}

	@Override
	public void changed(ObservableValue<? extends TreeItem<CrystalItem>> observable, TreeItem<CrystalItem> oldValue, TreeItem<CrystalItem> newValue) {
		shardDetailContainer.getChildren().clear();
		if (newValue instanceof ShardTreeItem) {
			var node = (ShardTreeItem) newValue;
			ViewModels.get(node.getShard()).ifPresent(vm -> shardDetailContainer.getChildren().add(vm.getContentPane()));
		}
	}
}
