package nl.andrewlalis.crystalkeep.view.shard_details;

import javafx.scene.Node;
import javafx.scene.control.TextArea;
import nl.andrewlalis.crystalkeep.model.shards.TextShard;

public class TextShardPane extends ShardPane<TextShard> {
	public TextShardPane(TextShard shard) {
		super(shard);
	}

	@Override
	protected Node getContent(TextShard shard) {
		var textArea = new TextArea(shard.getText());
		textArea.textProperty().addListener((observable, oldValue, newValue) -> {
			shard.setText(newValue);
		});
		return textArea;
	}
}
