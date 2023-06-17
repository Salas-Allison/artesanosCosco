package com.artesanoscosco.blog.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "produit")
public class Produit
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nom",nullable = true,length = 50)
    private String nom;
    @Column(name = "description",nullable = true,length = 255)
    private String description;
    @Column(name = "image",nullable = true,length = 255)
    private String image;
    @Column(name = "prix",nullable = true,length = 255)
    private double prix;

    public Produit(){

    }

    public Produit(Long id, String nom, String description, double prix, String image) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

}
