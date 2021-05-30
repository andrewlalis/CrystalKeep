package nl.andrewlalis.crystalkeep;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.andrewlalis.crystalkeep.control.MainViewController;
import nl.andrewlalis.crystalkeep.model.Model;

import java.net.URL;

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
		model.setActiveCluster(null); // TODO: Auto load last cluster?
		stage.show();
		MainViewController controller = loader.getController();
		controller.init(model);
	}
}
