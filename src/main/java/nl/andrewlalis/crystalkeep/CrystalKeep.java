package nl.andrewlalis.crystalkeep;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import nl.andrewlalis.crystalkeep.control.MainViewController;
import nl.andrewlalis.crystalkeep.model.Cluster;
import nl.andrewlalis.crystalkeep.model.Model;
import nl.andrewlalis.crystalkeep.model.serialization.ClusterLoader;
import nl.andrewlalis.crystalkeep.model.shards.LoginCredentialsShard;
import nl.andrewlalis.crystalkeep.model.shards.TextShard;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Optional;

public class CrystalKeep extends Application {
	public static void main(String[] args) throws GeneralSecurityException {
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
		model.setActiveCluster(null);

		stage.show();

		MainViewController controller = loader.getController();
		controller.init(model);
	}

//	private Cluster loadRootCluster() throws IOException, GeneralSecurityException {
//		ClusterLoader clusterLoader = new ClusterLoader();
//		Cluster rootCluster;
//
////		Optional<Cluster> oc = clusterLoader.load(ClusterLoader.DEFAULT_CLUSTER);
////		if (oc.isEmpty()) {
////			new Alert(Alert.AlertType.ERROR, "Could not load cluster.").show();
////			System.exit(1);
////		}
////		return oc.get();
//
////		try {
////			long start = System.currentTimeMillis();
////			rootCluster = clusterLoader.load(ClusterLoader.DEFAULT_CLUSTER);
////			long dur = System.currentTimeMillis() - start;
////			System.out.println("Loaded existing root cluster in " + dur + " ms.");
////		} catch (Exception e) {
////			e.printStackTrace();
////			rootCluster = new Cluster("Root");
////			rootCluster.addShard(new TextShard("Example Shard", LocalDateTime.now(), "Hello world!"));
////			rootCluster.addShard(new LoginCredentialsShard("Netflix", LocalDateTime.now(), "user", "secret password"));
////			for (int i = 0; i < 100; i++) {
////				rootCluster.addShard(new TextShard("test " + i, LocalDateTime.now(), "value: " + i));
////			}
////			clusterLoader.save(rootCluster, ClusterLoader.DEFAULT_CLUSTER, "test");
////			System.out.println("Saved root cluster on first load.");
////		}
////		return rootCluster;
//	}
}
