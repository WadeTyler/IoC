package samplebeans;

import java.util.HashMap;
import java.util.Map;

public class UserRepo {

	private final Map<Long, User> users = new HashMap<>();

	public UserRepo() {
		// Initialize with some sample data
		users.put(1L, new User(1L, "John", "Doe"));
		users.put(2L, new User(2L, "Jane", "Smith"));
	}

	public User save(User user) {
		users.put(user.getId(), user);
		return user;
	}

	public User findById(Long id) {
		return users.get(id);
	}

}