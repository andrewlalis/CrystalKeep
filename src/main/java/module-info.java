module crystalkeep {
	requires javafx.fxml;
	requires javafx.controls;

	opens nl.andrewlalis.crystalkeep.control;
	opens nl.andrewlalis.crystalkeep;

	exports nl.andrewlalis.crystalkeep.control to javafx.fxml;
	exports nl.andrewlalis.crystalkeep.model to javafx.fxml;
}