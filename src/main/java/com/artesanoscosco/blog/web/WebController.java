package com.artesanoscosco.blog.web;

import com.artesanoscosco.blog.dao.ProduitRepository;
import com.artesanoscosco.blog.entity.Produit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@Controller
public class WebController
{
    private final ProduitRepository produitDao;

    @Autowired
    public WebController(ProduitRepository pproduitDao)
    {
        this.produitDao = pproduitDao;
    }
    @GetMapping(path="/")
    public String index(Model model){
        Sort.Order o = Sort.Order.desc("prix");
        Collection<Produit> produits = produitDao.findAll(Sort.by(o));

        model.addAttribute("produits", produits);

        return "index";
    }
}
