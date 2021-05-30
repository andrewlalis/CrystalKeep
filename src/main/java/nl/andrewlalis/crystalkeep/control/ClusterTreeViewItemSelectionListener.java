package nl.andrewlalis.crystalkeep.control;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import nl.andrewlalis.crystalkeep.model.CrystalItem;
import nl.andrewlalis.crystalkeep.model.Shard;
import nl.andrewlalis.crystalkeep.model.shards.LoginCredentialsShard;
import nl.andrewlalis.crystalkeep.model.shards.TextShard;
import nl.andrewlalis.crystalkeep.view.ShardTreeItem;
import nl.andrewlalis.crystalkeep.view.shard_details.LoginCredentialsPane;
import nl.andrewlalis.crystalkeep.view.shard_details.ShardPane;
import nl.andrewlalis.crystalkeep.view.shard_details.TextShardPane;

import java.util.HashMap;
import java.util.Map;

public class ClusterTreeViewItemSelectionListener implements ChangeListener<TreeItem<CrystalItem>> {
	private static final Map<Class<? extends Shard>, Class<? extends ShardPane<? extends Shard>>> shardPanesMap = new HashMap<>();
	static {
		shardPanesMap.put(TextShard.class, TextShardPane.class);
		shardPanesMap.put(LoginCredentialsShard.class, LoginCredentialsPane.class);
	}

	private final VBox shardDetailContainer;

	public ClusterTreeViewItemSelectionListener(VBox shardDetailContainer) {
		this.shardDetailContainer = shardDetailContainer;
	}

	@Override
	public void changed(ObservableValue<? extends TreeItem<CrystalItem>> observable, TreeItem<CrystalItem> oldValue, TreeItem<CrystalItem> newValue) {
		shardDetailContainer.getChildren().clear();
		if (newValue instanceof ShardTreeItem) {
			var node = (ShardTreeItem) newValue;
			var paneClass = shardPanesMap.get(node.getShard().getClass());
			try {
				var pane = paneClass.getDeclaredConstructor(node.getShard().getClass()).newInstance(node.getShard());
				shardDetailContainer.getChildren().add(pane);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
