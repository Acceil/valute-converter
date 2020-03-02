package org.itstep.msk.app.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "valute_conversions")
public class ValuteConversion {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(targetEntity = Valute.class)
    @JoinColumn(name = "valute_from_id", referencedColumnName = "id")
    private Valute valuteFrom;

    @ManyToOne(targetEntity = Valute.class)
    @JoinColumn(name = "valute_to_id", referencedColumnName = "id")
    private Valute valuteTo;

    @Column(name = "conversion_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "conversion_value")
    private Double value;

    @Column(name = "conversion_result")
    private Double result;

    public Integer getId() {
        return id;
    }

    public Valute getValuteFrom() {
        return valuteFrom;
    }

    public void setValuteFrom(Valute valuteFrom) {
        this.valuteFrom = valuteFrom;
    }

    public Valute getValuteTo() {
        return valuteTo;
    }

    public void setValuteTo(Valute valuteTo) {
        this.valuteTo = valuteTo;
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

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }
}
