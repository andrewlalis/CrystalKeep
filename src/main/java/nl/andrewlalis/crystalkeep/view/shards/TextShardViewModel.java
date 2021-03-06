package nl.andrewlalis.crystalkeep.view.shards;

import javafx.scene.Node;
import javafx.scene.control.TextArea;
import nl.andrewlalis.crystalkeep.model.shards.TextShard;

public class TextShardViewModel extends ShardViewModel<TextShard> {
	public TextShardViewModel(TextShard shard) {
		super(shard);
	}

	@Override
	protected Node getContent(TextShard shard) {
		var textArea = new TextArea(shard.getText());
		textArea.setWrapText(true);
		textArea.textProperty().addListener((observable, oldValue, newValue) -> {
			shard.setText(newValue);
		});
		return textArea;
	}

	@Override
	public String getIconPath() {
		return "/nl/andrewlalis/crystalkeep/ui/images/text_shard_node_icon.png";
	}
}
