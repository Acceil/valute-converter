package org.itstep.msk.app.repository;

import org.itstep.msk.app.entity.Rate;
import org.itstep.msk.app.entity.Valute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RateRepository extends JpaRepository<Rate, Integer> {
    List<Rate> findAllByDateOrderByValueAsc(Date date);

    Rate findFirstByValuteOrderByDateDesc(Valute valute);
}
