package samplebeans.circulardependency;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClassB {
	private final ClassC classC;

}