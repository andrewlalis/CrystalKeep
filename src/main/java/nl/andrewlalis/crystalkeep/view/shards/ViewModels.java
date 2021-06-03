package nl.andrewlalis.crystalkeep.view.shards;

import nl.andrewlalis.crystalkeep.model.Shard;
import nl.andrewlalis.crystalkeep.model.shards.FileShard;
import nl.andrewlalis.crystalkeep.model.shards.LoginCredentialsShard;
import nl.andrewlalis.crystalkeep.model.shards.TextShard;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Utility class that provides the ability to look up and obtain a new view
 * model for a particular type of shard.
 */
public class ViewModels {
	private static final Map<Class<?>, Class<?>> shardViewModels = new HashMap<>();
	static {
		shardViewModels.put(TextShard.class, TextShardViewModel.class);
		shardViewModels.put(LoginCredentialsShard.class, LoginCredentialsViewModel.class);
		shardViewModels.put(FileShard.class, FileShardViewModel.class);
	}

	public static Optional<ShardViewModel<?>> get(Shard shard) {
		try {
			Class<?> viewModelClass = shardViewModels.get(shard.getClass());
			if (viewModelClass != null) {
				var viewModel = (ShardViewModel<?>) viewModelClass.getDeclaredConstructor(shard.getClass()).newInstance(shard);
				return Optional.of(viewModel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
}
