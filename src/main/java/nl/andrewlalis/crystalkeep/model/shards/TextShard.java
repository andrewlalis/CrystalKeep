package nl.andrewlalis.crystalkeep.model.shards;

import nl.andrewlalis.crystalkeep.model.Shard;
import nl.andrewlalis.crystalkeep.model.ShardType;
import nl.andrewlalis.crystalkeep.io.serialization.ByteUtils;
import nl.andrewlalis.crystalkeep.io.serialization.ShardSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

public class TextShard extends Shard {
	private String text;

	public TextShard(String name, LocalDateTime createdAt, String text) {
		super(name, createdAt, ShardType.TEXT);
		this.text = text;
	}

	public TextShard(String name) {
		this(name, LocalDateTime.now(), "");
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return super.toString() + ", text=\"" + this.text + "\"";
	}

	public static class Serializer implements ShardSerializer<TextShard> {
		@Override
		public byte[] serialize(TextShard shard) throws IOException {
			return ByteUtils.writeLengthPrefixedStrings(new String[]{shard.getText()});
		}

		@Override
		public TextShard deserialize(InputStream is, String name, LocalDateTime createdAt) throws IOException {
			String text = ByteUtils.readLengthPrefixedString(is);
			return new TextShard(name, createdAt, text);
		}
	}
}
