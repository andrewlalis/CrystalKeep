package nl.andrewlalis.crystalkeep.model.shards;

import lombok.Getter;
import lombok.Setter;
import nl.andrewlalis.crystalkeep.model.Cluster;
import nl.andrewlalis.crystalkeep.model.Shard;

import java.time.LocalDateTime;

@Getter
@Setter
public class LoginCredentialsShard extends Shard {
	private String username;
	private String password;

	public LoginCredentialsShard(Cluster cluster, String name, LocalDateTime createdAt, String username, String password) {
		super(cluster, name, createdAt);
		this.username = username;
		this.password = password;
	}
}
