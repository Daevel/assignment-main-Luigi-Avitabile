package nl.gerimedica.assignment.repository;

import nl.gerimedica.assignment.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Appointment save(Appointment appointment);
    List<Appointment> findAll();
}
