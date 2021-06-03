package nl.andrewlalis.crystalkeep.io.serialization;

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

	public static int readInt(InputStream is) throws IOException {
		return toInt(is.readNBytes(4));
	}

	public static void writeInt(OutputStream os, int value) throws IOException {
		os.write(toBytes(value));
	}

	public static byte[] longToBytes(long l) {
		byte[] result = new byte[8];
		for (int i = 7; i >= 0; i--) {
			result[i] = (byte)(l & 0xFF);
			l >>= 8;
		}
		return result;
	}

	public static long bytesToLong(final byte[] b) {
		long result = 0;
		for (int i = 0; i < 8; i++) {
			result <<= 8;
			result |= (b[i] & 0xFF);
		}
		return result;
	}

	public static void writeLong(long value, OutputStream os) throws IOException {
		os.write(longToBytes(value));
	}

	public static long readLong(InputStream is) throws IOException {
		return bytesToLong(is.readNBytes(Long.BYTES));
	}

	public static void writeLengthPrefixed(byte[] bytes, OutputStream os) throws IOException {
		os.write(toBytes(bytes.length));
		os.write(bytes);
	}

	public static void writeLengthPrefixed(String s, OutputStream os) throws IOException {
		writeLengthPrefixed(s.getBytes(StandardCharsets.UTF_8), os);
	}

	public static void writeLengthPrefixedStrings(String[] strings, OutputStream os) throws IOException {
		for (String s : strings) {
			writeLengthPrefixed(s, os);
		}
	}

	public static byte[] readLengthPrefixed(InputStream is) throws IOException {
		int count = toInt(is.readNBytes(4));
		return is.readNBytes(count);
	}

	public static String readLengthPrefixedString(InputStream is) throws IOException {
		return new String(readLengthPrefixed(is), StandardCharsets.UTF_8);
	}
}
