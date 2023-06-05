package com.artesanoscosco.blog.api;

import com.artesanoscosco.blog.dao.UtilisateurRepository;
import com.artesanoscosco.blog.entity.Utilisateur;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class UtilisateurController {

    private final UtilisateurRepository utilisateurDao;

    @Autowired
    public UtilisateurController(UtilisateurRepository utilisateurDao) {
        this.utilisateurDao = utilisateurDao;
    }

    @GetMapping("/utilisateurs")
    public String getUtilisateurs(Model model) {
        List<Utilisateur> utilisateurs = utilisateurDao.findAll();
        model.addAttribute("utilisateurs", utilisateurs);
        return "utilisateurs";
    }

    @GetMapping("/utilisateurs/{id}")
    public String getUtilisateur(@PathVariable Long id, Model model) {
        Optional<Utilisateur> utilisateurOptional = utilisateurDao.findById(id);
        if (utilisateurOptional.isPresent()) {
            Utilisateur utilisateur = utilisateurOptional.get();
            model.addAttribute("utilisateur", utilisateur);
        } else {
            // Gérer le cas où l'utilisateur n'existe pas
        }
        return "utilisateur";
    }

    @GetMapping("/ajouter-utilisateur")
    public String formulaireAjouterUtilisateur(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        return "ajouter-utilisateur";
    }

    @PostMapping("/ajouter-utilisateur")
    public String ajouterUtilisateur(@ModelAttribute @Valid Utilisateur utilisateur, BindingResult result) {
        if (result.hasErrors()) {
            return "ajouter-utilisateur";
        }
        utilisateurDao.save(utilisateur);
        return "redirect:/utilisateurs";
    }

    @GetMapping("/editer-utilisateur/{id}")
    public String formulaireEditerUtilisateur(@PathVariable Long id, Model model) {
        Optional<Utilisateur> utilisateurOptional = utilisateurDao.findById(id);
        if (utilisateurOptional.isPresent()) {
            Utilisateur utilisateur = utilisateurOptional.get();
            model.addAttribute("utilisateur", utilisateur);
            return "editer-utilisateur";
        } else {
            // Gérer le cas où l'utilisateur n'existe pas
            return "redirect:/utilisateurs";
        }
    }

    @PostMapping("/editer-utilisateur/{id}")
    public String editerUtilisateur(@PathVariable Long id, @ModelAttribute @Valid Utilisateur utilisateur,
                                    BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("utilisateur", utilisateur);
            return "editer-utilisateur";
        }
        utilisateur.setId(id);
        utilisateurDao.save(utilisateur);
        return "redirect:/utilisateurs";
    }

    @GetMapping("/supprimer-utilisateur/{id}")
    public String supprimerUtilisateur(@PathVariable Long id) {
        utilisateurDao.deleteById(id);
        return "redirect:/utilisateurs";
    }
}
