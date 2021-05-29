package nl.andrewlalis.crystalkeep.model;

import lombok.Getter;
import lombok.Setter;
import nl.andrewlalis.crystalkeep.model.shards.ShardType;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A shard is a single "piece" of information, such as a snippet of text, login
 * credentials, an image, or a private key. All shards within a cluster should
 * have unique names.
 * <p>
 *     Due to the need to deserialize shards from byte arrays, it is required
 *     that this parent class holds a type discriminator value, which is used to
 *     decide which shard type to deserialize.
 * </p>
 */
@Getter
public abstract class Shard implements Comparable<Shard>, CrystalItem {
	@Setter
	private Cluster cluster;
	@Setter
	private String name;
	private final LocalDateTime createdAt;
	private final ShardType type;

	public Shard(Cluster cluster, String name, LocalDateTime createdAt, ShardType type) {
		this.cluster = cluster;
		this.name = name;
		this.createdAt = createdAt;
		this.type = type;
	}

	@Override
	public int compareTo(Shard o) {
		int r = this.getName().compareTo(o.getName());
		if (r != 0) return r;
		return this.getCreatedAt().compareTo(o.getCreatedAt());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Shard shard = (Shard) o;
		return getCluster().equals(shard.getCluster()) && getName().equals(shard.getName()) && getType() == shard.getType();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCluster(), getName(), getType());
	}

	@Override
	public String toString() {
		return "Shard: name=\"" + this.name + "\", type=" + this.type + ", createdAt=" + this.createdAt;
	}

	@Override
	public String getIconPath() {
		return "ui/images/shard_node_icon.png";
	}
}
