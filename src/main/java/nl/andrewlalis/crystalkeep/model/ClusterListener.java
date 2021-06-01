package nl.andrewlalis.crystalkeep.model;

public interface ClusterListener {
	default void clusterAdded(Cluster cluster) {}
	default void clusterRemoved(Cluster cluster) {}
	default void shardAdded(Shard shard) {}
	default void shardRemoved(Shard shard) {}
}
