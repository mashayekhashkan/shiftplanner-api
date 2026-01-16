package com.shiftplanner.shiftplanner_api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Entity
@Table(name = "app_user")
@Getter
@Setter
@NoArgsConstructor
public class User {
    public enum Rolle {
        MANAGER, ADMIN
    }
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true, length = 255)
    private String email;
    @Column(nullable = false)
    private String passwordHash;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rolle rolle;
    @ManyToOne(optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
}
