package net.tylerwade.ioc;

import java.util.HashSet;
import java.util.Set;

/**
 * Manages locks for bean creation to prevent circular dependencies.
 */
class BeanLockManager {

	private static final Set<Class<?>> beanCreationLocks = new HashSet<>();

	/**
	 * Checks if the bean type is currently locked for creation.
	 * @param beanType the class type of the bean to check
	 * @return true if the bean type is locked, false otherwise
	 */
	public static synchronized boolean isLocked(Class<?> beanType) {
		return beanCreationLocks.contains(beanType);
	}

	/**
	 * Locks the bean type for creation.
	 * @param beanType the class type of the bean to lock
	 * @return true if the lock was acquired, false if it was already locked
	 */
	public static synchronized boolean lock(Class<?> beanType) {
		return beanCreationLocks.add(beanType);
	}

	/**
	 * Unlocks the bean type after creation.
	 * @param beanType the class type of the bean to unlock
	 */
	public static synchronized void unlock(Class<?> beanType) {
		beanCreationLocks.remove(beanType);
	}


}