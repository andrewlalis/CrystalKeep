package nl.andrewlalis.crystalkeep.model.shards;

import nl.andrewlalis.crystalkeep.model.Shard;
import nl.andrewlalis.crystalkeep.model.ShardType;

import java.time.LocalDateTime;

public class FileShard extends Shard {
	private String fileName;
	private String mimeType;
	private byte[] contents;

	public FileShard(String name, LocalDateTime createdAt, String fileName, String mimeType, byte[] contents) {
		super(name, createdAt, ShardType.FILE);
		this.fileName = fileName;
		this.mimeType = mimeType;
		this.contents = contents;
	}

	public FileShard(String name, String fileName, String mimeType, byte[] contents) {
		this(name, LocalDateTime.now(), fileName, mimeType, contents);
	}

	public String getFileName() {
		return fileName;
	}

	public String getMimeType() {
		return mimeType;
	}

	public byte[] getContents() {
		return contents;
	}
}
