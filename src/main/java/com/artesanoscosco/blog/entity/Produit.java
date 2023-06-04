package com.artesanoscosco.blog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Produit {
    private Long id;
    private String nom;
    private String description;
    private String prix;

}
