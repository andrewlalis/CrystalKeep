package nl.andrewlalis.crystalkeep.view.shard_details;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import nl.andrewlalis.crystalkeep.model.shards.LoginCredentialsShard;

public class LoginCredentialsPane extends ShardPane<LoginCredentialsShard> {
	public LoginCredentialsPane(LoginCredentialsShard shard) {
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
		usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
			shard.setUsername(newValue);
		});
		gp.add(usernameField, 1, 0);
		gp.add(new Label("Password"), 0, 1);
		var passwordField = new PasswordField();
		passwordField.setText(shard.getPassword());
		var rawPasswordField = new TextField(shard.getPassword());
		rawPasswordField.setVisible(false);
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
		var showPasswordCheckbox = new CheckBox("Show password");
		showPasswordCheckbox.setSelected(false);
		showPasswordCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			passwordField.setVisible(!newValue);
			rawPasswordField.setVisible(newValue);
		});
		gp.add(showPasswordCheckbox, 1, 2);
		return gp;
	}
}
