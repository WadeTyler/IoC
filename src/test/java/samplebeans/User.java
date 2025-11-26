package samplebeans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class User {

	private Long id;
	private String firstName;
	private String lastName;

}