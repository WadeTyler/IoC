import net.tylerwade.ioc.IoCContext;
import org.junit.jupiter.api.Test;
import samplebeans.User;
import samplebeans.UserService;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationTests {

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

}