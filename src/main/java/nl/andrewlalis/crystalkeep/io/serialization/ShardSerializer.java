package nl.andrewlalis.crystalkeep.io.serialization;

import nl.andrewlalis.crystalkeep.model.Shard;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;

/**
 * This interface should be implemented to provide the ability to serialize and
 * deserialize a shard to and from a stream of bytes.
 * @param <T> The shard type.
 */
public interface ShardSerializer<T extends Shard> {
	void serialize(T shard, OutputStream os) throws IOException;

	T deserialize(InputStream is, String name, LocalDateTime createdAt) throws IOException;
}
