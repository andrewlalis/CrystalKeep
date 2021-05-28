package nl.andrewlalis.crystalkeep.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashSet;

public class ClusterSerializer {
	public byte[] toBytes(Cluster cluster) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bos.write(this.toBytes(cluster.getClusters().size()));
		for (Cluster child : cluster.getClusters()) {
			bos.write(this.toBytes(child));
		}
		bos.write(this.toBytes(cluster.getShards().size()));
		for (Shard shard : cluster.getShards()) {
			bos.write(shard.toBytes());
		}
		return bos.toByteArray();
	}

	public Cluster fromBytes(ByteArrayInputStream bis, Cluster parent) throws IOException {
		Cluster cluster = new Cluster(new HashSet<>(), new HashSet<>(), parent);
		int childCount = this.toInt(bis.readNBytes(4));
		for (int i = 0; i < childCount; i++) {
			cluster.addCluster(this.fromBytes(bis, cluster));
		}
		int shardCount = this.toInt(bis.readNBytes(4));
		for (int i = 0; i < shardCount; i++) {
			cluster.addShard(Shard.fromBytes(bis));
		}
		return cluster;
	}

	public byte[] toBytes(int x) {
		return ByteBuffer.allocate(4).putInt(x).array();
	}

	public int toInt(byte[] bytes) {
		assert(bytes.length == 4);
		return ByteBuffer.wrap(bytes).getInt();
	}
}
