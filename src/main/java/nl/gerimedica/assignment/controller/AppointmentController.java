package nl.gerimedica.assignment.controller;

import nl.gerimedica.assignment.model.Appointment;
import nl.gerimedica.assignment.Utils.Utils;
import nl.gerimedica.assignment.model.BulkAppointmentRequest;
import nl.gerimedica.assignment.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    AppointmentService appointmentService;

    /**
     *
     * Example: {
     * "reasons": ["Checkup", "Follow-up", "X-Ray"],
     * "dates": ["2025-02-01", "2025-02-15", "2025-03-01"]
     * }
     *
     * @param patientName
     * @param ssn
     * @param payload
     *
     * @return creation list of appointments based on reasons and dates found
     */
    @PostMapping("/bulk")
    public ResponseEntity<List<Appointment>> createBulkAppointments(
            @RequestParam String patientName,
            @RequestParam String ssn,
            @RequestBody BulkAppointmentRequest payload
    ) {
        List<String> reasons = payload.getReasons();
        List<String> dates = payload.getDates();

        if (Utils.isNullOrEmpty(reasons) || Utils.isNullOrEmpty(dates)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Utils.recordUsage("Controller triggered bulk appointments creation");

        List<Appointment> created = appointmentService.bulkCreateAppointments(patientName, ssn, reasons, dates);
        return new ResponseEntity<>(created, HttpStatus.OK);
    }

    /**
     * GET /api/appointments
     * Example: /api/appointments?reason=X-Ray
     * @param reason
     * @return list of appointments based on reason
     */
    @GetMapping
    public ResponseEntity<List<Appointment>> getAppointmentsByReason(
            @RequestParam String reason
    ) {
        List<Appointment> found = appointmentService.getAppointmentsByReason(reason);
        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    /**
     * DELETE /api/appointments
     * Example: /api/appointments?ssn=XYZ
     * @param ssn
     * @return deletion of appointments based on ssn code
     */
    @DeleteMapping
    public ResponseEntity<String> deleteAppointmentsBySSN(@RequestParam String ssn) {
        appointmentService.deleteAppointmentsBySsn(ssn);
        return new ResponseEntity<>("Deleted all appointments for SSN: " + ssn, HttpStatus.OK);
    }

    /**
     * GET /api/appointment/latest
     * Example: /api/appointments/latest?ssn=XYZ
     * @param ssn
     * @return latest appointment based on ssn code
     */
    @GetMapping("/latest")
    public ResponseEntity<Appointment> getLatestAppointment(@RequestParam String ssn) {
        Appointment latest = appointmentService.findLatestAppointmentBySsn(ssn);

        if (Utils.isNull(latest)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(latest, HttpStatus.OK);
    }
}
