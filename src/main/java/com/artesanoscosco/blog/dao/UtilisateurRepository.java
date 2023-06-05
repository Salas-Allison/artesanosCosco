package com.artesanoscosco.blog.dao;

import com.artesanoscosco.blog.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

}
