package nl.andrewlalis.crystalkeep.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import nl.andrewlalis.crystalkeep.model.*;
import nl.andrewlalis.crystalkeep.model.serialization.ClusterLoader;
import nl.andrewlalis.crystalkeep.model.shards.LoginCredentialsShard;
import nl.andrewlalis.crystalkeep.model.shards.TextShard;
import nl.andrewlalis.crystalkeep.view.ClusterTreeItem;
import nl.andrewlalis.crystalkeep.view.CrystalItemTreeCell;
import nl.andrewlalis.crystalkeep.view.ShardTreeItem;
import nl.andrewlalis.crystalkeep.view.shard_details.LoginCredentialsPane;
import nl.andrewlalis.crystalkeep.view.shard_details.ShardPane;
import nl.andrewlalis.crystalkeep.view.shard_details.TextShardPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainViewController implements ModelListener {
	private static final Map<Class<? extends Shard>, Class<? extends ShardPane<? extends Shard>>> shardPanesMap = new HashMap<>();
	static {
		shardPanesMap.put(TextShard.class, TextShardPane.class);
		shardPanesMap.put(LoginCredentialsShard.class, LoginCredentialsPane.class);
	}

	private Model model;

	@FXML
	public TreeView<CrystalItem> clusterTreeView;
	@FXML
	public VBox shardDetailContainer;

	public void init(Model model) {
		this.model = model;
		this.model.addListener(this);
		this.activeClusterUpdated();
		assert(this.clusterTreeView != null);
		this.clusterTreeView.setCellFactory(param -> new CrystalItemTreeCell(this.model));
		this.clusterTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
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
		});
	}

	@FXML
	public void exit(ActionEvent event) {
		System.out.println("Exiting...");
		try {
			new ClusterLoader().saveDefault(model.getActiveCluster());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void activeClusterUpdated() {
		if (this.model.getActiveCluster() == null) return;
		TreeItem<CrystalItem> root = this.createNode(this.model.getActiveCluster());
		this.clusterTreeView.setRoot(root);
		this.clusterTreeView.setShowRoot(true);
	}

	private TreeItem<CrystalItem> createNode(Cluster cluster) {
		ClusterTreeItem node = new ClusterTreeItem(cluster);
		node.setExpanded(true);
		for (Cluster child : cluster.getClustersOrdered()) {
			node.getChildren().add(this.createNode(child));
		}
		for (Shard shard : cluster.getShardsOrdered()) {
			node.getChildren().add(new ShardTreeItem(shard));
		}
		return node;
	}
}
