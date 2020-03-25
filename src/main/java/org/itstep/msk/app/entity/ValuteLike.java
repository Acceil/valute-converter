package org.itstep.msk.app.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "valute_likes")
public class ValuteLike extends Like {
    @ManyToOne(targetEntity = Valute.class)
    @JoinColumn(name = "valute_id", referencedColumnName = "id")
    private Valute valute;

    public Valute getValute() {
        return valute;
    }

    public void setValute(Valute valute) {
        this.valute = valute;
    }
}
