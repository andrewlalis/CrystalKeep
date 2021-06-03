package nl.andrewlalis.crystalkeep.view.shards;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import nl.andrewlalis.crystalkeep.model.shards.FileShard;
import nl.andrewlalis.crystalkeep.util.StringUtils;

import java.io.*;

public class FileShardViewModel extends ShardViewModel<FileShard> {
	public FileShardViewModel(FileShard shard) {
		super(shard);
	}

	@Override
	protected Node getContent(FileShard shard) {
		VBox container = new VBox(10);
		container.setPadding(new Insets(5));
		GridPane gp = new GridPane();
		gp.setVgap(5);
		gp.setHgap(5);

		gp.add(new Label("File Name"), 0, 0);
		TextField nameField = new TextField(shard.getFileName());
		nameField.textProperty().addListener((observable, oldValue, newValue) -> {
			shard.setFileName(newValue);
		});
		gp.add(nameField, 1, 0);
		gp.add(new Label("File Size"), 0, 1);
		TextField sizeField = new TextField(shard.getContents().length + " bytes");
		sizeField.setEditable(false);
		gp.add(sizeField, 1, 1);

		Button setFileButton = new Button("Set File");
		setFileButton.setOnAction(event -> {
			FileChooser chooser = new FileChooser();
			chooser.setTitle("Choose a File");
			File file = chooser.showOpenDialog(gp.getScene().getWindow());
			if (file != null) {
				try (FileInputStream fis = new FileInputStream(file)) {
					byte[] contents = fis.readAllBytes();
					shard.setFileName(file.getName());
					shard.setContents(contents);
					nameField.setText(shard.getFileName());
					sizeField.setText(shard.getContents().length + " bytes");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		gp.add(setFileButton, 0, 2);

		Button extractFileButton = new Button("Extract File");
		extractFileButton.setOnAction(event -> {
			FileChooser chooser = new FileChooser();
			chooser.setTitle("Extract File");
			chooser.setInitialFileName(shard.getFileName());
			File file = chooser.showSaveDialog(gp.getScene().getWindow());
			if (file != null) {
				try (FileOutputStream fos = new FileOutputStream(file)) {
					fos.write(shard.getContents());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		gp.add(extractFileButton, 1, 2);
		container.getChildren().add(gp);

		if (StringUtils.endsWithAny(shard.getFileName().toLowerCase(), StringUtils.IMAGE_TYPES)) {
			container.getChildren().add(new Separator(Orientation.HORIZONTAL));
			ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(shard.getContents())));
			imageView.setPreserveRatio(true);
			ScrollPane scrollPane = new ScrollPane(imageView);
			container.getChildren().add(scrollPane);
		} else if (StringUtils.endsWithAny(shard.getFileName().toLowerCase(), StringUtils.PLAIN_TEXT_TYPES)) {
			container.getChildren().add(new Separator(Orientation.HORIZONTAL));
			TextArea textArea = new TextArea(new String(shard.getContents()));
			textArea.setEditable(false);
			textArea.setWrapText(true);
			container.getChildren().add(textArea);
		}

		return container;
	}

	@Override
	public String getIconPath() {
		return "/nl/andrewlalis/crystalkeep/ui/images/file_shard_node_icon.png";
	}
}
