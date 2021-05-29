package nl.andrewlalis.crystalkeep;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.andrewlalis.crystalkeep.control.MainViewController;
import nl.andrewlalis.crystalkeep.model.Cluster;
import nl.andrewlalis.crystalkeep.model.Model;
import nl.andrewlalis.crystalkeep.model.Shard;
import nl.andrewlalis.crystalkeep.model.serialization.ClusterLoader;
import nl.andrewlalis.crystalkeep.model.serialization.ClusterSerializer;
import nl.andrewlalis.crystalkeep.model.shards.LoginCredentialsShard;
import nl.andrewlalis.crystalkeep.model.shards.TextShard;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;

public class CrystalKeep extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		URL url = CrystalKeep.class.getClassLoader().getResource("ui/crystalkeep.fxml");
		FXMLLoader loader = new FXMLLoader(url);
		Model model = new Model();
		var scene = new Scene(loader.load());
		stage.setScene(scene);
		stage.setTitle("CrystalKeep");
		stage.sizeToScene();

		ClusterLoader clusterLoader = new ClusterLoader();
		Cluster rootCluster;
		try {
			rootCluster = clusterLoader.loadDefault();
			System.out.println("Loaded existing root cluster.");
		} catch (IOException e) {
			rootCluster = new Cluster("Root");
			rootCluster.addShard(new TextShard(rootCluster, "Example Shard", LocalDateTime.now(), "Hello world!"));
			rootCluster.addShard(new LoginCredentialsShard(rootCluster, "Netflix", LocalDateTime.now(), "user", "secret password"));
			for (int i = 0; i < 100; i++) {
				rootCluster.addShard(new TextShard(rootCluster, "test " + i, LocalDateTime.now(), "value: " + i));
			}
			clusterLoader.saveDefault(rootCluster);
			System.out.println("Saved root cluster on first load.");
		}
		model.setActiveCluster(rootCluster);

		stage.show();

		MainViewController controller = loader.getController();
		controller.init(model);
		System.out.println(rootCluster);
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
