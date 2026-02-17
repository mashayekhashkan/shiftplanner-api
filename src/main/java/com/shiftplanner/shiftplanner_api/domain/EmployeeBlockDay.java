package com.shiftplanner.shiftplanner_api.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

/**
 * Entity-Klasse zur Abbildung einzelner gesperrter Tage eines Mitarbeiters.
 *
 * Diese Klasse wird verwendet, um spezifische Kalendertage zu speichern,
 * an denen ein Mitarbeiter unabhängig von seiner regulären Verfügbarkeit
 * nicht eingeplant werden darf (z.B. Urlaub, Krankheit, Sonderfreistellung).
 *
 * Datenbanktabelle: employee_block_day
 */
@Entity
@Table(name = "employee_block_day")
@Getter
@Setter
@NoArgsConstructor
public class EmployeeBlockDay {

    /**
     * Primärschlüssel des Block-Tages.
     *
     * Wird automatisch als UUID generiert.
     * UUID eignet sich gut für verteilte Systeme und verhindert
     * ID-Kollisionen.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Referenz auf den Mitarbeiter, für den dieser Tag gesperrt ist.
     *
     * Many-to-One-Beziehung:
     * Ein Mitarbeiter kann mehrere gesperrte Tage besitzen.
     *
     * optional = false bedeutet:
     * Ein BlockDay-Eintrag muss zwingend einem Mitarbeiter zugeordnet sein.
     */
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Employee employee;

    /**
     * Konkretes Datum, an dem der Mitarbeiter nicht verfügbar ist.
     *
     * Beispiel:
     * - Urlaub
     * - Krankheit
     * - Fortbildung
     * - Individuelle Freistellung
     *
     * Es wird LocalDate verwendet (ohne Uhrzeit),
     * da nur das Kalenderdatum relevant ist.
     */
    @Column(nullable = false)
    private LocalDate date;
}
