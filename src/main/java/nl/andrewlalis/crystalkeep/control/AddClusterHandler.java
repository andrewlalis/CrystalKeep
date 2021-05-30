package nl.andrewlalis.crystalkeep.control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import nl.andrewlalis.crystalkeep.model.Cluster;
import nl.andrewlalis.crystalkeep.model.Model;

public class AddClusterHandler implements EventHandler<ActionEvent> {
	private final Cluster cluster;
	private final Model model;

	public AddClusterHandler(Cluster cluster, Model model) {
		this.cluster = cluster;
		this.model = model;
	}

	@Override
	public void handle(ActionEvent event) {
		Dialog<String> d = new TextInputDialog();
		d.setContentText("Enter the name of the new cluster.");
		d.setHeaderText(null);
		d.setTitle("Add Cluster");
		d.setGraphic(null);
		d.showAndWait().ifPresent(s -> {
			cluster.addCluster(new Cluster(s.trim()));
			model.notifyListeners();
		});
	}
}
