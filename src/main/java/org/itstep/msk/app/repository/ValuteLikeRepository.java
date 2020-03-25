package org.itstep.msk.app.repository;

import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.entity.Valute;
import org.itstep.msk.app.entity.ValuteLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValuteLikeRepository extends JpaRepository<ValuteLike, Integer> {
    ValuteLike findByUserAndValute(User user, Valute valute);
}
