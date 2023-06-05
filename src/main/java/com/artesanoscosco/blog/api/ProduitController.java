package com.artesanoscosco.blog.api;

import com.artesanoscosco.blog.dao.ProduitRepository;
import com.artesanoscosco.blog.entity.Produit;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;

@Controller
public class ProduitController
{
    private final ProduitRepository produitDao;

    @Autowired
    public ProduitController(ProduitRepository pproduitDao)
    {
        this.produitDao = pproduitDao;
    }

    @GetMapping(path = "/produits")
    public String getproduits(
            @RequestParam(required = false, defaultValue = "prix") String sort,
            @RequestParam(required = false) String order,
            Model model)
    {
        Sort.Order o = "desc".equalsIgnoreCase(order) ? Sort.Order.desc(sort) : Sort.Order.asc(sort);
        Collection<Produit> produits = produitDao.findAll(Sort.by(o));

        // Pour inverser le tri la prochaine fois qu'on clique, on modifie la variable "order"
        // avant de l'injecter dans le modèle
        order = "asc".equalsIgnoreCase(order) ? "desc" : "asc";
        model.addAttribute("produits", produits);
        model.addAttribute("order", order);
        model.addAttribute("sort", sort);
        return "produits";
    }


    @GetMapping(path = "/ajouter-produit")
    public String formulaireAjouterproduit(Model model)
    {
        model.addAttribute("produit", new Produit());
        return "ajouter-produit";
    }

    @PostMapping(path = "/ajouter-produit")
    public String actionAjouterproduit(
            @ModelAttribute("produit") @Valid Produit produit, BindingResult validation, Model model)
    {
        model.addAttribute("produit", produit);
        model.addAttribute("produitId", produit.getId());

        if (!validation.hasErrors())
        {
            produitDao.save(produit);
            return "redirect:/produits";
        }
        else
        {
            return "ajouter-produit";
        }
    }

    @GetMapping(path = "/editer-produit")
    public String formulaireEditerproduit(@RequestParam Long produitId, Model model)
    {
        model.addAttribute("produit", produitDao.findById(produitId));
        return "editer-produit";
    }

    @PostMapping(path = "/editer-produit")
    public String actionEditerproduit(
            @ModelAttribute @Valid Produit produit, BindingResult validation, RedirectAttributes model)
    {
        model.addAttribute("produit", produit);
        model.addAttribute("produitId", produit.getId());

        if (!validation.hasErrors())
        {
            produitDao.save(produit);
            return "redirect:/produits";
        }
        else
        {
            return "editer-produit";
        }
    }
}
