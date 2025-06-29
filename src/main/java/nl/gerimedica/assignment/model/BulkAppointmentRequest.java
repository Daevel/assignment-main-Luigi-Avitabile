package nl.gerimedica.assignment.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BulkAppointmentRequest {
    private List<String> reasons;
    private List<String> dates;
}
