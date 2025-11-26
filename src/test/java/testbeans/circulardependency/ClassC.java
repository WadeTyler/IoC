package testbeans.circulardependency;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClassC {
	private final ClassA classA;

}