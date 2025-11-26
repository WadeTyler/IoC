package samplebeans;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {

	private final UserRepo userRepo;

	public User registerUser(Long id, String firstName, String lastName) {
		User user = new User(id, firstName, lastName);
		return userRepo.save(user);
	}

	public User getUserById(Long id) {
		return userRepo.findById(id);
	}

}