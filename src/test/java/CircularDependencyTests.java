import net.tylerwade.ioc.Beans;
import net.tylerwade.ioc.exception.CircularDependencyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testbeans.circulardependency.ClassA;
import testbeans.circulardependency.ClassB;
import testbeans.circulardependency.ClassC;

import static org.junit.jupiter.api.Assertions.*;

public class CircularDependencyTests {

	@BeforeEach
	void setUp() {

		Beans.clear();
	}

	@Test
	void circularDependencyShouldThrow() {

		assertThrows(CircularDependencyException.class, () -> Beans.inject(ClassA.class));
		assertThrows(CircularDependencyException.class, () -> Beans.inject(ClassB.class));
		assertThrows(CircularDependencyException.class, () -> Beans.inject(ClassC.class));
	}

	@Test
	void circularDependencyExceptionShouldContainDependentAndParentClasses() {
		try {
			Beans.inject(ClassA.class);
		} catch (CircularDependencyException e) {
			assertEquals(ClassA.class.getName(), e.getParentClass().getName());
			assertEquals(ClassC.class.getName(), e.getDependentClass().getName());
		}
	}

}