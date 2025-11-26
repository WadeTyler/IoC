package testbeans.circulardependency;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClassA {

	private final ClassB classB;

}