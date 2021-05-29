package nl.andrewlalis.crystalkeep.model;

/**
 * Unifying interface for both clusters and shards and any other possible
 * components in the hierarchy.
 */
public interface CrystalItem {
	String getName();
	String getIconPath();
}
