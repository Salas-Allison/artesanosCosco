package com.artesanoscosco.blog.dao;

import com.artesanoscosco.blog.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

}
