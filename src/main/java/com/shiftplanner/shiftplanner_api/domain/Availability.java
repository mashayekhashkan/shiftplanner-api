package com.shiftplanner.shiftplanner_api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.UUID;

/**
 * Entity-Klasse zur Abbildung der Verfügbarkeiten von Mitarbeitern.
 *
 * Diese Klasse speichert, an welchem Wochentag ein bestimmter Mitarbeiter
 * für eine bestimmte Schicht verfügbar ist.
 *
 * Datenbanktabelle: availability
 */
@Entity
@Table(name = "availability")
@Getter
@Setter
@NoArgsConstructor
public class Availability {

    /**
     * Enum zur Definition der möglichen Schichttypen.
     *
     * EARLY     -> Frühschicht
     * LATE      -> Spätschicht
     * SAT_FULL  -> Ganztägige Samstagsschicht
     */
    public enum ShiftCode {
        EARLY,
        LATE,
        SAT_FULL
    }

    /**
     * Primärschlüssel der Availability-Entität.
     *
     * Wird automatisch als UUID generiert.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Referenz auf den Mitarbeiter, dem diese Verfügbarkeit gehört.
     *
     * Viele Verfügbarkeiten können einem Mitarbeiter zugeordnet sein (Many-to-One).
     * optional = false bedeutet, dass jede Verfügbarkeit zwingend
     * einem Mitarbeiter zugeordnet sein muss.
     *
     * In der Datenbank wird dies durch die Fremdschlüsselspalte "employee_id"
     * realisiert.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    /**
     * Wochentag, für den die Verfügbarkeit gilt.
     *
     * Verwendet das java.time.DayOfWeek Enum.
     * Darf nicht null sein.
     */
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    /**
     * Schichttyp, für den die Verfügbarkeit definiert wird.
     *
     * Wird als String in der Datenbank gespeichert (z.B. "EARLY").
     * Die Verwendung von EnumType.STRING ist empfehlenswert,
     * da Änderungen an der Enum-Reihenfolge sonst keine Probleme verursachen.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShiftCode shiftCode;

    /**
     * Gibt an, ob der Mitarbeiter in der angegebenen Schicht verfügbar ist.
     *
     * true  -> Mitarbeiter ist verfügbar
     * false -> Mitarbeiter ist nicht verfügbar
     */
    @Column(nullable = false)
    private boolean available;
}