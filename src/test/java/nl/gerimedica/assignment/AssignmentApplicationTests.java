package nl.gerimedica.assignment;

import nl.gerimedica.assignment.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AssignmentApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void testGetAllPatientsEndpoint() {
		String url = "http://localhost:" + port + "/api/patients";

		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		String body = response.getBody();

		assertNotNull(body);
		assertTrue(body.trim().startsWith("["));

		// Facoltativo:
		// assertTrue(body.contains("Mario Rossi"));
	}

	@Test
	void testAddPatientAndGetAll() {
		String saveUrl = "http://localhost:" + port + "/api/patients/save";

		Patient newPatient = new Patient();
		newPatient.setName("Mario Rossi");
		newPatient.setId(12345L);
		newPatient.setSsn("12345");

		ResponseEntity<Patient> saveResponse = restTemplate.postForEntity(saveUrl, newPatient, Patient.class);
		assertEquals(HttpStatus.CREATED, saveResponse.getStatusCode());
		assertNotNull(saveResponse.getBody());
		assertEquals("Mario", saveResponse.getBody().getName());

		// Recupero lista pazienti
		String getAllUrl = "http://localhost:" + port + "/api/patients";

		ResponseEntity<Patient[]> getAllResponse = restTemplate.getForEntity(getAllUrl, Patient[].class);
		assertEquals(HttpStatus.OK, getAllResponse.getStatusCode());
		Patient[] patients = getAllResponse.getBody();

		assertNotNull(patients);
		// Controlla che la lista contenga il paziente appena inserito (esempio semplice)
		boolean found = Arrays.stream(patients)
				.anyMatch(p -> "Mario".equals(p.getName()));
		assertTrue(found, "The patient should be found in the list.");
	}
}
