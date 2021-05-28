package nl.andrewlalis.crystalkeep.model.serialization;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ByteUtils {
	public static byte[] toBytes(int x) {
		return ByteBuffer.allocate(4).putInt(x).array();
	}

	public static int toInt(byte[] bytes) {
		assert(bytes.length == 4);
		return ByteBuffer.wrap(bytes).getInt();
	}

	public static void writeLengthPrefixed(byte[] bytes, OutputStream os) throws IOException {
		os.write(toBytes(bytes.length));
		os.write(bytes);
	}

	public static void writeLengthPrefixed(String s, OutputStream os) throws IOException {
		writeLengthPrefixed(s.getBytes(StandardCharsets.UTF_8), os);
	}

	public static byte[] writeLengthPrefixedStrings(String[] strings) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		for (String s : strings) {
			writeLengthPrefixed(s, bos);
		}
		return bos.toByteArray();
	}

	public static byte[] readLengthPrefixed(InputStream is) throws IOException {
		int count = toInt(is.readNBytes(4));
		return is.readNBytes(count);
	}

	public static String readLengthPrefixedString(InputStream is) throws IOException {
		return new String(readLengthPrefixed(is), StandardCharsets.UTF_8);
	}
}
