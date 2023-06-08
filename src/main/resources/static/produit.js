/**
 * @typedef Produit
 * @param {number} Produit.id
 * @param {string} Produit.nom
 * @param {string} Produit.description
 * @param {string} Produit.image
 * @param {string} Produit.prix
 */

const BASEURL_WEBSERVICE_PRODUITS = "/api/produits";
const BASEURL_PRODUITS = "/produits";


const BTN_REFRESH = "btn-refresh-pollings";
const DIV_PRODUITS = "produits";
const FORM_CREATE_PRODUIT = "form-creation-produit";

document.onreadystatechange = () => {
    if (document.readyState === "complete") {
        let btnRefreshPollings = document.getElementById(BTN_REFRESH);
        let formCreatePolling = document.getElementById(FORM_CREATE_PRODUIT);
        btnRefreshPollings.addEventListener("click", refreshAllProduit);
        formCreatePolling.addEventListener("submit", createNewPolling);
    }
}

/**
 * Recharge la liste des produits.
 * @param {MouseEvent} event
 */
function refreshAllProduit(event) {

    let pageCourante = document.querySelector(".page-courante").textContent;
    pageCourante -= 1;

    fetch(BASEURL_PRODUITS+'?page='+pageCourante)
        .then(result => result.text())
        .then(text => {
            let div = document.getElementById(DIV_PRODUIT);
            let documentFragment = document.createRange().createContextualFragment(text);
            div.innerHTML = documentFragment.firstChild.innerHTML;
        })
    ;
}

/**
 * Ajoute un nouveau sondage dans la liste sans devoir tout recharger.
 * @param {Produit} produit
 */
function updateListWith(produit) {
    console.log("Mise à jour de la page avec le nouveau produit : ");
    console.log(produit);

    fetch(BASEURL_FRAGMENT_SONDAGES + '/' + sondage.id)
        .then(response => response.text())
        .then(text => {
            const div = document.getElementById(DIV_SONDAGES);
            let documentFragment = document.createRange().createContextualFragment(text);
            div.prepend(documentFragment.firstChild);
        });
}

/**
 * Envoie les données du sondage vers l'URL "/api/sondages" avec méthode POST.
 *
 * @param {Event} event L'évènement de click.
 */
function createNewPolling(event) {

    // Pour éviter d'envoyer une requête POST par défaut via le navigateur
    // on désactive le comportement par défaut du bouton submit car on va
    // gérer nous-même la requête en mode 'fetch'
    event.preventDefault();

    // On transforme les champs du formulaire au format JSON
    let form = document.getElementById(FORM_CREATE_SONDAGE);
    let formData = new FormData(form);
    let json = JSON.stringify(Object.fromEntries(formData.entries()));

    console.debug("Sending data to server : \n" + json);

    // Envoi des données au WebService
    fetch(BASEURL_WEBSERVICE_SONDAGES, {
        method: 'POST',
        body: json,
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (response.ok) {
            // La requête s'est bien passée
            console.log("Sondage créé avec succès !");
            response.json().then(sondage => updateListWith(sondage));
        } else {
            // La requête a échoué
            console.error("Erreur lors de la création du sondage !");
            response.json().then(err => console.error(err));
        }
    }).catch(error => {
        console.error("Erreur lors de la requête :", error);
    });
}