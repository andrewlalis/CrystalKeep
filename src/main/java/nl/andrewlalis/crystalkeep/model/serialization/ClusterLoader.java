package nl.andrewlalis.crystalkeep.model.serialization;

import nl.andrewlalis.crystalkeep.model.Cluster;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ClusterLoader {
	private static final Path CLUSTER_PATH = Path.of("clusters");
	private static final Path DEFAULT_CLUSTER = CLUSTER_PATH.resolve("default.cts");

	public Cluster loadDefault() throws IOException {
		InputStream is = new FileInputStream(DEFAULT_CLUSTER.toFile());
		return ClusterSerializer.clusterFromBytes(is, null);
	}

	public void saveDefault(Cluster cluster) throws IOException {
		Files.createDirectories(CLUSTER_PATH);
		byte[] bytes = ClusterSerializer.toBytes(cluster);
		Files.write(DEFAULT_CLUSTER, bytes);
	}
}
