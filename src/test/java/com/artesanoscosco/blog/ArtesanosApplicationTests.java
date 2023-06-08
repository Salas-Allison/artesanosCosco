package com.artesanoscosco.blog;

import com.artesanoscosco.blog.entity.Produit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@SpringBootTest
class ArtesanosApplicationTests {

	private final String URL = "http://localhost:8080/api/produits";

	/**
	 * Client REST permettant de faire des appels distants.
	 */
	private RestTemplate restTemplate;

	@Bean // Indique qu'il s'agit d'un Bean de configuration pour Spring.
	@Primary // Indique que si plusieurs Beans sont trouvés par Spring, celui-ci doit avoir la priorité.
	public DataSource dataSource()
	{
		return new EmbeddedDatabaseBuilder() //
				.generateUniqueName(true) //
				.setType(EmbeddedDatabaseType.H2) //
				.ignoreFailedDrops(true) //
				.setScriptEncoding("UTF-8") //
				//.addScripts("user_data.sql") // Pour charger un jeu de données de test par exemple
				.build();
	}

	private Produit produit;

	@BeforeEach
	public void init()
	{
		this.restTemplate = new RestTemplate();

		this.produit = new Produit();
		this.produit.setDescription("Un produit");

	}

	/**
	 * Test du WebService "GET /produits".
	 */
	@Test
	public void testFindAll()
	{
		ResponseEntity<Produit[]> response = restTemplate.getForEntity(URL, Produit[].class);
		Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

		for (Produit produit : response.getBody())
		{
			Assertions.assertNotNull(produit);
		}
	}

	/**
	 * Test du WebService "POST /produits".
	 */
	@Test
	public void testCreate()
	{
		// Requête du service "POST /sondages" pour créer l'entité
		ResponseEntity<Produit> response = restTemplate.postForEntity(URL, produit, Produit.class);

		// Vérifications
		Assertions.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
		URI location = response.getHeaders().getLocation();
		Produit body = response.getBody();
		long idOfCreated = Long.parseLong(location.getPath().substring(1+location.getPath().lastIndexOf('/')));

		// Vérification des données reçues dans la réponse HTTP
		Assertions.assertNotNull(location);
		Assertions.assertNotNull(body);
		Assertions.assertNotNull(body.getId());
		Assertions.assertEquals(idOfCreated, body.getId());
		Assertions.assertEquals(produit.getNom(), body.getNom());
		Assertions.assertEquals(produit.getDescription(), body.getDescription());



		// Re-demande l'objet créé pour vérifier que les données sont identiques
		response = restTemplate.getForEntity(location, Produit.class);
		Produit created = response.getBody();
		Assertions.assertNotNull(created);
		Assertions.assertNotNull(created.getId());
		Assertions.assertEquals(idOfCreated, body.getId());
		Assertions.assertEquals(produit.getNom(), created.getNom());
		Assertions.assertEquals(produit.getDescription(), created.getDescription());

	}

	/**
	 * Test du WebService "PUT /produits/{id}".
	 */
	@Test
	public void testUpdate()
	{
		// Création d'un nouveau produit
		ResponseEntity<Produit> response = restTemplate.postForEntity(URL, produit, Produit.class);  // POST /produit
		Long newProduitId = response.getBody().getId();
		Assertions.assertNotNull(newProduitId);

		// Mise à jour (nouvelle URL = url + "/" + newProduitId)
		produit.setNom("New Nom");
		produit.setDescription("New description");
		restTemplate.put(URL + "/" + newProduitId, produit);  // PUT /produits/{id}

		// Requête du nouveau produit avec son ID
		response = restTemplate.getForEntity(URL + "/" + newProduitId, Produit.class); // GET /produits/{id}
		Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Assertions.assertEquals(response.getBody().getNom(), produit.getNom());
		Assertions.assertEquals(response.getBody().getDescription(), produit.getDescription());

	}

	/**
	 * Test du WebService "PUT /produits/{id}".
	 */
	@Test
	public void testUpdateWithBadValues()
	{
		// Création d'un nouveau sondage
		ResponseEntity<Produit> response = restTemplate.postForEntity(URL, produit, Produit.class);  // POST /produit
		Long newProduitId = response.getBody().getId();
		Assertions.assertNotNull(newProduitId);

		// Mise à jour (nouvelle URL = url + "/" + newProduitId)
		produit.setNom("Modified nom");
		produit.setDescription("Modified description");



		// L'appel au WebService doit échouer
		Assertions.assertThrows(HttpClientErrorException.BadRequest.class, () -> restTemplate.put(URL + "/" + newProduitId, produit));  // PUT /sondages/{id}
	}

	/**
	 * Test du WebService "DELETE /produits/{id}".
	 */
	@Test
	public void testDelete()
	{
		ResponseEntity<Produit> response = restTemplate.postForEntity(URL, produit, Produit.class);
		Produit body = response.getBody();
		Assertions.assertNotNull(body.getId());

		// Re-lecture de l'entité via le WebService avec l'ID de l'objet retourné précédemment
		// pour vérifier que l'objet a bien été créé en BDD
		ResponseEntity<Produit> readSondage = Assertions.assertDoesNotThrow(() -> {
			return restTemplate.getForEntity(URL + "/" + body.getId(), Produit.class);
		});

		// Suppression de l'objet
		Assertions.assertDoesNotThrow(() -> {
			restTemplate.delete(URL + "/" + body.getId());  // DELETE /sondages/{id}
		});

		// Re-lecture de l'entité via le WebService avec l'ID de l'objet
		// pour vérifier que l'objet n'existe plus (le webService doit générer une erreur 404)
		HttpClientErrorException.NotFound err = Assertions.assertThrows(HttpClientErrorException.NotFound.class, () -> {
			restTemplate.getForEntity(URL + "/" + body.getId(), Produit.class);
		});
		Assertions.assertEquals(404, err.getStatusCode().value());
	}

}
