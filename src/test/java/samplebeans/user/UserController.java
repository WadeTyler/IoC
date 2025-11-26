package samplebeans.user;

public class UserController {

	private final UserService userService;
	private final UserRepo userRepo;

	public UserController(UserService userService, UserRepo userRepo) {

		this.userService = userService;
		this.userRepo = userRepo;
	}

	public User getUserById(Long id) {
		return userService.getUserById(id);
	}

	public UserService getUserService() {

		return userService;
	}

	public UserRepo getUserRepo() {

		return userRepo;
	}
}