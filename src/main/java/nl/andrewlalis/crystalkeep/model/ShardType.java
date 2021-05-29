package nl.andrewlalis.crystalkeep.model;

import nl.andrewlalis.crystalkeep.model.shards.LoginCredentialsShard;
import nl.andrewlalis.crystalkeep.model.shards.TextShard;

/**
 * Represents a distinct type of shard, and should correspond to exactly one
 * subtype of {@link Shard}.
 * <p>
 *     All types defined here should have a unique integer value, which is used
 *     for serialization and deserialization of shards.
 * </p>
 */
public enum ShardType {
	/**
	 * Represents a {@link TextShard}
	 */
	TEXT(1),

	/**
	 * Represents a {@link LoginCredentialsShard}
	 */
	LOGIN_CREDENTIALS(2);

	private final int value;

	ShardType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static ShardType valueOf(int value) {
		for (var type : values()) {
			if (type.getValue() == value) return type;
		}
		throw new IllegalArgumentException("Invalid value.");
	}
}
