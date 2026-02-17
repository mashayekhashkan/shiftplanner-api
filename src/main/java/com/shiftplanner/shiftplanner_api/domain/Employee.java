package com.shiftplanner.shiftplanner_api.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
/**
 * Entity-Klasse zur Abbildung eines Mitarbeiters.
 *
 * Diese Klasse repräsentiert einen Mitarbeiter innerhalb einer Filiale (Store)
 * und speichert relevante Informationen wie Name, Wochenarbeitszeit,
 * Schichtbeschränkungen sowie Aktivstatus.
 *
 * Datenbanktabelle: employee
 */
@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
public class Employee {

    /**
     * Primärschlüssel des Mitarbeiters.
     *
     * Wird automatisch als UUID generiert.
     * UUIDs sind besonders geeignet für verteilte Systeme,
     * da sie global eindeutig sind.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Vorname des Mitarbeiters.
     *
     * Pflichtfeld (nullable = false).
     */
    @Column(nullable = false)
    private String firstName;

    /**
     * Nachname des Mitarbeiters.
     *
     * Pflichtfeld (nullable = false).
     */
    @Column(nullable = false)
    private String lastName;

    /**
     * Vertraglich vereinbarte Wochenarbeitszeit in Stunden.
     *
     * Wird als BigDecimal gespeichert, um Rundungsfehler
     * (z.B. bei 37.5 Stunden) zu vermeiden.
     *
     * precision = 4  -> maximale Gesamtanzahl an Ziffern
     * scale = 1      -> eine Nachkommastelle (z.B. 40.0)
     *
     * Beispielwerte:
     * 20.0
     * 37.5
     * 40.0
     */
    @Column(nullable = false, precision = 4, scale = 1)
    private BigDecimal weeklyHoursTarget;

    /**
     * Gibt an, ob der Mitarbeiter ausschließlich samstags arbeitet.
     *
     * true  -> nur Samstagsschichten
     * false -> normale Wochenverfügbarkeit
     */
    @Column(nullable = false)
    private boolean saturdayOnly;

    /**
     * Gibt an, ob der Mitarbeiter ausschließlich Frühschichten übernehmen darf.
     *
     * Wird typischerweise aus vertraglichen oder organisatorischen
     * Gründen gesetzt.
     */
    @Column(nullable = false)
    private boolean onlyEarlyShift;

    /**
     * Gibt an, ob der Mitarbeiter ausschließlich Spätschichten übernehmen darf.
     */
    @Column(nullable = false)
    private boolean onlyLateShift;

    /**
     * Referenz auf die Filiale (Store), in der der Mitarbeiter beschäftigt ist.
     *
     * Viele Mitarbeiter gehören zu einer Filiale (Many-to-One-Beziehung).
     * Ein Mitarbeiter muss zwingend einer Filiale zugeordnet sein
     * (optional = false).
     *
     * In der Datenbank wird dies durch die Fremdschlüsselspalte "store_id" umgesetzt.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    /**
     * Aktivstatus des Mitarbeiters.
     *
     * true  -> Mitarbeiter ist aktiv und kann eingeplant werden
     * false -> Mitarbeiter ist inaktiv (z.B. ausgeschieden, pausiert)
     *
     * Standardwert ist true.
     */
    @Column(nullable = false)
    private boolean active = true;
}
