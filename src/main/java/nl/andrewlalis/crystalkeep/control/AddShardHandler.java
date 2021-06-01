package nl.andrewlalis.crystalkeep.control;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import nl.andrewlalis.crystalkeep.model.Cluster;
import nl.andrewlalis.crystalkeep.model.Model;
import nl.andrewlalis.crystalkeep.model.Shard;
import nl.andrewlalis.crystalkeep.model.ShardType;
import nl.andrewlalis.crystalkeep.model.shards.FileShard;
import nl.andrewlalis.crystalkeep.model.shards.LoginCredentialsShard;
import nl.andrewlalis.crystalkeep.model.shards.TextShard;

public class AddShardHandler implements EventHandler<ActionEvent> {
	private final Cluster cluster;
	private final Model model;

	public AddShardHandler(Cluster cluster, Model model) {
		this.cluster = cluster;
		this.model = model;
	}

	@Override
	public void handle(ActionEvent event) {
		Dialog<Shard> dialog = new Dialog<>();
		dialog.setTitle("Create Shard");

		GridPane gp = new GridPane();
		gp.setHgap(5);
		gp.setVgap(5);
		gp.add(new Label("Name"), 0, 0);
		TextField nameField = new TextField();
		gp.add(nameField, 1, 0);
		gp.add(new Label("Type"), 0, 1);
		ComboBox<ShardType> typeBox = new ComboBox<>(FXCollections.observableArrayList(ShardType.values()));
		gp.add(typeBox, 1, 1);
		dialog.getDialogPane().setContent(gp);

		dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		dialog.setResultConverter(result -> {
			if (result == ButtonType.OK) {
				ShardType type = typeBox.getValue();
				if (type == null) {
					return null;
				};
				String name = nameField.getText().trim();
				if (name.isBlank()) {
					return null;
				};
				switch (type) {
					case TEXT: return new TextShard(name);
					case LOGIN_CREDENTIALS: return new LoginCredentialsShard(name);
					case FILE: return new FileShard(name, "", "", new byte[0]);
					default: return null;
				}
			}
			return null;
		});
		dialog.showAndWait().ifPresent(shard -> {
			cluster.addShard(shard);
			model.notifyListeners();
		});
	}
}
