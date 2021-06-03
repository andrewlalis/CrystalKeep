package nl.andrewlalis.crystalkeep.util;

public class StringUtils {
	public static boolean endsWithAny(String s, String... suffixes) {
		for (String suffix : suffixes) {
			if (s.endsWith(suffix)) return true;
		}
		return false;
	}
}
