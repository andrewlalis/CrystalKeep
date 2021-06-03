package nl.andrewlalis.crystalkeep.model.shards;

import nl.andrewlalis.crystalkeep.io.serialization.ByteUtils;
import nl.andrewlalis.crystalkeep.io.serialization.ShardSerializer;
import nl.andrewlalis.crystalkeep.model.Shard;
import nl.andrewlalis.crystalkeep.model.ShardType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;

public class LoginCredentialsShard extends Shard {
	private String username;
	private String password;

	public LoginCredentialsShard(String name, LocalDateTime createdAt, String username, String password) {
		super(name, createdAt, ShardType.LOGIN_CREDENTIALS);
		this.username = username;
		this.password = password;
	}

	public LoginCredentialsShard(String name) {
		this(name, LocalDateTime.now(), "", "");
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return super.toString() + ", username=\"" + this.username + "\", password=\"" + this.password + "\"";
	}

	public static final class Serializer implements ShardSerializer<LoginCredentialsShard> {

		@Override
		public void serialize(LoginCredentialsShard shard, OutputStream os) throws IOException {
			ByteUtils.writeLengthPrefixedStrings(new String[]{shard.getUsername(), shard.getPassword()}, os);
		}

		@Override
		public LoginCredentialsShard deserialize(InputStream is, String name, LocalDateTime createdAt) throws IOException {
			String username = ByteUtils.readLengthPrefixedString(is);
			String password = ByteUtils.readLengthPrefixedString(is);
			return new LoginCredentialsShard(name, createdAt, username, password);
		}
	}
}
