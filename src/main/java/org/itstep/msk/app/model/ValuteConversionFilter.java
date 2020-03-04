package org.itstep.msk.app.model;

import org.itstep.msk.app.entity.Valute;

public class ValuteConversionFilter {
    private Valute valuteFrom;
    private Valute valuteTo;
    private Integer valueFrom;
    private Integer valueTo;
    private Integer resultFrom;
    private Integer resultTo;

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

    public Integer getValueFrom() {
        return valueFrom;
    }

    public void setValueFrom(Integer valueFrom) {
        this.valueFrom = valueFrom;
    }

    public Integer getValueTo() {
        return valueTo;
    }

    public void setValueTo(Integer valueTo) {
        this.valueTo = valueTo;
    }

    public Integer getResultFrom() {
        return resultFrom;
    }

    public void setResultFrom(Integer resultFrom) {
        this.resultFrom = resultFrom;
    }

    public Integer getResultTo() {
        return resultTo;
    }

    public void setResultTo(Integer resultTo) {
        this.resultTo = resultTo;
    }
}
