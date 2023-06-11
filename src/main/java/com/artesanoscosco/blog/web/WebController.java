package com.artesanoscosco.blog.web;

import com.artesanoscosco.blog.dao.ProduitRepository;
import com.artesanoscosco.blog.entity.Produit;
import com.artesanoscosco.blog.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collection;
import java.util.Optional;

@Controller
public class WebController {

    @Autowired
    private ProduitService produitService;



    @GetMapping({ "/","index"})
    public String index(Model model) {
        model.addAttribute("produits",
                produitService.listarProduits());
        return "index";
    }

    @GetMapping(path = "/description/{id}")
    public String description(Model model, @PathVariable long id) {
        Produit produitExist=produitService.produitID(id);

        model.addAttribute("produit", produitExist);

        return "description";
    }

    //Maping add product view  createProduit html
    @GetMapping(path = "/admin/createProduit")
    public String createProduit(Model model) {
        Produit produit = new Produit();
        model.addAttribute("produit", produit);
        return "admin/createProduit"; //html
    }

    // post for create product save post data
    @PostMapping(path = "/admin/createProduit")
    public String createProduitSave(@ModelAttribute("produit")
                                    Produit produit ) {
        produitService.saveProduit(produit);
        return "redirect:/";
    }

    @GetMapping("/admin/viewProduit/{id}")
    public String viewProduitEdit(@PathVariable Long id,Model modelo){
        //search produit by id
        modelo.addAttribute("produit",produitService.produitID(id));

        return "admin/editProduit"; //html
    }
    @PostMapping("/admin/updateProduit/{id}")
    public String updateProduitEdit(@PathVariable Long id,
                                    @ModelAttribute("produit") Produit produit,
                                    Model model){
        Produit produitExist=produitService.produitID(id);
        produitExist.setId(id);
        produitExist.setDescription(produit.getDescription());
        produitExist.setImage(produit.getImage());
        produitExist.setNom(produit.getNom());
        produitExist.setPrix(produit.getPrix());

        produitService.updateproduit(produitExist);
        // return "admin/editProduit"; //html
        return "redirect:/"; //html
    }
    @GetMapping("/admin/deleteProduit/{id}")
    public String deleteProduit(@PathVariable Long id){
        produitService.deleteproduit(id);
        return "redirect:/";
    }


}

