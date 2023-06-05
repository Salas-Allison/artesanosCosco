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

import java.util.Collection;

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
        Sort.Order o = Sort.Order.desc("prix");
        Collection<Produit> produits = produitDao.findAll(Sort.by(o));

        model.addAttribute("produits", produits);

        return "index";
    }

    @GetMapping(path="/admin")
    public String admin(Model model) {
        Collection<Utilisateur> utilisateurs = utilisateurDao.findAll();

        model.addAttribute("utilisateurs", utilisateurs);

        return "admin";
    }
}

