package nl.andrewlalis.crystalkeep.control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import nl.andrewlalis.crystalkeep.model.Cluster;
import nl.andrewlalis.crystalkeep.model.Model;
import nl.andrewlalis.crystalkeep.model.Shard;
import nl.andrewlalis.crystalkeep.model.ShardType;
import nl.andrewlalis.crystalkeep.model.shards.LoginCredentialsShard;
import nl.andrewlalis.crystalkeep.model.shards.TextShard;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AddShardHandler implements EventHandler<ActionEvent> {
	private final Cluster cluster;
	private final Model model;

	public AddShardHandler(Cluster cluster, Model model) {
		this.cluster = cluster;
		this.model = model;
	}

	@Override
	public void handle(ActionEvent event) {
		Dialog<String> d = new TextInputDialog();
		d.setContentText("Enter the name of the new shard.");
		d.showAndWait().ifPresent(s -> {
			List<String> choices = Arrays.stream(ShardType.values())
					.map(Enum::name)
					.collect(Collectors.toList());
			Dialog<String> d1 = new ChoiceDialog<>("TEXT", choices);
			d1.setContentText("Choose the type of shard to create.");
			d1.showAndWait().ifPresent(typeName -> {
				ShardType type = ShardType.valueOf(typeName.toUpperCase());
				Shard shard;
				switch (type) {
					case TEXT:
						shard = new TextShard(s);
						break;
					case LOGIN_CREDENTIALS:
						shard = new LoginCredentialsShard(s);
						break;
					default:
						throw new IllegalStateException("Invalid shard type selected.");
				}
				cluster.addShard(shard);
				model.notifyListeners();
			});
		});
	}
}
