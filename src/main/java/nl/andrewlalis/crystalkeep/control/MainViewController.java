package nl.andrewlalis.crystalkeep.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import nl.andrewlalis.crystalkeep.model.Model;
import nl.andrewlalis.crystalkeep.model.serialization.ClusterLoader;

import java.io.IOException;

public class MainViewController {
	private final Model model;

	public MainViewController(Model model) {
		this.model = model;
	}

	@FXML
	public void exit(ActionEvent event) {
		System.out.println("Exiting...");
		try {
			new ClusterLoader().saveDefault(model.getActiveCluster());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
