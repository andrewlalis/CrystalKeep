package nl.andrewlalis.crystalkeep.model.shards;

import nl.andrewlalis.crystalkeep.io.serialization.ByteUtils;
import nl.andrewlalis.crystalkeep.io.serialization.ShardSerializer;
import nl.andrewlalis.crystalkeep.model.Shard;
import nl.andrewlalis.crystalkeep.model.ShardType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

	public static final class Serializer implements ShardSerializer<TextShard> {
		@Override
		public void serialize(TextShard shard, OutputStream os) throws IOException {
			ByteUtils.writeLengthPrefixedStrings(new String[]{shard.getText()}, os);
		}

		@Override
		public TextShard deserialize(InputStream is, String name, LocalDateTime createdAt) throws IOException {
			String text = ByteUtils.readLengthPrefixedString(is);
			return new TextShard(name, createdAt, text);
		}
	}
}
