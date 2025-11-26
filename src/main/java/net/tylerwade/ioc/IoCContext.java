package net.tylerwade.ioc;


import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.*;

@Slf4j
public class IoCContext {

	private static final Map<Class<?>, Object> beans = new HashMap<>();

	/**
	 * Get a bean from the IoC context, creating it and its dependencies if necessary.
	 *
	 * @param beanType the class type of the bean to retrieve
	 * @param <T>      the type of the bean
	 * @return the requested bean instance
	 */
	public static <T> T getBean(Class<?> beanType) {

		assertThat("beanType must not be null", beanType != null);
		assertThat("Cannot retrieve a bean of IoCContext type", !beanType.equals(IoCContext.class));

		try {
			while (BeanLockManager.isLocked(beanType)) {
				// Busy-wait until the bean is fully created.
			}

			if (!beanExists(beanType)) {
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
	public static boolean beanExists(Class<?> beanType) {

		return beans.containsKey(beanType);
	}


	/**
	 * Clears all beans from the IoC context.
	 */
	public static void clear() {

		beans.clear();
	}

}