package com.realestate.backendrealestate.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "client",fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
    private List<Property> properties;

    @OneToMany(mappedBy = "client",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SubscriptionClient> subscriptionClients;

//    @OneToMany(mappedBy = "employee",cascade = CascadeType.ALL)
//    private List<Education> educations;
//    @OneToMany(mappedBy = "employee",cascade = CascadeType.ALL)
//    private List<Certification> certifications;
//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "employee_language",
//            joinColumns = @JoinColumn(name = "employee_id"),
//            inverseJoinColumns = @JoinColumn(name = "language_id")
//    )
//    private List<Language> languages;
//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "employee_skill",
//            joinColumns = @JoinColumn(name = "employee_id"),
//            inverseJoinColumns = @JoinColumn(name = "skill_id")
//    )
//    private List<Skill> skills;
//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "job_application",
//            joinColumns = @JoinColumn(name = "employee_id"),
//            inverseJoinColumns = @JoinColumn(name = "job_id")
//    )
//    private List<Job> jobs;
}
