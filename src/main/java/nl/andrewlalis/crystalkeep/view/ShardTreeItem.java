package nl.andrewlalis.crystalkeep.view;

import javafx.scene.control.TreeItem;
import nl.andrewlalis.crystalkeep.model.CrystalItem;
import nl.andrewlalis.crystalkeep.model.Shard;

public class ShardTreeItem extends TreeItem<CrystalItem> {
	private final Shard shard;

	public ShardTreeItem(Shard shard) {
		super(shard);
		this.shard = shard;
	}

	public Shard getShard() {
		return shard;
	}
}
