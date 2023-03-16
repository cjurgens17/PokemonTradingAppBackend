package com.poolstore.quickclean.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pokemon extends BaseEntity {

    @Column(name="name")
    private String name;

    @Column(name="weight")
    private int weight;

    @Column(name="image")
    private String image;

    @Column(name="index")
    private int index;

    @Column(name="backImage")
    private String backImage;

    @Column(name="abilities")
    private List<String> abilities = new ArrayList<>();

    @Column(name="statNames")
    private List<String> statNames = new ArrayList<>();

    @Column(name="baseStat")
    private List<Integer> baseStat = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

}
