package com.artesanoscosco.blog.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecordNotFoundException extends RuntimeException
{
    private Object id;

    /**
     * Constructeur.
     *
     * @param id L'identifiant de l'entité.
     */
    public RecordNotFoundException(Object id)
    {
        super();
        this.id = id;
    }

    /**
     * @return l'identifiant de l'objet introuvable en BDD.
     */
    public Object getId()
    {
        return this.id;
    }
}
