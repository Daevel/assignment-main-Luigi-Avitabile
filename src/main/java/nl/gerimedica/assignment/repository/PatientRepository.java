package nl.gerimedica.assignment.repository;

import nl.gerimedica.assignment.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findById(Long id);
    Optional<Patient> findPatientBySsn(String ssn);
}
