package org.itstep.msk.app.repository;

import org.itstep.msk.app.entity.Valute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValuteRepository extends JpaRepository<Valute, Integer> {
    Valute findByCharCode(String charCode);
}
