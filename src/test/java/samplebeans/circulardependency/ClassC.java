package samplebeans.circulardependency;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClassC {
	private final ClassA classA;

}