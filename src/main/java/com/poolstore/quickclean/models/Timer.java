package com.poolstore.quickclean.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="timer")
public class Timer extends BaseEntity {

    @Column(name="prevDate")
    private LocalDateTime prevDate;

    @OneToOne(mappedBy = "timer")
    @JsonManagedReference
    private User user;
}
