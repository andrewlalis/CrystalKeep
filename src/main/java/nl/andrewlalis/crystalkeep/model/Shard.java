package nl.andrewlalis.crystalkeep.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A shard is a single "piece" of information, such as a snippet of text, login
 * credentials, an image, or a private key.
 * <p>
 *     Due to the need to deserialize shards from byte arrays, it is required
 *     that this parent class holds a type discriminator value, which is used to
 *     decide which shard type to deserialize.
 * </p>
 */
public abstract class Shard implements Comparable<Shard>, CrystalItem {
	private String name;
	private final LocalDateTime createdAt;
	private final ShardType type;

	public Shard(String name, LocalDateTime createdAt, ShardType type) {
		this.name = name;
		this.createdAt = createdAt;
		this.type = type;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public ShardType getType() {
		return type;
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
		return getName().equals(shard.getName()) && getType() == shard.getType() && getCreatedAt().equals(shard.getCreatedAt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName(), getType());
	}

	@Override
	public String toString() {
		return "Shard: name=\"" + this.name + "\", type=" + this.type + ", createdAt=" + this.createdAt;
	}
}
