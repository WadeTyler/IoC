import net.tylerwade.ioc.IoCContext;
import net.tylerwade.ioc.exception.CircularDependencyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samplebeans.circulardependency.ClassA;
import samplebeans.circulardependency.ClassB;
import samplebeans.circulardependency.ClassC;

import static org.junit.jupiter.api.Assertions.*;

public class CircularDependencyTests {

	@BeforeEach
	void setUp() {

		IoCContext.clear();
	}

	@Test
	void circularDependencyShouldThrow() {

		assertThrows(CircularDependencyException.class, () -> IoCContext.getBean(ClassA.class));
		assertThrows(CircularDependencyException.class, () -> IoCContext.getBean(ClassB.class));
		assertThrows(CircularDependencyException.class, () -> IoCContext.getBean(ClassC.class));
	}

	@Test
	void circularDependencyExceptionShouldContainDependentAndParentClasses() {
		try {
			IoCContext.getBean(ClassA.class);
		} catch (CircularDependencyException e) {
			assertEquals(ClassA.class.getName(), e.getParentClass().getName());
			assertEquals(ClassC.class.getName(), e.getDependentClass().getName());
		}
	}

}