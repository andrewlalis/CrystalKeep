package nl.andrewlalis.crystalkeep.control;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import nl.andrewlalis.crystalkeep.io.ClusterIO;
import nl.andrewlalis.crystalkeep.model.*;
import nl.andrewlalis.crystalkeep.view.ClusterTreeItem;
import nl.andrewlalis.crystalkeep.view.CrystalItemTreeCell;
import nl.andrewlalis.crystalkeep.view.ShardTreeItem;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

public class MainViewController implements ModelListener {
	private Model model;

	@FXML
	public TreeView<CrystalItem> clusterTreeView;
	@FXML
	public VBox shardDetailContainer;
	@FXML
	public Menu fileMenu;

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
			if (password.isEmpty() || password.get().length == 0) {
				cluster = loader.loadUnencrypted(file.toPath());
				this.model.setActiveClusterPassword(null);
			} else {
				char[] pw = password.get();
				cluster = loader.load(file.toPath(), pw);
				this.model.setActiveClusterPassword(pw);
			}
		} catch (Exception e) {
			Notifications.create()
				.text("Could not load cluster. Check that your password is correct.")
				.hideAfter(Duration.seconds(3))
				.owner(this.shardDetailContainer.getScene().getWindow())
				.show();
			return;
		}
		model.setActiveCluster(cluster);
		model.setActiveClusterPath(file.toPath());
	}

	@FXML
	public void save() {
		if (model.getActiveCluster() == null) return;
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
		this.saveCluster(path);
	}

	@FXML
	public void saveAs() {
		if (model.getActiveCluster() == null) return;
		Path path = model.getActiveClusterPath();
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Save Cluster");
		chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Cluster Files", "cts"));
		if (path != null) {
			chooser.setInitialDirectory(path.getParent().toFile());
		} else {
			chooser.setInitialDirectory(ClusterIO.CLUSTER_PATH.toFile());
		}
		File file = chooser.showSaveDialog(this.clusterTreeView.getScene().getWindow());
		if (file == null) return;
		path = file.toPath();
		this.saveCluster(path);
	}

	@FXML
	public void newCluster() {
		Cluster c = new Cluster("Root");
		model.setActiveCluster(c);
		model.setActiveClusterPath(null);
	}

	private Optional<char[]> promptPassword() {
		Dialog<char[]> d = new Dialog<>();
		d.setTitle("Enter Password");
		d.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		PasswordField pwField = new PasswordField();
		VBox content = new VBox(10);
		content.setAlignment(Pos.CENTER);
		content.getChildren().addAll(new Label("Enter password"), pwField);
		d.getDialogPane().setContent(content);
		d.setResultConverter(param -> {
			if (param == ButtonType.OK) {
				return pwField.getText().toCharArray();
			}
			return null;
		});
		return d.showAndWait();
	}

	private void saveCluster(Path path) {
		char[] pw = this.model.getActiveClusterPassword();
		if (pw == null) {
			pw = this.promptPassword().orElse(new char[0]);
		}
		ClusterIO loader = new ClusterIO();
		if (!path.getFileName().toString().trim().toLowerCase().endsWith(".cts")) {
			path = path.resolveSibling(path.getFileName() + ".cts");
		}
		try {
			if (pw.length == 0) {
				loader.saveUnencrypted(model.getActiveCluster(), path);
			} else {
				loader.save(model.getActiveCluster(), path, pw);
			}
			Notifications.create()
				.text("Cluster saved to " + path)
				.hideAfter(Duration.seconds(3))
				.owner(this.shardDetailContainer.getScene().getWindow())
				.show();
		} catch (Exception e) {
			e.printStackTrace();
			Notifications.create()
				.text("Could not save cluster.")
				.hideAfter(Duration.seconds(3))
				.owner(this.shardDetailContainer.getScene().getWindow())
				.show();
		}
	}
}
