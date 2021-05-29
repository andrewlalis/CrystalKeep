package nl.andrewlalis.crystalkeep.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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

import java.io.IOException;

public class MainViewController implements ModelListener {
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
		this.clusterTreeView.setCellFactory(param -> new CrystalItemTreeCell());
		this.clusterTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			shardDetailContainer.getChildren().clear();
			if (newValue instanceof ShardTreeItem) {
				var node = (ShardTreeItem) newValue;
				System.out.println(node.getShard());
				if (node.getShard() instanceof TextShard) {
					shardDetailContainer.getChildren().add(new TextArea(((TextShard) node.getShard()).getText()));
				} else if (node.getShard() instanceof LoginCredentialsShard) {
					shardDetailContainer.getChildren().add(new Label("Username: " + ((LoginCredentialsShard) node.getShard()).getUsername()));
					shardDetailContainer.getChildren().add(new Label("Password: " + ((LoginCredentialsShard) node.getShard()).getPassword()));
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
		for (Cluster child : cluster.getClustersOrdered()) {
			node.getChildren().add(this.createNode(child));
		}
		for (Shard shard : cluster.getShardsOrdered()) {
			node.getChildren().add(new ShardTreeItem(shard));
		}
		return node;
	}
}
