package org.itstep.msk.app.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "rates")
public class Rate {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(targetEntity = Valute.class)
    @JoinColumn(name = "valute_id", referencedColumnName = "id")
    private Valute valute;

    @Column(name = "rate_date")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "rate_value")
    private Double value;

    public Integer getId() {
        return id;
    }

    public Valute getValute() {
        return valute;
    }

    public void setValute(Valute valute) {
        this.valute = valute;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
