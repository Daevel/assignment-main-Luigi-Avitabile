package nl.gerimedica.assignment.service;

import nl.gerimedica.assignment.model.Patient;
import nl.gerimedica.assignment.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Transactional
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient findById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with ID: " + id));
    }

    public Patient findPatientBySsn(String ssn) {
        return patientRepository.findPatientBySsn(ssn)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with SSN: " + ssn));
    }
}
