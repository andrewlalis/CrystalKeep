package nl.andrewlalis.crystalkeep.view.shards;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import nl.andrewlalis.crystalkeep.model.shards.LoginCredentialsShard;
import org.controlsfx.control.Notifications;

public class LoginCredentialsViewModel extends ShardViewModel<LoginCredentialsShard> {
	public LoginCredentialsViewModel(LoginCredentialsShard shard) {
		super(shard);
	}

	@Override
	protected Node getContent(LoginCredentialsShard shard) {
		GridPane gp = new GridPane();
		gp.setPadding(new Insets(5));
		gp.setHgap(5);
		gp.setVgap(5);
		gp.add(new Label("Username"), 0, 0);
		var usernameField = new TextField(shard.getUsername());
		usernameField.setPrefColumnCount(40);
		usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
			shard.setUsername(newValue);
		});
		gp.add(usernameField, 1, 0);
		gp.add(new Label("Password"), 0, 1);
		var passwordField = new PasswordField();
		passwordField.setText(shard.getPassword());
		passwordField.setPrefColumnCount(40);
		var rawPasswordField = new TextField(shard.getPassword());
		rawPasswordField.setVisible(false);
		rawPasswordField.setPrefColumnCount(40);
		passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
			shard.setPassword(newValue);
			rawPasswordField.setText(newValue);
		});
		rawPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
			shard.setPassword(newValue);
			passwordField.setText(newValue);
		});
		var passwordsContainer = new Pane();
		passwordsContainer.getChildren().add(passwordField);
		passwordsContainer.getChildren().add(rawPasswordField);
		gp.add(passwordsContainer, 1, 1);

		var passwordActionsPane = new HBox(5);
		var showPasswordCheckbox = new CheckBox("Show password");
		showPasswordCheckbox.setSelected(false);
		showPasswordCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			passwordField.setVisible(!newValue);
			rawPasswordField.setVisible(newValue);
		});
		var copyPasswordButton = new Button("Copy to Clipboard");
		copyPasswordButton.setOnAction(event -> {
			ClipboardContent content = new ClipboardContent();
			content.putString(shard.getPassword());
			Clipboard.getSystemClipboard().setContent(content);
			Notifications.create()
				.text("Password copied to clipboard.")
				.hideAfter(Duration.seconds(3))
				.owner(gp)
				.show();
		});

		passwordActionsPane.getChildren().addAll(showPasswordCheckbox, copyPasswordButton);

		gp.add(passwordActionsPane, 1, 2);
		return gp;
	}

	@Override
	public String getIconPath() {
		return "/nl/andrewlalis/crystalkeep/ui/images/login_credentials_shard_node_icon.png";
	}
}
