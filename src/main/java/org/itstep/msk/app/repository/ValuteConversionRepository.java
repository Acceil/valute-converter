package org.itstep.msk.app.repository;

import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.entity.Valute;
import org.itstep.msk.app.entity.ValuteConversion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

@Repository
public interface ValuteConversionRepository extends JpaRepository<ValuteConversion, Integer>, JpaSpecificationExecutor<ValuteConversion> {
    Page<ValuteConversion> findAllByUser(User user, Pageable pagination);

    static Specification<ValuteConversion> hasUser(User user) {
        return (conversion, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(
                        conversion.get("user"),
                        user
                );
    }

    // Спецификация для фильтрации конвертаций по исходной валюте
    static Specification<ValuteConversion> hasValuteFrom(Valute valuteFrom) {
        return (conversion, criteriaQuery, criteriaBuilder) ->
            criteriaBuilder.equal(
                    conversion.get("valuteFrom"),
                    valuteFrom
            );
    }

    static Specification<ValuteConversion> hasValuteTo(Valute valuteTo) {
        return (conversion, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(
                        conversion.get("valuteTo"),
                        valuteTo
                );
    }

    static Specification<ValuteConversion> hasValue(Integer from, Integer to) {
        return (conversion, criteriaQuery, criteriaBuilder) -> {
            Expression<Integer> value = conversion.get("value");

            Predicate fromPredicate = from >= 0
                    ? criteriaBuilder.greaterThanOrEqualTo(value, from)
                    : criteriaBuilder.greaterThanOrEqualTo(value, 0);

            Predicate toPredicate = to >= 0
                    ? criteriaBuilder.lessThanOrEqualTo(value, to)
                    : criteriaBuilder.greaterThanOrEqualTo(value, 0);

            return criteriaBuilder.and(
                    fromPredicate,
                    toPredicate
            );
        };
    }

    static Specification<ValuteConversion> hasResult(Integer from, Integer to) {
        return (conversion, criteriaQuery, criteriaBuilder) -> {
            Expression<Integer> result = conversion.get("result");

            Predicate fromPredicate = from >= 0
                    ? criteriaBuilder.greaterThanOrEqualTo(result, from)
                    : criteriaBuilder.greaterThanOrEqualTo(result, 0);

            Predicate toPredicate = to >= 0
                    ? criteriaBuilder.lessThanOrEqualTo(result, to)
                    : criteriaBuilder.greaterThanOrEqualTo(result, 0);

            return criteriaBuilder.and(
                    fromPredicate,
                    toPredicate
            );
        };
    }
}
