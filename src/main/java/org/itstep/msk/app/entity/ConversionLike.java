package org.itstep.msk.app.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "conversion_likes")
public class ConversionLike extends Like {
    @ManyToOne(targetEntity = ValuteConversion.class)
    @JoinColumn(name = "conversion_id", referencedColumnName = "id")
    private ValuteConversion conversion;

    public ValuteConversion getConversion() {
        return conversion;
    }

    public void setConversion(ValuteConversion conversion) {
        this.conversion = conversion;
    }
}
