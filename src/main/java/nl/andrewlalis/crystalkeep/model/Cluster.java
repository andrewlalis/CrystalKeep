package nl.andrewlalis.crystalkeep.model;

import lombok.Getter;

import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Set;

@Getter
public class Cluster {
	private final Set<Shard> shards;
	private final Set<Cluster> clusters;
	private final Cluster parent;

	public Cluster(Set<Shard> shards, Set<Cluster> clusters, Cluster parent) {
		this.shards = shards;
		this.clusters = clusters;
		this.parent = parent;
		if (this.parent != null) {
			this.parent.addCluster(this);
		}
	}

	public Cluster() {
		this(new HashSet<>(), new HashSet<>(), null);
	}

	public void addShard(Shard shard) {
		if (!this.equals(shard.getCluster())) {
			throw new IllegalArgumentException("Shard must have correct cluster set before adding it to shard.");
		}
		this.shards.add(shard);
	}

	public void addCluster(Cluster cluster) {
		if (!this.equals(cluster.getParent())) {
			throw new IllegalArgumentException("Cluster must have correct parent set before adding it to cluster.");
		}
		this.clusters.add(cluster);
	}
}
