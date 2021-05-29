package nl.andrewlalis.crystalkeep.model.serialization;

import nl.andrewlalis.crystalkeep.model.Cluster;
import nl.andrewlalis.crystalkeep.model.Shard;
import nl.andrewlalis.crystalkeep.model.shards.LoginCredentialsShard;
import nl.andrewlalis.crystalkeep.model.shards.ShardType;
import nl.andrewlalis.crystalkeep.model.shards.TextShard;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static nl.andrewlalis.crystalkeep.model.serialization.ByteUtils.toInt;

/**
 * This serializer class offers some methods for reading and writing clusters
 * from and to byte arrays.
 */
public class ClusterSerializer {
	private static final Map<ShardType, ShardSerializer<?>> serializers = new HashMap<>();
	static {
		serializers.put(ShardType.TEXT, new TextShard.Serializer());
		serializers.put(ShardType.LOGIN_CREDENTIALS, new LoginCredentialsShard.Serializer());
	}

	/**
	 * Serializes a cluster to a byte array, including all shards and nested
	 * clusters.
	 * @param cluster The cluster to serialize.
	 * @param os The output stream to write to.
	 * @throws IOException If an error occurs while writing the cluster.
	 */
	public static void writeCluster(Cluster cluster, OutputStream os) throws IOException {
		ByteUtils.writeLengthPrefixed(cluster.getName(), os);
		os.write(ByteUtils.toBytes(cluster.getClusters().size()));
		for (Cluster child : cluster.getClustersOrdered()) {
			writeCluster(child, os);
		}
		os.write(ByteUtils.toBytes(cluster.getShards().size()));
		for (Shard shard : cluster.getShardsOrdered()) {
			writeShard(shard, os);
		}
	}

	/**
	 * Reads a cluster from an input stream.
	 * @param is The input stream to read from.
	 * @return The cluster that was read.
	 * @throws IOException If data could not be read from the stream.
	 */
	public static Cluster readCluster(InputStream is) throws IOException {
		String name = ByteUtils.readLengthPrefixedString(is);
		Cluster cluster = new Cluster(name, new HashSet<>(), new HashSet<>());
		int childCount = toInt(is.readNBytes(4));
		for (int i = 0; i < childCount; i++) {
			cluster.addCluster(readCluster(is));
		}
		int shardCount = toInt(is.readNBytes(4));
		for (int i = 0; i < shardCount; i++) {
			cluster.addShard(readShard(is));
		}
		return cluster;
	}

	/**
	 * Serializes a shard to a byte array. It does this by first writing the
	 * standard shard information, then using a specific {@link ShardSerializer}
	 * to get the bytes that represent the body of the shard.
	 * @param shard The shard to serialize.
	 * @param os The output stream to write to.
	 * @throws IOException If byte array stream could not be written to.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static void writeShard(Shard shard, OutputStream os) throws IOException {
		ByteUtils.writeLengthPrefixed(shard.getName().getBytes(StandardCharsets.UTF_8), os);
		ByteUtils.writeLengthPrefixed(shard.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).getBytes(StandardCharsets.UTF_8), os);
		os.write(ByteUtils.toBytes(shard.getType().getValue()));
		ShardSerializer serializer = serializers.get(shard.getType());
		os.write(serializer.serialize(shard));
	}

	/**
	 * Deserializes a shard from a byte array that's being read by the given
	 * input stream.
	 * @param is The input stream to read from.
	 * @return The shard that was deserialized.
	 * @throws IOException If an error occurs while reading bytes.
	 */
	public static Shard readShard(InputStream is) throws IOException {
		String name = ByteUtils.readLengthPrefixedString(is);
		LocalDateTime createdAt = LocalDateTime.parse(ByteUtils.readLengthPrefixedString(is), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		ShardType type = ShardType.valueOf(ByteUtils.toInt(is.readNBytes(4)));
		ShardSerializer<?> serializer = serializers.get(type);
		if (serializer == null) {
			throw new IOException("Unsupported shard type.");
		}
		return serializer.deserialize(is, name, createdAt);
	}
}
