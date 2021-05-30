package nl.andrewlalis.crystalkeep.control;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import nl.andrewlalis.crystalkeep.model.*;
import nl.andrewlalis.crystalkeep.model.serialization.ClusterLoader;
import nl.andrewlalis.crystalkeep.view.ClusterTreeItem;
import nl.andrewlalis.crystalkeep.view.CrystalItemTreeCell;
import nl.andrewlalis.crystalkeep.view.ShardTreeItem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;

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
		this.clusterTreeView.setCellFactory(param -> new CrystalItemTreeCell(this.model));
		this.clusterTreeView.getSelectionModel().selectedItemProperty()
				.addListener(new ClusterTreeViewItemSelectionListener(this.shardDetailContainer));
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

	@FXML
	public void exit() {
		Platform.exit();
	}

	@FXML
	public void load() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Load a Cluster");
		chooser.setInitialDirectory(ClusterLoader.CLUSTER_PATH.toFile());
		chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Cluster Files", "cts"));
		File file = chooser.showOpenDialog(this.clusterTreeView.getScene().getWindow());
		if (file == null) return;
		ClusterLoader loader = new ClusterLoader();
		var password = loader.promptPassword();
		if (password.isEmpty() || password.get().isEmpty()) return;
		try {
			var cluster = loader.load(file.toPath(), password.get());
			model.setActiveCluster(cluster);
			model.setActiveClusterPath(file.toPath());
		} catch (Exception e) {
			e.printStackTrace();
			new Alert(Alert.AlertType.WARNING, "Could not load cluster.").showAndWait();
		}
	}

	@FXML
	public void save() {
		if (model.getActiveCluster() == null) return;
		ClusterLoader loader = new ClusterLoader();
		Path path = model.getActiveClusterPath();
		if (path == null) {
			FileChooser chooser = new FileChooser();
			chooser.setTitle("Save Cluster");
			chooser.setInitialDirectory(ClusterLoader.CLUSTER_PATH.toFile());
			chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Cluster Files", "cts"));
			File file = chooser.showSaveDialog(this.clusterTreeView.getScene().getWindow());
			if (file == null) return;
			path = file.toPath();
		}
		var password = loader.promptPassword();
		if (password.isEmpty() || password.get().isEmpty()) return;
		try {
			new ClusterLoader().save(model.getActiveCluster(), path, password.get());
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
			var alert = new Alert(Alert.AlertType.ERROR, "Could not save cluster.");
			alert.showAndWait();
		}
	}

	@FXML
	public void newCluster() {
		Cluster c = new Cluster("Root");
		model.setActiveCluster(c);
		model.setActiveClusterPath(null);
	}
}
