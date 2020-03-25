package org.itstep.msk.app.repository;

import org.itstep.msk.app.entity.ConversionLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversionLikeRepository extends JpaRepository<ConversionLike, Integer> {
}
