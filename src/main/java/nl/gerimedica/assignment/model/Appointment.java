package nl.gerimedica.assignment.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@Table(name= "appointments")

public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    protected Appointment() {
    }

    public Appointment(String reason, LocalDateTime date, Patient patient) {
        this.reason = reason;
        this.date = date;
        this.patient = patient;
    }

    public void setReason(String reason) { this.reason = reason; }

    public void setDate(LocalDateTime date) { this.date = date; }

    public void setPatient(Patient patient) { this.patient = patient; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Appointment that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
