package nl.andrewlalis.crystalkeep.util;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This cache stores pre-loaded images that are used in the application, so that
 * they only need to be loaded once.
 */
public class ImageCache {
	private static final Map<ImageSpec, Image> images = new ConcurrentHashMap<>();

	public static Optional<Image> get(String path, double width, double height) {
		ImageSpec spec = new ImageSpec(path, width, height);
		Image img = images.get(spec);
		if (img == null) {
			InputStream is = ImageCache.class.getResourceAsStream(path);
			if (is == null) return Optional.empty();
			img = new Image(is, spec.width, spec.height, true, true);
			images.put(spec, img);
		}
		return Optional.of(img);
	}

	private static class ImageSpec {
		private final String path;
		private final double width;
		private final double height;

		public ImageSpec(String path, double width, double height) {
			this.path = path;
			this.width = width;
			this.height = height;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			ImageSpec imageSpec = (ImageSpec) o;
			return Double.compare(imageSpec.width, width) == 0 && Double.compare(imageSpec.height, height) == 0 && path.equals(imageSpec.path);
		}

		@Override
		public int hashCode() {
			return Objects.hash(path, width, height);
		}
	}
}
