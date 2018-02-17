/*
 * This file is generated by jOOQ.
*/
package jooq.jooqobjects.tables.pojos;


import java.io.Serializable;
import java.math.BigDecimal;

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
public class PriceType implements Serializable {

    private static final long serialVersionUID = 1431816794;

    private Integer    id;
    private String     name;
    private BigDecimal value;

    public PriceType() {}

    public PriceType(PriceType value) {
        this.id = value.id;
        this.name = value.name;
        this.value = value.value;
    }

    public PriceType(
        Integer    id,
        String     name,
        BigDecimal value
    ) {
        this.id = id;
        this.name = name;
        this.value = value;
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

    public BigDecimal getValue() {
        return this.value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("PriceType (");

        sb.append(id);
        sb.append(", ").append(name);
        sb.append(", ").append(value);

        sb.append(")");
        return sb.toString();
    }
}
