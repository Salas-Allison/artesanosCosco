package com.artesanoscosco.blog.api;

import com.artesanoscosco.blog.dao.ProduitRepository;
import com.artesanoscosco.blog.entity.Produit;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur CRUD pour les produits.
 */
@RestController
@RequestMapping("/api")
public class ProduitController
{
    private ProduitRepository mRepository;

    @Autowired
    public ProduitController(ProduitRepository pRepository)
    {
        mRepository = pRepository;
    }

    @GetMapping(path = "/produits")
    @ApiResponse(responseCode = "200", description = "Les ressources ont été trouvées et renvoyées avec succès.")
    public List<Produit> sondages()
    {
        List<Produit> all = mRepository.findAll();
        return all;
    }

    @GetMapping(path = "/produits/{id}")
    @ApiResponse(responseCode = "200", description = "La ressource a été trouvée et renvoyée avec succès.")
    @ApiResponse(responseCode = "404", description = "La ressource n'existe pas.")
    public ResponseEntity<Produit> getSondageById(@PathVariable Long id)
    {
        return ResponseEntity.of(mRepository.findById(id));
    }

    @PostMapping(path = "/produits")
    @ApiResponse(responseCode = "201", description = "La ressource a été créée avec succès.")
    @ApiResponse(responseCode = "400", description = "En cas d'erreur de validation.")
    public ResponseEntity<?> createSondage(
            @Valid @RequestBody Produit produit, BindingResult validation, HttpServletRequest request)
    {
        if (validation.hasErrors())
        {
            List<String> errors = validation.getAllErrors().stream()//
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)//
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errors);
        }

        // Force un identifiant à null dans le cas où le client envoie l'id d'un sondage qui existe déjà
        // pour tenter de le modifier.
        produit.setId(null);
        produit = mRepository.save(produit);

        // Construction de la réponse
        URI location = ServletUriComponentsBuilder.fromRequest(request)//
                .path("/{id}")//
                .buildAndExpand(produit.getId())//
                .toUri();
        return ResponseEntity.created(location).body(produit);
    }

    @PutMapping(path = "/produits/{id}")
    @ApiResponse(responseCode = "200", description = "La ressource a été mise à jour avec succès.")
    @ApiResponse(responseCode = "404", description = "La ressource à mettre à jour n'a pas été trouvée.")
    public ResponseEntity<?> updateProduit(
            @PathVariable Long id,
            @RequestBody @Valid Produit produit,
            BindingResult validation,
            HttpServletRequest request)
    {
        if (validation.hasErrors())
        {
            List<String> errors = validation.getAllErrors().stream()//
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)//
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errors);
        }

        Produit updated = mRepository.findById(id).map(s -> {
            s.setNom(produit.getNom());
            s.setDescription(produit.getDescription());
            s.setImage(produit.getImage());
            s.setPrix(produit.getPrix());

            return mRepository.save(s);
        }).orElseGet(() -> null);

        if (updated == null)
        {
            return ResponseEntity.notFound()//
                    .location(ServletUriComponentsBuilder.fromRequest(request).build().toUri())//
                    .build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(path = "/produits/{id}")
    @ApiResponse(responseCode = "200", description = "La ressource n'existe pas, requête ignorée.")
    @ApiResponse(responseCode = "204", description = "La ressource a été supprimée avec succès.")
    public ResponseEntity deleteProduit(@PathVariable Long id)
    {
        if (mRepository.existsById(id))
        {
            mRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().build();
    }
}