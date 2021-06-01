package nl.andrewlalis.crystalkeep.control;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import nl.andrewlalis.crystalkeep.io.ClusterIO;
import nl.andrewlalis.crystalkeep.model.*;
import nl.andrewlalis.crystalkeep.view.ClusterTreeItem;
import nl.andrewlalis.crystalkeep.view.CrystalItemTreeCell;
import nl.andrewlalis.crystalkeep.view.ShardTreeItem;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

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
		ClusterTreeItem node = new ClusterTreeItem(cluster, this.clusterTreeView);
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
		chooser.setInitialDirectory(ClusterIO.CLUSTER_PATH.toFile());
		chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Cluster Files", "cts"));
		File file = chooser.showOpenDialog(this.clusterTreeView.getScene().getWindow());
		if (file == null) return;
		ClusterIO loader = new ClusterIO();
		var password = this.promptPassword();
		Cluster cluster;
		try {
			if (password.isEmpty() || password.get().isEmpty()) {
				cluster = loader.loadUnencrypted(file.toPath());
				this.model.setActiveClusterPassword(null);
			} else {
				char[] pw = password.get().toCharArray();
				cluster = loader.load(file.toPath(), pw);
				this.model.setActiveClusterPassword(pw);
			}
		} catch (Exception e) {
			e.printStackTrace();
			new Alert(Alert.AlertType.WARNING, "Could not load cluster.").showAndWait();
			return;
		}
		model.setActiveCluster(cluster);
		model.setActiveClusterPath(file.toPath());
	}

	@FXML
	public void save() {
		if (model.getActiveCluster() == null) return;
		ClusterIO loader = new ClusterIO();
		Path path = model.getActiveClusterPath();
		if (path == null) {
			FileChooser chooser = new FileChooser();
			chooser.setTitle("Save Cluster");
			chooser.setInitialDirectory(ClusterIO.CLUSTER_PATH.toFile());
			chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Cluster Files", "cts"));
			File file = chooser.showSaveDialog(this.clusterTreeView.getScene().getWindow());
			if (file == null) return;
			path = file.toPath();
		}
		char[] pw = this.model.getActiveClusterPassword();
		if (pw == null) {
			pw = this.promptPassword().orElse("").toCharArray();
		}
		try {
			if (pw.length == 0) {
				loader.saveUnencrypted(model.getActiveCluster(), path);
			} else {
				loader.save(model.getActiveCluster(), path, pw);
			}
		} catch (Exception e) {
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

	private Optional<String> promptPassword() {
		Dialog<String> d = new Dialog<>();
		d.setTitle("Enter Password");
		d.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		PasswordField pwField = new PasswordField();
		VBox content = new VBox(10);
		content.setAlignment(Pos.CENTER);
		content.getChildren().addAll(new Label("Enter password"), pwField);
		d.getDialogPane().setContent(content);
		d.setResultConverter(param -> {
			if (param == ButtonType.OK) {
				return pwField.getText();
			}
			return null;
		});
		return d.showAndWait();
	}
}
