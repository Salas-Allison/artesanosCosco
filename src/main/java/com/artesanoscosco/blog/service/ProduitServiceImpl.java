package com.artesanoscosco.blog.service;

import com.artesanoscosco.blog.dao.ProduitRepository;
import com.artesanoscosco.blog.entity.Produit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduitServiceImpl implements ProduitService {

    @Autowired
    private ProduitRepository repository;

    @Override
    public List<Produit> listarProduits() {
        return repository.findAll();
    }

    @Override
    public Produit saveProduit(Produit produit) {
        return repository.save(produit);
    }

    @Override
    public Produit produitID(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public Produit updateproduit(Produit produit) {

        return repository.save(produit);
    }

    @Override
    public void deleteproduit(Long id) {
        repository.deleteById(id);
    }
}

