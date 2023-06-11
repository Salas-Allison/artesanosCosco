package com.artesanoscosco.blog.service;

import com.artesanoscosco.blog.entity.Produit;

import java.util.List;

public interface ProduitService {
    public List<Produit> listarProduits();
    public  Produit saveProduit(Produit produit);
    public Produit produitID(Long id);
    public Produit updateproduit(Produit produit);
    public void deleteproduit(Long id);


}
