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

/**
 * The view model for a type of shard. A shard type's view model defines how the
 * shard is displayed in the application, including the actual UI contents, and
 * icon.
 * @param <T> The type of shard.
 */
public abstract class ShardViewModel<T extends Shard> {
	protected final T shard;

	public ShardViewModel(T shard) {
		this.shard = shard;
	}

	public Node getContentPane() {
		VBox pane = new VBox(5);
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
		pane.getChildren().add(gp);
		pane.getChildren().add(new Separator(Orientation.HORIZONTAL));
		pane.getChildren().add(this.getContent(shard));
		return pane;
	}

	protected abstract Node getContent(T shard);

	public String getIconPath() {
		return "/nl/andrewlalis/crystalkeep/ui/images/shard_node_icon.png";
	}
}
