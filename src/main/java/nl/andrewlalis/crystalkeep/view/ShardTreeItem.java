package nl.andrewlalis.crystalkeep.view;

import javafx.scene.control.TreeItem;
import lombok.Getter;
import nl.andrewlalis.crystalkeep.model.CrystalItem;
import nl.andrewlalis.crystalkeep.model.Shard;

public class ShardTreeItem extends TreeItem<CrystalItem> {
	@Getter
	private final Shard shard;

	public ShardTreeItem(Shard shard) {
		super(shard);
		this.shard = shard;
	}
}
