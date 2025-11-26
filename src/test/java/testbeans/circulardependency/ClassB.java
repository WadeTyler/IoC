package testbeans.circulardependency;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClassB {
	private final ClassC classC;

}