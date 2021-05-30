package nl.andrewlalis.crystalkeep.io;

import nl.andrewlalis.crystalkeep.model.Cluster;
import nl.andrewlalis.crystalkeep.model.shards.LoginCredentialsShard;
import nl.andrewlalis.crystalkeep.model.shards.TextShard;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests the functionality of the {@link ClusterIO} class in loading and saving
 * clusters.
 */
public class ClusterIOTest {
	public static List<Cluster> testClusters() {
		List<Cluster> clusters = new ArrayList<>();
		clusters.add(new Cluster("Root"));
		Cluster c1 = new Cluster("C1");
		c1.addShard(new TextShard("text", LocalDateTime.now(), "hello world!"));
		clusters.add(c1);
		Cluster c2 = new Cluster("C2");
		Cluster c2Nested = new Cluster("Nested");
		c2Nested.addShard(new LoginCredentialsShard("login", LocalDateTime.now(), "andrew", "pass"));
		c2Nested.addShard(new TextShard("data", LocalDateTime.now(), "test, 1, 2, 3"));
		c2.addCluster(c2Nested);
		c2.addShard(new TextShard("more_data", LocalDateTime.now(), "Antidisestablishmentarianism"));
		clusters.add(c2);
		return clusters;
	}

	@ParameterizedTest
	@MethodSource("testClusters")
	public void testUnencryptedIO(Cluster cluster) throws IOException, GeneralSecurityException {
		Path file = Files.createTempFile(UUID.randomUUID().toString(), "cts");
		var io = new ClusterIO();
		// Test normal save and load.
		io.saveUnencrypted(cluster, file);
		Cluster loaded = io.loadUnencrypted(file);
		assertEquals(cluster, loaded);
		// Test attempting to load an encrypted file, which throws an IOException.
		io.save(cluster, file, "testpass");
		assertThrows(IOException.class, () -> io.loadUnencrypted(file));
	}

	@ParameterizedTest
	@MethodSource("testClusters")
	public void testEncryptedIO(Cluster cluster) throws Exception {
		Path file = Files.createTempFile(UUID.randomUUID().toString(), "cts");
		var io = new ClusterIO();
		// Test normal save and load.
		io.save(cluster, file, "test");
		Cluster loaded = io.load(file, "test");
		assertEquals(cluster, loaded);
		// Test attempting to load an unencrypted file, which throws an IOException.
		io.saveUnencrypted(cluster, file);
		assertThrows(IOException.class, () -> io.load(file, "test"));
		// Test attempting to load an encrypted file with the wrong password. An exception is thrown when reading it.
		io.save(cluster, file, "other");
		assertThrows(Exception.class, () -> io.load(file, "not_password"));
	}
}
