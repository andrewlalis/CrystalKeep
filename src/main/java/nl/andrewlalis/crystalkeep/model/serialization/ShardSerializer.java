package nl.andrewlalis.crystalkeep.model.serialization;

import nl.andrewlalis.crystalkeep.model.Shard;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

public interface ShardSerializer<T extends Shard> {
	byte[] serialize(T shard) throws IOException;

	T deserialize(InputStream is, String name, LocalDateTime createdAt) throws IOException;
}
