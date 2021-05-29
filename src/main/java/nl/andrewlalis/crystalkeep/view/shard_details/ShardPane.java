package nl.andrewlalis.crystalkeep.view.shard_details;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import nl.andrewlalis.crystalkeep.model.Shard;

import java.time.format.DateTimeFormatter;

public abstract class ShardPane<T extends Shard> extends VBox {
	public ShardPane(T shard) {
		this.setSpacing(5);
		GridPane gp = new GridPane();
		gp.setPadding(new Insets(5));
		gp.setHgap(5);
		gp.setVgap(5);
		gp.add(new Label("Name"), 0, 0);
		var nameField = new TextField(shard.getName());
		nameField.textProperty().addListener((observable, oldValue, newValue) -> {
			shard.setName(newValue);
		});
		gp.add(nameField, 1, 0);
		gp.add(new Label("Created at"), 0, 1);
		var createdAtField = new TextField(shard.getCreatedAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss")));
		createdAtField.setEditable(false);
		gp.add(createdAtField, 1, 1);
		this.getChildren().add(gp);
		this.getChildren().add(new Separator(Orientation.HORIZONTAL));
		this.getChildren().add(this.getContent(shard));
	}

	protected abstract Node getContent(T shard);
}
