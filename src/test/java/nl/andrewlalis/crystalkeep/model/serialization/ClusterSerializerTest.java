package nl.andrewlalis.crystalkeep.model.serialization;

import nl.andrewlalis.crystalkeep.model.Shard;
import nl.andrewlalis.crystalkeep.model.shards.LoginCredentialsShard;
import nl.andrewlalis.crystalkeep.model.shards.TextShard;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClusterSerializerTest {
	private static List<Shard> testShardIOData() {
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
				"First letter of shard name does not match expected."
		);

		Shard loadedS1 = ClusterSerializer.readShard(new ByteArrayInputStream(data));
		assertEquals(s1, loadedS1, "Loaded shard should equal original shard.");
		bos.reset();
		ClusterSerializer.writeShard(loadedS1, bos);
		byte[] data2 = bos.toByteArray();
		assertArrayEquals(data, data2, "Serialized data from a shard should not change.");
	}
}
