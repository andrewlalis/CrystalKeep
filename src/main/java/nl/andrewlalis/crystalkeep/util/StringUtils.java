package nl.andrewlalis.crystalkeep.util;

public class StringUtils {
	public static final String[] IMAGE_TYPES = {
		"png", "jpg", "jpeg", "gif"
	};
	public static final String[] PLAIN_TEXT_TYPES = {
		"txt"
	};

	public static boolean endsWithAny(String s, String... suffixes) {
		for (String suffix : suffixes) {
			if (s.endsWith(suffix)) return true;
		}
		return false;
	}
}
