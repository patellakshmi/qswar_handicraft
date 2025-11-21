package com.qswar.hc.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * Represents the 'employee' table.
 * Includes a self-referencing relationship for the manager.
 */
@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer Id;

    @Column(name = "qswar_id")
    private String qSwarId;

    @Column(name = "gov_emp_id", unique = true, nullable = true, length = 20)
    private String govEmpId;

    @Column(name = "username")
    private String username;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "position", length = 50)
    private String position;

    @Column(name = "authorised")
    private String authorised; // Maps to BOOLEAN DEFAULT TRUE

    @Column(name = "department", length = 50)
    private String department;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "user_photo_link", nullable = true, length = 512)
    private String userPhotoLink;

    // --- Relationships ---
    @ToString.Exclude
    // Many employees can have one manager (self-referencing FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_emp_id", referencedColumnName = "id")
    private Employee manager;

    @ToString.Exclude
    // One manager can have many subordinates
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    private List<Employee> subordinates;

    @ToString.Exclude
    // One employee can have many visits
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Visit> visits;


    public Employee(String qSwarId, String username, String phone, String email, String password){
        this.qSwarId = qSwarId;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }


}