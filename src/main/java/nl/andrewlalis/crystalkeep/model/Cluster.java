package nl.andrewlalis.crystalkeep.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
public class Cluster implements Comparable<Cluster>, CrystalItem {
	@Setter
	private String name;
	private final Set<Shard> shards;
	private final Set<Cluster> clusters;
	@Setter
	private Cluster parent;

	public Cluster(String name, Set<Shard> shards, Set<Cluster> clusters, Cluster parent) {
		this.name = name;
		this.shards = shards;
		this.clusters = clusters;
		this.parent = parent;
	}

	public Cluster(String name) {
		this(name, new HashSet<>(), new HashSet<>(), null);
	}

	public void addShard(Shard shard) {
		this.shards.add(shard);
	}

	public void addCluster(Cluster cluster) {
		this.clusters.add(cluster);
		cluster.setParent(this);
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

	@Override
	public int compareTo(Cluster o) {
		return this.getName().compareTo(o.getName());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Cluster cluster = (Cluster) o;
		return getName().equals(cluster.getName()) && Objects.equals(getParent(), cluster.getParent());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName(), getParent());
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

	@Override
	public String getIconPath() {
		return "ui/images/cluster_node_icon.png";
	}
}
