package nl.andrewlalis.crystalkeep.model.serialization;

import nl.andrewlalis.crystalkeep.model.Cluster;
import nl.andrewlalis.crystalkeep.model.Shard;
import nl.andrewlalis.crystalkeep.model.shards.LoginCredentialsShard;
import nl.andrewlalis.crystalkeep.model.shards.ShardType;
import nl.andrewlalis.crystalkeep.model.shards.TextShard;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
	 * TODO: Use output stream instead of byte array.
	 * @param cluster The cluster to serialize.
	 * @return The byte array representing the cluster.
	 * @throws IOException If an error occurs while writing the cluster.
	 */
	public static byte[] toBytes(Cluster cluster) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ByteUtils.writeLengthPrefixed(cluster.getName(), bos);

		bos.write(ByteUtils.toBytes(cluster.getClusters().size()));
		Cluster[] children = new Cluster[cluster.getClusters().size()];
		cluster.getClusters().toArray(children);
		Arrays.sort(children);
		for (Cluster child : children) {
			bos.write(toBytes(child));
		}

		bos.write(ByteUtils.toBytes(cluster.getShards().size()));
		Shard[] shards = new Shard[cluster.getShards().size()];
		cluster.getShards().toArray(shards);
		Arrays.sort(shards);
		for (Shard shard : shards) {
			bos.write(toBytes(shard));
		}
		return bos.toByteArray();
	}

	/**
	 * Reads a cluster from an input stream.
	 * @param is The input stream to read from.
	 * @param parent The parent cluster of this cluster. This may be null.
	 * @return The cluster that was read.
	 * @throws IOException If data could not be read from the stream.
	 */
	public static Cluster clusterFromBytes(InputStream is, Cluster parent) throws IOException {
		String name = ByteUtils.readLengthPrefixedString(is);
		Cluster cluster = new Cluster(name, new HashSet<>(), new HashSet<>(), parent);
		int childCount = toInt(is.readNBytes(4));
		for (int i = 0; i < childCount; i++) {
			cluster.addCluster(clusterFromBytes(is, cluster));
		}
		int shardCount = toInt(is.readNBytes(4));
		for (int i = 0; i < shardCount; i++) {
			cluster.addShard(shardFromBytes(is, cluster));
		}
		return cluster;
	}

	/**
	 * Serializes a shard to a byte array. It does this by first writing the
	 * standard shard information, then using a specific {@link ShardSerializer}
	 * to get the bytes that represent the body of the shard.
	 * @param shard The shard to serialize.
	 * @return A byte array representing the shard.
	 * @throws IOException If byte array stream could not be written to.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static byte[] toBytes(Shard shard) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ByteUtils.writeLengthPrefixed(shard.getName().getBytes(StandardCharsets.UTF_8), bos);
		ByteUtils.writeLengthPrefixed(shard.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).getBytes(StandardCharsets.UTF_8), bos);
		bos.write(ByteUtils.toBytes(shard.getType().getValue()));
		ShardSerializer serializer = serializers.get(shard.getType());
		bos.write(serializer.serialize(shard));
		return bos.toByteArray();
	}

	/**
	 * Deserializes a shard from a byte array that's being read by the given
	 * input stream.
	 * @param is The input stream to read from.
	 * @param cluster The cluster that the shard should belong to.
	 * @return The shard that was deserialized.
	 * @throws IOException If an error occurs while reading bytes.
	 */
	public static Shard shardFromBytes(InputStream is, Cluster cluster) throws IOException {
		String name = ByteUtils.readLengthPrefixedString(is);
		LocalDateTime createdAt = LocalDateTime.parse(ByteUtils.readLengthPrefixedString(is), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		ShardType type = ShardType.valueOf(ByteUtils.toInt(is.readNBytes(4)));
		ShardSerializer<?> serializer = serializers.get(type);
		if (serializer == null) {
			throw new IOException("Unsupported shard type.");
		}
		return serializer.deserialize(is, cluster, name, createdAt);
	}
}
