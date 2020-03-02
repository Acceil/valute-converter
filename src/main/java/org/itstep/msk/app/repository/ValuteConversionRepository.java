package org.itstep.msk.app.repository;

import org.itstep.msk.app.entity.ValuteConversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValuteConversionRepository extends JpaRepository<ValuteConversion, Integer> {
}
