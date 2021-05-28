package nl.andrewlalis.crystalkeep.model;

import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;

@Getter
public abstract class Shard implements Comparable<Shard> {
	private final Cluster cluster;
	@Setter
	private String name;
	private final LocalDateTime createdAt;

	public Shard(Cluster cluster, String name, LocalDateTime createdAt) {
		this.cluster = cluster;
		this.cluster.addShard(this);
		this.name = name;
		this.createdAt = createdAt;
	}

	@Override
	public int compareTo(Shard o) {
		int r = this.getName().compareTo(o.getName());
		if (r != 0) return r;
		return this.getCreatedAt().compareTo(o.getCreatedAt());
	}

	public byte[] toBytes() {
		return new byte[0];
	}

	public static Shard fromBytes(ByteArrayInputStream bis) {
		return null;
	}
}
