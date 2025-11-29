import net.tylerwade.ioc.Beans;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testbeans.user.User;
import testbeans.user.UserController;
import testbeans.user.UserRepo;
import testbeans.user.UserService;

import static net.tylerwade.ioc.Beans.*;
import static org.junit.jupiter.api.Assertions.*;

public class BeansTests {

	@BeforeEach
	public void setup() {

		Beans.clear();
	}

	@Test
	public void contextLoads() {
		// Test to ensure the application context loads successfully
	}

	@Test
	public void userServiceRegisters() {

		inject(UserService.class);
	}

	@Test
	public void userServiceRegistersUser() {

		UserService userService = inject(UserService.class);

		User user = userService.registerUser(1L, "Jacob", "Smith");

		assertNotNull(user);
		assertEquals(1L, user.getId());
		assertEquals("Jacob", user.getFirstName());
		assertEquals("Smith", user.getLastName());
	}

	@Test
	public void userServiceGetsUserById() {

		UserService userService = inject(UserService.class);

		User user = userService.getUserById(1L);

		assertNotNull(user);
		assertEquals(1L, user.getId());
		assertEquals("John", user.getFirstName());
		assertEquals("Doe", user.getLastName());
	}

	@Test
	public void userServiceGetsNonExistentUserById() {

		UserService userService = inject(UserService.class);

		User user = userService.getUserById(999L);

		assertNull(user);
	}

	@Test
	public void multipleUserServiceInstancesAreSame() {

		UserService userService1 = inject(UserService.class);
		UserService userService2 = inject(UserService.class);

		assertSame(userService1, userService2);
	}

	@Test
	public void doesNotCreateNewUserRepoAfterUserService() {

		UserService userService = inject(UserService.class);

		UserRepo userRepo = inject(UserRepo.class);
	}

	@Test
	public void userControllerCreatesWithDependencies() {

		UserController userController = inject(testbeans.user.UserController.class);

		UserService userService = inject(UserService.class);
		UserRepo userRepo = inject(UserRepo.class);

		assertNotNull(userController);
		assertNotNull(userController.getUserService());
		assertSame(userService, userController.getUserService());
		assertNotNull(userController.getUserRepo());
		assertSame(userRepo, userController.getUserRepo());
	}

	@Test
	public void userServiceGetsRemoved() {
		UserService userService = inject(UserService.class);

		assertTrue(exists(UserService.class));

		remove(UserService.class);

		assertFalse(exists(UserService.class));
	}

	@Test
	public void userRepoExistsAfterUserServiceRemoved() {
		UserService userService = inject(UserService.class);

		assertTrue(exists(UserService.class));
		assertTrue(exists(UserRepo.class));

		remove(UserService.class);

		assertFalse(exists(UserService.class));
		assertTrue(exists(UserRepo.class));
	}

	@Test
	public void userControllerAndDependenciesRemoved() {

		UserController userController = inject(UserController.class);

		assertTrue(exists(UserController.class));
		assertTrue(exists(UserService.class));
		assertTrue(exists(UserRepo.class));

		removeWithDependencies(UserController.class);

		assertFalse(exists(UserController.class));
		assertFalse(exists(UserService.class));
		assertFalse(exists(UserRepo.class));
	}

}