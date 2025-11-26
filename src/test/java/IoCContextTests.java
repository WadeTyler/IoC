import net.tylerwade.ioc.IoCContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samplebeans.user.User;
import samplebeans.user.UserController;
import samplebeans.user.UserRepo;
import samplebeans.user.UserService;

import static org.junit.jupiter.api.Assertions.*;

public class IoCContextTests {


	@BeforeEach
	public void setup() {
		IoCContext.clear();
	}

	@Test
	public void contextLoads() {
		// Test to ensure the application context loads successfully
	}

	@Test
	public void userServiceRegisters() {

		UserService userService = IoCContext.getBean(UserService.class);
	}

	@Test
	public void userServiceRegistersUser() {

		UserService userService = IoCContext.getBean(UserService.class);

		User user = userService.registerUser(1L, "Jacob", "Smith");

		assertNotNull(user);
		assertEquals(1L, user.getId());
		assertEquals("Jacob", user.getFirstName());
		assertEquals("Smith", user.getLastName());
	}

	@Test
	public void userServiceGetsUserById() {

		UserService userService = IoCContext.getBean(UserService.class);

		User user = userService.getUserById(1L);

		assertNotNull(user);
		assertEquals(1L, user.getId());
		assertEquals("John", user.getFirstName());
		assertEquals("Doe", user.getLastName());
	}

	@Test
	public void userServiceGetsNonExistentUserById() {

		UserService userService = IoCContext.getBean(UserService.class);

		User user = userService.getUserById(999L);

		assertNull(user);
	}

	@Test
	public void multipleUserServiceInstancesAreSame() {

		UserService userService1 = IoCContext.getBean(UserService.class);
		UserService userService2 = IoCContext.getBean(UserService.class);

		assertSame(userService1, userService2);
	}

	@Test
	public void doesNotCreateNewUserRepoAfterUserService() {
		UserService userService = IoCContext.getBean(UserService.class);

		UserRepo userRepo = IoCContext.getBean(UserRepo.class);
	}

	@Test
	public void userControllerCreatesWithDependencies() {

		UserController userController = IoCContext.getBean(samplebeans.user.UserController.class);

		UserService userService = IoCContext.getBean(UserService.class);
		UserRepo userRepo = IoCContext.getBean(UserRepo.class);

		assertNotNull(userController);
		assertNotNull(userController.getUserService());
		assertSame(userService, userController.getUserService());
		assertNotNull(userController.getUserRepo());
		assertSame(userRepo, userController.getUserRepo());
	}
}