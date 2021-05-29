package nl.andrewlalis.crystalkeep;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.andrewlalis.crystalkeep.control.MainViewController;
import nl.andrewlalis.crystalkeep.model.Cluster;
import nl.andrewlalis.crystalkeep.model.Model;
import nl.andrewlalis.crystalkeep.model.serialization.ClusterLoader;
import nl.andrewlalis.crystalkeep.model.shards.LoginCredentialsShard;
import nl.andrewlalis.crystalkeep.model.shards.TextShard;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;

public class CrystalKeep extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		URL url = getClass().getResource("/nl/andrewlalis/crystalkeep/ui/crystalkeep.fxml");
		FXMLLoader loader = new FXMLLoader(url);
		Model model = new Model();
		var scene = new Scene(loader.load());
		stage.setScene(scene);
		stage.setTitle("CrystalKeep");
		stage.sizeToScene();
		model.setActiveCluster(this.loadRootCluster());

		stage.show();

		MainViewController controller = loader.getController();
		controller.init(model);
	}

	private Cluster loadRootCluster() throws IOException {
		ClusterLoader clusterLoader = new ClusterLoader();
		Cluster rootCluster;
		try {
			rootCluster = clusterLoader.loadDefault();
			System.out.println("Loaded existing root cluster.");
		} catch (IOException e) {
			rootCluster = new Cluster("Root");
			rootCluster.addShard(new TextShard("Example Shard", LocalDateTime.now(), "Hello world!"));
			rootCluster.addShard(new LoginCredentialsShard("Netflix", LocalDateTime.now(), "user", "secret password"));
			for (int i = 0; i < 100; i++) {
				rootCluster.addShard(new TextShard("test " + i, LocalDateTime.now(), "value: " + i));
			}
			clusterLoader.saveDefault(rootCluster);
			System.out.println("Saved root cluster on first load.");
		}
		return rootCluster;
	}
}
