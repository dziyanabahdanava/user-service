package com.epam.ms.repository.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user_profiles", schema = "users")
@Data
public class UserProfile {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @NotNull(message = "Please enter your age")
    @Max(120)
    @Min(1)
    private int age;
    @NotNull(message = "Please enter your height")
    @Max(250)
    @Min(100)
    private int height;
    @NotNull(message = "Please enter your weight")
    @Max(200)
    @Min(1)
    private int weight;
    @NotNull(message = "Please enter your goal")
    @Enumerated(EnumType.STRING)
    private Goal goal;
    @NotNull(message = "Please enter your physical activity")
    @Enumerated(EnumType.STRING)
    private PhysicalActivity physicalActivity;
}
