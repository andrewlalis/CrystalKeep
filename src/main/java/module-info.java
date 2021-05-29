module crystalkeep {
	requires javafx.fxml;
	requires javafx.controls;

	opens nl.andrewlalis.crystalkeep;

	exports nl.andrewlalis.crystalkeep.control to javafx.fxml;
}