package com.poolstore.quickclean.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="timer")
public class Timer extends BaseEntity {

    @Column(name="prevDate")
    private Date prevDate;

    @OneToOne(mappedBy = "timer")
    private User user;
}
