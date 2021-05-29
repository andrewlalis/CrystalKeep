package nl.andrewlalis.crystalkeep.model.shards;

import lombok.Getter;
import lombok.Setter;
import nl.andrewlalis.crystalkeep.model.Shard;
import nl.andrewlalis.crystalkeep.model.serialization.ByteUtils;
import nl.andrewlalis.crystalkeep.model.serialization.ShardSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

@Getter
@Setter
public class LoginCredentialsShard extends Shard {
	private String username;
	private String password;

	public LoginCredentialsShard(String name, LocalDateTime createdAt, String username, String password) {
		super(name, createdAt, ShardType.LOGIN_CREDENTIALS);
		this.username = username;
		this.password = password;
	}

	@Override
	public String toString() {
		return super.toString() + ", username=\"" + this.username + "\", password=\"" + this.password + "\"";
	}

	public static class Serializer implements ShardSerializer<LoginCredentialsShard> {

		@Override
		public byte[] serialize(LoginCredentialsShard shard) throws IOException {
			return ByteUtils.writeLengthPrefixedStrings(new String[]{shard.getUsername(), shard.getPassword()});
		}

		@Override
		public LoginCredentialsShard deserialize(InputStream is, String name, LocalDateTime createdAt) throws IOException {
			String username = ByteUtils.readLengthPrefixedString(is);
			String password = ByteUtils.readLengthPrefixedString(is);
			return new LoginCredentialsShard(name, createdAt, username, password);
		}
	}
}