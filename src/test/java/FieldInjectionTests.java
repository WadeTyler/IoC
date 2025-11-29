import org.junit.jupiter.api.Test;
import testbeans.user.UserController;
import testbeans.user.UserRepo;
import testbeans.user.UserService;

import static net.tylerwade.ioc.Beans.*;
import static org.junit.jupiter.api.Assertions.*;

public class FieldInjectionTests {

	private final UserController userController = inject(UserController.class);

	@Test
	void userControllerExists() {
		assertTrue(exists(UserController.class));
	}

	@Test
	void userServiceExists() {
		assertTrue(exists(UserService.class));
	}

	@Test
	void userRepoExists() {
		assertTrue(exists(UserRepo.class));
	}

	@Test
	void userServiceDependenciesInjected() {
		assertNotNull(userController.getUserService());
		assertNotNull(userController.getUserRepo());
		assertSame(userController.getUserService(), inject(UserService.class));
		assertSame(userController.getUserRepo(), inject(UserRepo.class));
	}

}