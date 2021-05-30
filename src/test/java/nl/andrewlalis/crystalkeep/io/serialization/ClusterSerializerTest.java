package nl.andrewlalis.crystalkeep.io.serialization;

import nl.andrewlalis.crystalkeep.model.Cluster;
import nl.andrewlalis.crystalkeep.model.Shard;
import nl.andrewlalis.crystalkeep.model.shards.LoginCredentialsShard;
import nl.andrewlalis.crystalkeep.model.shards.TextShard;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the functionality of the {@link ClusterSerializer}, which handles
 * transforming key model components to and from byte arrays for storage.
 */
public class ClusterSerializerTest {
	public static List<Shard> testShardIOData() {
		return List.of(
				new TextShard("a", LocalDateTime.now(), "Hello world!"),
				new TextShard("Another", LocalDateTime.now(), "Testing"),
				new TextShard("", LocalDateTime.now(), ""),
				new LoginCredentialsShard("login", LocalDateTime.now(), "andrew", "password"),
				new LoginCredentialsShard("test", LocalDateTime.now(), "", "")
		);
	}

	@ParameterizedTest
	@MethodSource("testShardIOData")
	public void testShardIO(Shard s1) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ClusterSerializer.writeShard(s1, bos);
		byte[] data = bos.toByteArray();
		assertNotEquals(0, data.length, "Serialized shard should never result in empty bytes.");
		assertEquals(
				s1.getName().length(),
				ByteUtils.toInt(Arrays.copyOfRange(data, 0, 4)),
				"Serialized shard name length does not match expected."
		);
		assertEquals(
				s1.getName(),
				new String(Arrays.copyOfRange(data, 4, 4 + s1.getName().length())),
				"Shard name does not match expected."
		);

		Shard loadedS1 = ClusterSerializer.readShard(new ByteArrayInputStream(data));
		assertEquals(s1, loadedS1, "Loaded shard should equal original shard.");
		bos.reset();
		ClusterSerializer.writeShard(loadedS1, bos);
		byte[] data2 = bos.toByteArray();
		assertArrayEquals(data, data2, "Serialized data from a shard should not change.");
	}

	public static List<Cluster> testClusterIOData() {
		List<Cluster> clusters = new ArrayList<>();
		clusters.add(new Cluster("test"));

		Cluster c1 = new Cluster("c1");
		c1.addCluster(new Cluster("c2"));
		clusters.add(c1);

		Cluster c2 = new Cluster("c2");
		c2.addShard(new TextShard("testing", LocalDateTime.now(), "Hello world!"));
		clusters.add(c2);

		Cluster c3 = new Cluster("c3");
		Cluster c3Nested = new Cluster("nested");
		c3Nested.addShard(new LoginCredentialsShard("login", LocalDateTime.now(), "andrew", "secret password"));
		c3.addCluster(c3Nested);
		clusters.add(c3);
		return clusters;
	}

	@ParameterizedTest
	@MethodSource("testClusterIOData")
	public void testClusterIO(Cluster c) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ClusterSerializer.writeCluster(c, bos);
		byte[] data = bos.toByteArray();
		assertNotEquals(0, data.length, "Serialized cluster should never result in empty bytes.");
		assertEquals(
				c.getName().length(),
				ByteUtils.toInt(Arrays.copyOfRange(data, 0, 4)),
				"Serialized cluster name length does not match expected."
		);
		assertEquals(
				c.getName(),
				new String(Arrays.copyOfRange(data, 4, 4 + c.getName().length())),
				"Cluster name does not match expected."
		);

		Cluster loaded = ClusterSerializer.readCluster(new ByteArrayInputStream(data));
		assertEquals(c, loaded, "Loaded cluster should equal original cluster.");
		bos.reset();
		ClusterSerializer.writeCluster(loaded, bos);
		byte[] data2 = bos.toByteArray();
		assertArrayEquals(data, data2, "Serialized cluster should not change.");
	}
}
