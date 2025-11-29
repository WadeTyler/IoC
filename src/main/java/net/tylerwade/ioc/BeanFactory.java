package net.tylerwade.ioc;

import lombok.extern.slf4j.Slf4j;
import net.tylerwade.ioc.exception.BeanCreationException;
import net.tylerwade.ioc.exception.CircularDependencyException;
import net.tylerwade.ioc.exception.InvalidBeanTypeException;
import net.tylerwade.ioc.exception.RequiredConstructorNotFound;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
class BeanFactory {

	/**
	 * Creates a bean of the specified type, resolving and injecting its dependencies.
	 *
	 * @param beanType the class type of the bean to create
	 */
	protected static Object createBean(Class<?> beanType) {

		try {
			if (!BeanLockManager.lock(beanType)) {
				// A creation is already in progress for this bean type. This shouldn't happen due to the earlier check.
				throw new IllegalStateException("Recursive creation conflict for bean type: " + beanType.getName());
			}

			validateBeanType(beanType);

			log.info("Creating bean for {}", beanType.getName());

			Constructor<?> constructor = getGreediestConstructor(beanType);

			checkForCircularDependency(beanType, new HashSet<>());

			Map<Class<?>, Object> dependencies = getDependencies(constructor.getParameterTypes());

			Object newBean = createBeanInstance(beanType, dependencies, constructor);

			// Store the newly created bean in the context.

			log.info("Finished creating bean for {}", beanType.getName());
			return newBean;
		} catch (Exception e) {
			log.error("Failed to create bean for {}. Reason: {}", beanType.getName(), e.getMessage());
			throw e;
		} finally {
			BeanLockManager.unlock(beanType);
		}
	}

	private static void validateBeanType(Class<?> type) {

		if (type.isPrimitive()
				|| type.getName().startsWith("net.tylerwade.ioc.")
				|| type.getName().startsWith("java.lang.")
				|| type.getName().startsWith("java.util.")
				|| type.getName().startsWith("java.time.")
				|| Collection.class.isAssignableFrom(type)
				|| Map.class.isAssignableFrom(type)
				|| type.isArray()
				|| type == Object.class) {
			throw new InvalidBeanTypeException(type);
		}
	}

	/**
	 * Identiefies the constructor with the most parameters (the "greediest" constructor).
	 *
	 * @param beanType the class type of the bean
	 * @return the greediest constructor
	 */
	public static Constructor<?> getGreediestConstructor(Class<?> beanType) {

		Constructor<?>[] constructors = beanType.getConstructors();

		if (constructors.length == 0) {
			throw new RequiredConstructorNotFound(beanType);
		} else if (constructors.length == 1) {
			return constructors[0];
		}

		Constructor<?> constructor = Arrays.stream(constructors)
				.max(Comparator.comparingInt(Constructor::getParameterCount))
				.orElseThrow(() -> new RequiredConstructorNotFound(beanType));

		for (Class<?> param : constructor.getParameterTypes()) {
			try {
				validateBeanType(param);
			} catch (InvalidBeanTypeException e) {
				throw new InvalidBeanTypeException(param, beanType);
			}
		}

		return constructor;
	}

	/**
	 * Checks for circular dependencies in the constructor parameters.
	 * Implements a recursive depth-first search to detect cycles.
	 *
	 * @param clazz   the class to check
	 * @param parents the set of parent classes in the current dependency chain
	 */
	private static void checkForCircularDependency(Class<?> clazz, Set<Class<?>> parents) {

		parents.add(clazz);

		Constructor<?> constructor = getGreediestConstructor(clazz);

		for (Class<?> paramType : constructor.getParameterTypes()) {
			if (parents.contains(paramType)) {
				throw new CircularDependencyException(clazz, paramType);
			}

			checkForCircularDependency(paramType, parents);
		}

		parents.remove(clazz);
	}


	/**
	 * Resolves the dependencies required for a bean's constructor.
	 *
	 * @param requiredFields the classes of the required dependencies
	 * @return a map of dependency class types to their instances
	 */
	private static Map<Class<?>, Object> getDependencies(Class<?>[] requiredFields) {

		Map<Class<?>, Object> dependencies = new HashMap<>();

		for (Class<?> clazz : requiredFields) {
			Object dependency = Beans.inject(clazz);
			dependencies.put(clazz, dependency);
		}

		return dependencies;
	}

	/**
	 * Creates an instance of the bean using the provided constructor and resolved dependencies.
	 *
	 * @param beanType     the class type of the bean
	 * @param dependencies the resolved dependencies
	 * @param constructor  the constructor to use for instantiation
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