package net.tylerwade.ioc;


import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.*;

@Slf4j
public class Beans {

	private static final Map<Class<?>, Object> beans = new HashMap<>();

	/**
	 * Get a bean from the IoC context, creating it and its dependencies if necessary.
	 *
	 * @param beanType the class type of the bean to retrieve
	 * @param <T>      the type of the bean
	 * @return the requested bean instance
	 */
	public static <T> T inject(Class<?> beanType) {

		assertThat("beanType must not be null", beanType != null);

		try {
			while (BeanLockManager.isLocked(beanType)) {
				// Busy-wait until the bean is fully created.
			}

			if (!exists(beanType)) {
				Object bean = BeanFactory.createBean(beanType);
				beans.put(beanType, bean);
				log.info("Bean for {} created and stored in context", beanType.getName());
			}

			return (T) beans.get(beanType);
		} catch (Exception e) {
			log.error("Failed to get bean for {}", beanType.getName());
			throw e;
		}
	}

	/**
	 * Checks if a bean of the specified type exists in the IoC context.
	 *
	 * @param beanType the class type of the bean to check
	 * @return true if the bean exists, false otherwise
	 */
	public static boolean exists(Class<?> beanType) {

		return beans.containsKey(beanType);
	}


	/**
	 * Checks if a bean of the specified type is currently being created.
	 *
	 * @param beanType the class type of the bean to check
	 * @return true if the bean is being created, false otherwise
	 */
	public static boolean isCreating(Class<?> beanType) {

		return BeanLockManager.isLocked(beanType);
	}

	/**
	 * Clears all beans from the IoC context.
	 */
	public static void clear() {

		beans.clear();
	}

	/**
	 * Removes a specific bean from the IoC context.
	 *
	 * @param beanType the class type of the bean to remove
	 */
	public static void remove(Class<?> beanType) {

		beans.remove(beanType);
	}

	public static void removeWithDependencies(Class<?> beanType) {

		if (!exists(beanType)) {
			return;
		}

		beans.remove(beanType);

		Constructor<?> constructor = BeanFactory.getGreediestConstructor(beanType);

		for (Class<?> paramType : constructor.getParameterTypes()) {
			removeWithDependencies(paramType);
		}
	}

}