/*
 * This file is generated by jOOQ.
*/
package jooq.jooqobjects.tables.pojos;


import java.io.Serializable;

import javax.annotation.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Film implements Serializable {

    private static final long serialVersionUID = 1282088653;

    private Integer id;
    private String  name;
    private Integer filmTypeId;

    public Film() {}

    public Film(Film value) {
        this.id = value.id;
        this.name = value.name;
        this.filmTypeId = value.filmTypeId;
    }

    public Film(
        Integer id,
        String  name,
        Integer filmTypeId
    ) {
        this.id = id;
        this.name = name;
        this.filmTypeId = filmTypeId;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFilmTypeId() {
        return this.filmTypeId;
    }

    public void setFilmTypeId(Integer filmTypeId) {
        this.filmTypeId = filmTypeId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Film (");

        sb.append(id);
        sb.append(", ").append(name);
        sb.append(", ").append(filmTypeId);

        sb.append(")");
        return sb.toString();
    }
}
