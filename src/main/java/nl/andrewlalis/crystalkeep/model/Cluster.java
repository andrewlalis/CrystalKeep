package nl.andrewlalis.crystalkeep.model;

import java.util.*;

public class Cluster implements Comparable<Cluster>, CrystalItem {
	private String name;
	private final Set<Shard> shards;
	private final Set<Cluster> clusters;

	private Cluster parent;
	private final Set<ClusterListener> listeners = new HashSet<>();

	public Cluster(String name, Set<Shard> shards, Set<Cluster> clusters) {
		this.name = name;
		this.shards = shards;
		this.clusters = clusters;
	}

	public Cluster(String name) {
		this(name, new HashSet<>(), new HashSet<>());
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Shard> getShards() {
		return shards;
	}

	public Set<Cluster> getClusters() {
		return clusters;
	}

	public void addShard(Shard shard) {
		this.shards.add(shard);
		shard.setParent(this);
		this.listeners.forEach(l -> l.shardAdded(shard));
	}

	public void removeShard(Shard shard) {
		this.shards.remove(shard);
		shard.setParent(null);
		this.listeners.forEach(l -> l.shardRemoved(shard));
	}

	public void addCluster(Cluster cluster) {
		this.clusters.add(cluster);
		cluster.setParent(this);
		this.listeners.forEach(l -> l.clusterAdded(cluster));
	}

	public void removeCluster(Cluster cluster) {
		this.clusters.remove(cluster);
		cluster.setParent(null);
		this.listeners.forEach(l -> l.clusterRemoved(cluster));
	}

	public List<Cluster> getClustersOrdered() {
		List<Cluster> clusters = new ArrayList<>(this.getClusters());
		clusters.sort(Comparator.naturalOrder());
		return clusters;
	}

	public List<Shard> getShardsOrdered() {
		List<Shard> shards = new ArrayList<>(this.getShards());
		shards.sort(Comparator.naturalOrder());
		return shards;
	}

	public Cluster getParent() {
		return parent;
	}

	public void setParent(Cluster parent) {
		this.parent = parent;
	}

	public void addListener(ClusterListener listener) {
		this.listeners.add(listener);
	}

	public void removeListener(ClusterListener listener) {
		this.listeners.remove(listener);
	}

	@Override
	public int compareTo(Cluster o) {
		return this.getName().compareTo(o.getName());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Cluster cluster = (Cluster) o;
		return getName().equals(cluster.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Cluster: name=\"").append(this.getName()).append("\"\nShards:\n");
		for (Shard s : this.getShards()) {
			sb.append('\t').append(s.toString()).append('\n');
		}
		sb.append("Nested clusters:\n");
		for (Cluster c : this.getClusters()) {
			sb.append('\n').append(c.toString()).append('\n');
		}
		return sb.toString();
	}
}
