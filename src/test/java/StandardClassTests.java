import net.tylerwade.ioc.IoCContext;
import net.tylerwade.ioc.exception.InvalidBeanTypeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import testbeans.standardclasses.Book;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class StandardClassTests {

	@BeforeEach
	public void setUp() {

		IoCContext.clear();
	}

	@Test
	void creatingStringBeanShouldThrow() {
		assertThrows(InvalidBeanTypeException.class, () -> IoCContext.getBean(String.class));
	}

	@Test
	void creatingObjectWithStandardClassDependencyShouldThrow() {
		assertThrows(InvalidBeanTypeException.class, () -> IoCContext.getBean(Book.class));
	}

	@ParameterizedTest
	@MethodSource("primitiveTypes")
	void creatingPrimitiveBeanShouldThrow(Class<?> primitiveType) {
		assertThrows(InvalidBeanTypeException.class, () -> IoCContext.getBean(primitiveType));
	}

	private static Stream<Class<?>> primitiveTypes() {
		return Stream.of(boolean.class, byte.class, char.class, short.class, int.class, long.class, float.class, double.class);
	}

	@ParameterizedTest
	@MethodSource("primitiveWrapperTypes")
	void creatingPrimitiveWrapperBeanShouldThrow(Class<?> primitiveWrapperType) {
		assertThrows(InvalidBeanTypeException.class, () -> IoCContext.getBean(primitiveWrapperType));
	}

	private static Stream<Class<?>> primitiveWrapperTypes() {
		return Stream.of(Boolean.class, Byte.class, Character.class, Short.class, Integer.class, Long.class, Float.class, Double.class);
	}

	@ParameterizedTest
	@MethodSource("standardClasses")
	void creatingStandardClassBeanShouldThrow(Class<?> standardClass) {
		assertThrows(InvalidBeanTypeException.class, () -> IoCContext.getBean(standardClass));
	}

	private static Stream<Class<?>> standardClasses() {
		return Stream.of(
				String.class,
				Object.class
		);
	}




}