package nl.andrewlalis.crystalkeep;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.andrewlalis.crystalkeep.model.Cluster;
import nl.andrewlalis.crystalkeep.model.Shard;
import nl.andrewlalis.crystalkeep.model.serialization.ClusterSerializer;
import nl.andrewlalis.crystalkeep.model.shards.LoginCredentialsShard;
import nl.andrewlalis.crystalkeep.model.shards.TextShard;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;

public class CrystalKeep extends Application {
	public static void main(String[] args) throws IOException {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		URL url = CrystalKeep.class.getClassLoader().getResource("ui/crystalkeep.fxml");
		FXMLLoader loader = new FXMLLoader(url);
		var scene = new Scene(loader.load());
		stage.setScene(scene);
		stage.setTitle("CrystalKeep");
		stage.sizeToScene();
		stage.show();
	}

	public static void test() throws IOException {
		Cluster c = new Cluster("Test");
		Shard s = new TextShard(c, "sample", LocalDateTime.now(), "Hello world!");
		Shard s2 = new LoginCredentialsShard(c, "logs", LocalDateTime.now().plusHours(3), "andrew", "testing");
		c.addShard(s);
		c.addShard(s2);
		Cluster c2 = new Cluster("Test2");
		Shard s3 = new TextShard(c2, "another sample", LocalDateTime.now().plusMinutes(3), "Testing this stuff....");
		c2.addShard(s3);
		Cluster parent = new Cluster("Parent");
		parent.addCluster(c);
		parent.addCluster(c2);

		System.out.println(parent);
		long start = System.currentTimeMillis();
		byte[] cBytes = ClusterSerializer.toBytes(parent);
		long dur = System.currentTimeMillis() - start;
		System.out.println("Duration: " + dur + " milliseconds");
		System.out.println(Arrays.toString(cBytes));

		Cluster cLoaded = ClusterSerializer.clusterFromBytes(new ByteArrayInputStream(cBytes), null);
		System.out.println(cLoaded);
		System.out.println(Arrays.toString(ClusterSerializer.toBytes(cLoaded)));
		System.out.println(Arrays.equals(cBytes, ClusterSerializer.toBytes(cLoaded)));
	}
}
