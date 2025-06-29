package nl.gerimedica.assignment.controller;


import nl.gerimedica.assignment.Utils.Utils;
import nl.gerimedica.assignment.model.Patient;
import nl.gerimedica.assignment.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * GET /api/patients
     * Example: /api/patients
     * @return list of patients
     */
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.findAll();
        return ResponseEntity.ok(patients);
    }

    /**
     * GET /api/patients/id/{id}
     * Example: /api/patients/id/1234
     * @return list of patients
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<Patient> getPatientByID(@PathVariable Long id) {
        try {
            Patient patient = patientService.findById(id);
            return ResponseEntity.ok(patient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * GET /api/patients/ssn/{ssn}
     * Example: /api/patients/ssn/ABDE2DBXCS
     * @param ssn
     * @return patient based on ssn code
     */
    @GetMapping("/ssn/{ssn}")
    public ResponseEntity<Patient> getPatientBySSN(@PathVariable String ssn) {
        try {
            Patient patient = patientService.findPatientBySsn(ssn);
            return ResponseEntity.ok(patient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    /**
     *
     * @param patient
     * @return new patient saved
     */
    @PostMapping("/save")
    public ResponseEntity<Patient> savePatient(@RequestBody Patient patient) {
        if (Utils.isNull(patient)) {
            return ResponseEntity.badRequest().build();
        }

        Utils.recordUsage("Controller triggered for saving patient");

        Patient saved = patientService.savePatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

}
