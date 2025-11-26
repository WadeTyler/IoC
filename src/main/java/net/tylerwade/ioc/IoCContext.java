package net.tylerwade.ioc;


import lombok.extern.slf4j.Slf4j;
import net.tylerwade.ioc.exception.BeanCreationException;
import net.tylerwade.ioc.exception.RequiredConstructorNotFound;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.*;

@Slf4j
public class IoCContext {

	private static final Map<Class<?>, Object> beans = new HashMap<>();

	private static final Set<Class<?>> beanCreationLocks = new HashSet<>();

	/**
	 * Get a bean from the IoC context, creating it and its dependencies if necessary.
	 *
	 * @param beanType the class type of the bean to retrieve
	 * @param <T>      the type of the bean
	 * @return the requested bean instance
	 */
	public static <T> T getBean(Class<?> beanType) {

		assertThat("beanType must not be null", beanType != null);

		try {

			while (beanCreationLocks.contains(beanType)) {
				// Busy-wait until the bean is fully created.
			}

			if (!beans.containsKey(beanType)) {
				createBean(beanType);
			}

			return (T) beans.get(beanType);
		} catch (Exception e) {
			log.error("Failed to get bean for {}", beanType.getName());
			throw e;
		}
	}

	/**
	 * Creates a bean of the specified type, resolving and injecting its dependencies.
	 * @param beanType the class type of the bean to create
	 */
	private static void createBean(Class<?> beanType) {

		assertThat("beanType must not be null", beanType != null);
		assertThat("Only one instance of each beanType is supported", !beans.containsKey(beanType));

		try {
			if (!beanCreationLocks.add(beanType)) {
				// A creation is already in progress for this bean type. This shouldn't happen due to the earlier check.
				throw new IllegalStateException("Recursive creation conflict for bean type: " + beanType.getName());
			}

			log.info("Creating bean for {}", beanType.getName());

			Constructor<?> constructor = getGreediestConstructor(beanType);

			Map<Class<?>, Object> dependencies = getDependencies(constructor.getParameterTypes());

			Object newBean = createBeanInstance(beanType, dependencies, constructor);

			// Store the newly created bean in the context.
			beans.put(beanType, newBean);
		} catch (Exception e) {
			log.error("Failed to create bean for {}", beanType.getName());
			throw e;
		} finally {
			log.info("Finished creating bean for {}", beanType.getName());
			beanCreationLocks.remove(beanType);
		}
	}

	/**
	 * Identiefies the constructor with the most parameters (the "greediest" constructor).
	 * @param beanType the class type of the bean
	 * @return the greediest constructor
	 */
	private static Constructor<?> getGreediestConstructor(Class<?> beanType) {

		Constructor<?>[] constructors = beanType.getConstructors();

		if (constructors.length == 0) {
			throw new RequiredConstructorNotFound(beanType);
		} else if (constructors.length == 1) {
			return constructors[0];
		}

		return Arrays.stream(constructors)
				.max(Comparator.comparingInt(Constructor::getParameterCount))
				.orElseThrow(() -> new RequiredConstructorNotFound(beanType));
	}


	/**
	 * Resolves the dependencies required for a bean's constructor.
	 * @param requiredFields the classes of the required dependencies
	 * @return a map of dependency class types to their instances
	 */
	private static Map<Class<?>, Object> getDependencies(Class<?>[] requiredFields) {

		Map<Class<?>, Object> dependencies = new HashMap<>();

		for (Class<?> clazz : requiredFields) {
			Object dependency = getBean(clazz);
			dependencies.put(clazz, dependency);
		}

		return dependencies;
	}

	/**
	 * Creates an instance of the bean using the provided constructor and resolved dependencies.
	 * @param beanType the class type of the bean
	 * @param dependencies the resolved dependencies
	 * @param constructor the constructor to use for instantiation
	 * @return the newly created bean instance
	 */
	private static Object createBeanInstance(Class<?> beanType, Map<Class<?>, Object> dependencies, Constructor<?> constructor) {

		try {
			Object[] constructorArgs = Arrays.stream(constructor.getParameterTypes())
					.map(dependencies::get)
					.toArray();

			return constructor.newInstance(constructorArgs);
		} catch (ReflectiveOperationException e) {
			throw new BeanCreationException(beanType, constructor, e);
		}
	}

}