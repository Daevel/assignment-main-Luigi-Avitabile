package nl.gerimedica.assignment.service;

import lombok.extern.slf4j.Slf4j;
import nl.gerimedica.assignment.Utils.Utils;
import nl.gerimedica.assignment.model.Appointment;
import nl.gerimedica.assignment.model.Patient;
import nl.gerimedica.assignment.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientService patientService;

    public List<Appointment> bulkCreateAppointments(
            String patientName,
            String ssn,
            List<String> reasons,
            List<String> dates
    ) {
        Patient found = patientService.findPatientBySsn(ssn);
        if (Utils.isNull(found)) {
            log.info("Creating new patient with SSN: {}", ssn);
            found = new Patient(patientName, ssn);
            patientService.savePatient(found);
        } else {
            log.info("Existing patient found, SSN: {}", found.getSsn());
        }

        List<Appointment> createdAppointments = new ArrayList<>();
        int loopSize = Math.min(reasons.size(), dates.size());

        for (int i = 0; i < loopSize; i++) {
            String reason = reasons.get(i);
            String dateStr = dates.get(i);
            // se necessario, converti dateStr in LocalDateTime
            LocalDateTime date = LocalDateTime.parse(dateStr);
            Appointment appt = new Appointment(reason, date, found);
            createdAppointments.add(appt);
        }

        for (Appointment appt : createdAppointments) {
            appointmentRepository.save(appt);
            log.info("Created appointment for reason: {} [Date: {}] [Patient SSN: {}]", appt.getReason(), appt.getDate(), appt.getPatient().getSsn());
        }

        Utils.recordUsage("Bulk create appointments");

        return createdAppointments;
    }

    /**
     *
     * @param reasonKeyword
     * @return list of appointments finded by reason (string)
     */
    public List<Appointment> getAppointmentsByReason(String reasonKeyword) {
        List<Appointment> allAppointments = appointmentRepository.findAll();
        List<Appointment> matched = new ArrayList<>();

        for (Appointment ap : allAppointments) {
            if (ap.getReason().contains(reasonKeyword)) {
                matched.add(ap);
            }
        }

        List<Appointment> finalList = new ArrayList<>();
        for (Appointment ap : matched) {
            if (ap.getReason().equalsIgnoreCase(reasonKeyword)) {
                finalList.add(ap);
            }
        }

        Utils.recordUsage("Get appointments by reason");

        return finalList;
    }

    /**
     *
     * @param ssn
     * delete an appointment by SSN code
     */
    @Transactional
    public void deleteAppointmentsBySsn(String ssn) {
        Patient patient = patientService.findPatientBySsn(ssn);
        if (Utils.isNull(patient)) {
            return;
        }
        List<Appointment> appointments = patient.getAppointments();
        appointmentRepository.deleteAll(appointments);
    }

    /**
     *
     * @param ssn
     * @return latest Appointment by using SSN code
     */
    public Appointment findLatestAppointmentBySsn(String ssn) {
        Patient patient = patientService.findPatientBySsn(ssn);
        if (Utils.isNullOrEmpty(patient)) {
            return null;
        }

        Appointment latest = null;
        for (Appointment appt : patient.getAppointments()) {
            if (Utils.isNull(appt) || appt.getDate().isAfter(latest.getDate())) {
                latest = appt;
            }
        }

        return latest;
    }
}
