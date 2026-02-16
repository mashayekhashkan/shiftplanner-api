package com.shiftplanner.shiftplanner_api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.ISBN;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Entity-Klasse zur Abbildung eines Wochenplans (Schedule).
 *
 * Ein Schedule repräsentiert die Schichtplanung einer bestimmten Filiale
 * (Store) für eine definierte Kalenderwoche.
 *
 * Der Plan kann sich in unterschiedlichen Status-Zuständen befinden
 * (z.B. Entwurf oder veröffentlicht).
 *
 * Datenbanktabelle: schedule
 */
@Entity
@Table(name = "schedule")
@Getter
@Setter
@NoArgsConstructor
public class Schedule {

    /**
     * Status eines Wochenplans.
     *
     * DRAFT      -> Plan befindet sich im Entwurfsmodus und ist noch bearbeitbar.
     * PUBLISHED  -> Plan wurde veröffentlicht und ist für Mitarbeiter sichtbar.
     */
    public enum Status {
        DRAFT,
        PUBLISHED
    }

    /**
     * Primärschlüssel des Schedules.
     *
     * Wird automatisch als UUID generiert.
     * UUIDs sind besonders geeignet für skalierbare Systeme
     * und vermeiden ID-Kollisionen.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Referenz auf die Filiale (Store), für die dieser Wochenplan erstellt wurde.
     *
     * Many-to-One-Beziehung:
     * Eine Filiale kann mehrere Wochenpläne besitzen.
     *
     * optional = false bedeutet:
     * Ein Schedule muss zwingend einer Filiale zugeordnet sein.
     *
     * In der Datenbank wird dies über die Fremdschlüsselspalte "store_id" realisiert.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    /**
     * Startdatum der Planungswoche.
     *
     * Speichert typischerweise den Montag der jeweiligen Kalenderwoche.
     *
     * Beispiel:
     * 2026-03-02 -> repräsentiert die gesamte Woche ab diesem Datum.
     *
     * Es wird LocalDate verwendet, da nur das Datum (keine Uhrzeit)
     * für die Wochenplanung relevant ist.
     */
    @Column(name = "start_date", nullable = false)
    private LocalDate week;

    /**
     * Aktueller Status des Wochenplans.
     *
     * Wird als String in der Datenbank gespeichert (EnumType.STRING),
     * um Probleme bei späteren Enum-Erweiterungen zu vermeiden.
     *
     * Mögliche Werte:
     * - DRAFT
     * - PUBLISHED
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
}