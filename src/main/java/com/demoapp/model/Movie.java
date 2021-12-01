/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demoapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "MOVIE")
public class Movie implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Column(name="YEAR")
    private Integer year;
    
    @Column(name="TITLE")
    private String title;
    
    @Column(name="STUDIOS")
    private String studios;
    
//    @Column(name="PRODUCERS")
//    private String producers;
    
    @Column(name="WINNER")
    private boolean winner;
    
    @JoinTable(name = "MOVIE_PRODUCER",
        joinColumns = {@JoinColumn(name = "ID_MOVIE", referencedColumnName = "ID")},
        inverseJoinColumns = {@JoinColumn(name = "ID_PRODUCER", referencedColumnName = "ID")})
    @ManyToMany(fetch = FetchType.LAZY)
    @OrderBy(value = "id DESC")
    private List<Producer> producers = new ArrayList<>();
    
}
