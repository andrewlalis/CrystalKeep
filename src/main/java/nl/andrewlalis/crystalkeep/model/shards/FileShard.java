package nl.andrewlalis.crystalkeep.model.shards;

import nl.andrewlalis.crystalkeep.io.serialization.ByteUtils;
import nl.andrewlalis.crystalkeep.io.serialization.ShardSerializer;
import nl.andrewlalis.crystalkeep.model.Shard;
import nl.andrewlalis.crystalkeep.model.ShardType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;

public class FileShard extends Shard {
	private String fileName;
	private byte[] contents;

	public FileShard(String name, LocalDateTime createdAt, String fileName, byte[] contents) {
		super(name, createdAt, ShardType.FILE);
		this.fileName = fileName;
		this.contents = contents;
	}

	public FileShard(String name, String fileName, byte[] contents) {
		this(name, LocalDateTime.now(), fileName, contents);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getContents() {
		return contents;
	}

	public void setContents(byte[] contents) {
		this.contents = contents;
	}

	public static final class Serializer implements ShardSerializer<FileShard> {
		@Override
		public void serialize(FileShard shard, OutputStream os) throws IOException {
			ByteUtils.writeLengthPrefixed(shard.getFileName(), os);
			ByteUtils.writeLengthPrefixed(shard.getContents(), os);
		}

		@Override
		public FileShard deserialize(InputStream is, String name, LocalDateTime createdAt) throws IOException {
			String fileName = ByteUtils.readLengthPrefixedString(is);
			byte[] contents = ByteUtils.readLengthPrefixed(is);
			return new FileShard(name, createdAt, fileName, contents);
		}
	}
}
