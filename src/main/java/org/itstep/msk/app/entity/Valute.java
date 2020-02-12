package org.itstep.msk.app.entity;

import javax.persistence.*;

@Entity
@Table(name = "valutes")
public class Valute {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cbr_id", unique = true)
    private String cbrId;

    @Column(name = "num_code")
    private Integer numCode;

    @Column(name = "char_code")
    private String charCode;

    @Column
    private Integer nominal;

    @Column
    private String name;

    public Integer getId() {
        return id;
    }

    public String getCbrId() {
        return cbrId;
    }

    public void setCbrId(String cbrId) {
        this.cbrId = cbrId;
    }

    public Integer getNumCode() {
        return numCode;
    }

    public void setNumCode(Integer numCode) {
        this.numCode = numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public Integer getNominal() {
        return nominal;
    }

    public void setNominal(Integer nominal) {
        this.nominal = nominal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
