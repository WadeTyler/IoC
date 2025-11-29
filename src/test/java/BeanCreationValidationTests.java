import net.tylerwade.ioc.Beans;
import net.tylerwade.ioc.exception.InvalidBeanTypeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import testbeans.standardclasses.Book;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class BeanCreationValidationTests {

	@BeforeEach
	public void setUp() {

		Beans.clear();
	}

	@Test
	void creatingStringBeanShouldThrow() {
		assertThrows(InvalidBeanTypeException.class, () -> Beans.inject(String.class));
	}

	@Test
	void creatingObjectWithStandardClassDependencyShouldThrow() {
		assertThrows(InvalidBeanTypeException.class, () -> Beans.inject(Book.class));
	}

	@ParameterizedTest
	@MethodSource("primitiveTypes")
	void creatingPrimitiveBeanShouldThrow(Class<?> primitiveType) {
		assertThrows(InvalidBeanTypeException.class, () -> Beans.inject(primitiveType));
	}

	private static Stream<Class<?>> primitiveTypes() {
		return Stream.of(boolean.class, byte.class, char.class, short.class, int.class, long.class, float.class, double.class);
	}

	@ParameterizedTest
	@MethodSource("primitiveWrapperTypes")
	void creatingPrimitiveWrapperBeanShouldThrow(Class<?> primitiveWrapperType) {
		assertThrows(InvalidBeanTypeException.class, () -> Beans.inject(primitiveWrapperType));
	}

	private static Stream<Class<?>> primitiveWrapperTypes() {
		return Stream.of(Boolean.class, Byte.class, Character.class, Short.class, Integer.class, Long.class, Float.class, Double.class);
	}

	@ParameterizedTest
	@MethodSource("standardClasses")
	void creatingStandardClassBeanShouldThrow(Class<?> standardClass) {
		assertThrows(InvalidBeanTypeException.class, () -> Beans.inject(standardClass));
	}

	private static Stream<Class<?>> standardClasses() {
		return Stream.of(
				String.class,
				Object.class
		);
	}

	@Test
	void creatingArrayBeanShouldThrow() {
		assertThrows(InvalidBeanTypeException.class, () -> Beans.inject(int[].class));
	}

	@Test
	void creatingEnumBeanShouldThrow() {
		assertThrows(InvalidBeanTypeException.class, () -> Beans.inject(Thread.State.class));
	}

	@Test
	void creatingIoCContextBeanShouldThrow() {
		assertThrows(InvalidBeanTypeException.class, () -> Beans.inject(Beans.class));
	}




}