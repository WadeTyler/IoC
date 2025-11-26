package samplebeans.circulardependency;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClassA {

	private final ClassB classB;

}