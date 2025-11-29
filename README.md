# IoC (Inversion of Control) Container

A lightweight Java IoC container (dependency injection) for creating and wiring beans with constructor and field injection, plus simple cycle detection.

## Features
- Lightweight bean factory for managing object creation
- Constructor and field injection for dependencies
- Circular dependency detection with clear exceptions
- Tested with JUnit

## Project Structure
- `src/main/java/net/tylerwade/ioc/` — core IoC classes (`BeanFactory`, `Beans`, `BeanLockManager`) and exceptions
- `src/test/java/` — unit tests, integration tests and sample beans
- `pom.xml` — Maven build configuration

## Quick Start

### Build & Test
Use Maven to compile and run tests:

```zsh
mvn -q clean test
```

### Example Usage
All you have to do is call the `Beans.inject(...)` method to create a bean with its dependencies injected. For a bean to be created, all of its dependencies must also be possible to create, with valid constructors.


```java
// Example: retrieve a service with dependencies
import static net.tylerwade.ioc.Beans.*;

// ...

MyService service = inject(MyService.class);
```

You can also manually remove beans as needed.

```java
// Example: Remove a bean from the container
import static net.tylerwade.ioc.Beans.*;

// ...

removeBean(MyService.class);

// ... The next time it is requested, it will be re-created.
MyService service = inject(MyService.class);

// ...

// If you want to fully remove a bean and all of it's dependencies:
removeWithDependencies(MyService.class);
```

You can also clear all beans from the container.

```java
// Example: Clear all beans from the container
import static net.tylerwade.ioc.Beans.*;

// ...
clear();
```

There's much more you can do. Check out the Javadocs for full details.

### Exceptions
- `BeanCreationException` — errors creating a bean
- `CircularDependencyException` — cycle detected while resolving dependencies
- `InvalidBeanTypeException` — bean type not supported
- `RequiredConstructorNotFound` — no suitable constructor for injection

## Development
- Java version and dependencies are managed in `pom.xml`
- Tests live under `src/test/java`