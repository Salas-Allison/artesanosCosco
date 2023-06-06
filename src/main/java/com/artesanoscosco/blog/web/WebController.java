package com.artesanoscosco.blog.web;

import com.artesanoscosco.blog.dao.ProduitRepository;
import com.artesanoscosco.blog.dao.UtilisateurRepository;
import com.artesanoscosco.blog.entity.Produit;
import com.artesanoscosco.blog.entity.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;
import java.util.Optional;

@Controller
public class WebController {

    private final ProduitRepository produitDao;
    private final UtilisateurRepository utilisateurDao;

    @Autowired
    public WebController(ProduitRepository produitDao, UtilisateurRepository utilisateurDao) {
        this.produitDao = produitDao;
        this.utilisateurDao = utilisateurDao;
    }

    @GetMapping(path="/")
    public String index(Model model) {
        Collection<Produit> produits = produitDao.findAll();

        model.addAttribute("produits", produits);

        return "index";
    }
/*
    @GetMapping("/description/{id}")
    public String description(@PathVariable("id") Long id, Model model) {
        // Récupérez le produit correspondant à l'ID et passez-le au modèle
        Produit produit = produitDao.findById(id);
        model.addAttribute("produit", produit);

        return "description";
    }
*/
    @GetMapping(path="/description/{id}")
    public String description(Model model, @PathVariable long id) {
        Optional<Produit> produit = produitDao.findById(id);

        model.addAttribute("produit", produit.get());

        return "description";
    }

    @GetMapping(path="/admin")
    public String admin(Model model) {
        Collection<Utilisateur> utilisateurs = utilisateurDao.findAll();

        model.addAttribute("utilisateurs", utilisateurs);

        return "admin";
    }
}

