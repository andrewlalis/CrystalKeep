package nl.andrewlalis.crystalkeep.model.shards;

import nl.andrewlalis.crystalkeep.model.Cluster;
import nl.andrewlalis.crystalkeep.model.Shard;

import java.time.LocalDateTime;

public class TextShard extends Shard {
	private String text;

	public TextShard(Cluster cluster, String name, LocalDateTime createdAt, String text) {
		super(cluster, name, createdAt);
		this.text = text;
	}
}
