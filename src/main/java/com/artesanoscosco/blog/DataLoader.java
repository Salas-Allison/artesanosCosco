package com.artesanoscosco.blog;

import com.artesanoscosco.blog.dao.ProduitRepository;
import com.artesanoscosco.blog.entity.Produit;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements ApplicationRunner
{
    private final ProduitRepository mproduitRepository;

    @Autowired
    public DataLoader(ProduitRepository pproduitRepository)
    {
        this.mproduitRepository = pproduitRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception
    {
        if (mproduitRepository.count() == 0)
        {
            ClassPathResource resource = new ClassPathResource("static/json/artesanos.json");
            ObjectMapper objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
            List<Produit> produits = objectMapper.readValue(resource.getInputStream(), new TypeReference<>()
            {
            });
            mproduitRepository.saveAll(produits);
        }
    }

}
