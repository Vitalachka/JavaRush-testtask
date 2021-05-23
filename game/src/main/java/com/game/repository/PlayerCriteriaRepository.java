package com.game.repository;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.PlayerFilterCriteria;
import com.game.entity.PlayerPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class PlayerCriteriaRepository {
    @PersistenceContext

    @Qualifier(value = "entityManager")
    private final EntityManager entityManager;
    private CriteriaBuilder criteriaBuilder;

    @Autowired
    public PlayerCriteriaRepository(EntityManagerFactory em) {
        this.entityManager = em.createEntityManager();
        this.criteriaBuilder = entityManager.getCriteriaBuilder();

    }

    public Page<Player> findAllWithFilters(PlayerPage playerPage, PlayerFilterCriteria playerFilterCriteria){
        CriteriaQuery<Player> criteriaQuery = criteriaBuilder.createQuery(Player.class);
        Root<Player> playerRoot = criteriaQuery.from(Player.class);
        Predicate predicate = getPredicate(playerFilterCriteria, playerRoot, playerPage);
        criteriaQuery.where(predicate);
        setOrder(playerPage, criteriaQuery, playerRoot);
        TypedQuery<Player> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(playerPage.getPageNumber()*playerPage.getPageSize());
        typedQuery.setMaxResults(playerPage.getPageSize());
        Pageable pageable = getPageable(playerPage);
        long playerCount = getPlayerCount(predicate);
        return new PageImpl<>(typedQuery.getResultList(), pageable, playerCount);
    }

    private Predicate getPredicate(PlayerFilterCriteria playerFilterCriteria, Root<Player> playerRoot, PlayerPage playerPage) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        List<Predicate> predicateList = new ArrayList<>();

        if(Objects.nonNull(playerFilterCriteria.getName())){
            predicateList.add(criteriaBuilder.like(playerRoot.get("name"), "%"+playerFilterCriteria.getName()+"%"));
        }

        if(Objects.nonNull(playerFilterCriteria.getTitle())){
            predicateList.add(criteriaBuilder.like(playerRoot.get("title"), "%"+playerFilterCriteria.getTitle()+"%"));
        }

        if(Objects.nonNull(playerFilterCriteria.getRace())){
            predicateList.add(criteriaBuilder.equal(playerRoot.get("race"),playerFilterCriteria.getRace()));
        }

        if(Objects.nonNull(playerFilterCriteria.getProfession())){
            predicateList.add(criteriaBuilder.equal(playerRoot.get("profession"),playerFilterCriteria.getProfession()));
        }

        if(Objects.nonNull(playerFilterCriteria.getBanned())){
            predicateList.add(criteriaBuilder.equal(playerRoot.get("banned"),playerFilterCriteria.getBanned()));
        }

        if(Objects.nonNull(playerFilterCriteria.getMinLevel()) && Objects.nonNull(playerFilterCriteria.getMaxLevel())){
            if(playerFilterCriteria.isMinLevel()){
                predicateList.add(criteriaBuilder.between(playerRoot.get("level"), playerFilterCriteria.getMinLevel(), playerFilterCriteria.getMaxLevel()));
            }
        }else{
            if(Objects.nonNull(playerFilterCriteria.getMinLevel())){
                predicateList.add(criteriaBuilder.greaterThan(playerRoot.get("level"),playerFilterCriteria.getMinLevel()));
            }

            if(Objects.nonNull(playerFilterCriteria.getMaxLevel())){
                predicateList.add(criteriaBuilder.lessThan(playerRoot.get("level"),playerFilterCriteria.getMaxLevel()));
            }
        }

        if(Objects.nonNull(playerFilterCriteria.getMinExperience()) && Objects.nonNull(playerFilterCriteria.getMaxExperience())){
            if(playerFilterCriteria.isMinExperience()){
                predicateList.add(criteriaBuilder.between(playerRoot.get("experience"), playerFilterCriteria.getMinExperience(), playerFilterCriteria.getMaxExperience()));
            }
        }else{
            if(Objects.nonNull(playerFilterCriteria.getMinExperience())){
                predicateList.add(criteriaBuilder.greaterThan(playerRoot.get("experience"),playerFilterCriteria.getMinExperience()));
            }

            if(Objects.nonNull(playerFilterCriteria.getMaxExperience())){
                predicateList.add(criteriaBuilder.lessThan(playerRoot.get("experience"),playerFilterCriteria.getMaxExperience()));
            }
        }

        if(Objects.nonNull(playerFilterCriteria.getBefore()) && Objects.nonNull(playerFilterCriteria.getAfter())){
            if(playerFilterCriteria.isMinExperience()){
                predicateList.add(criteriaBuilder.between(playerRoot.get("birthday"), playerFilterCriteria.getSqlAfter() , playerFilterCriteria.getSqlBefore()));
            }
        }else{
            if(Objects.nonNull(playerFilterCriteria.getAfter())){
                predicateList.add(criteriaBuilder.greaterThan(playerRoot.get("birthday"),playerFilterCriteria.getSqlAfter()));
            }

            if(Objects.nonNull(playerFilterCriteria.getBefore())){
                predicateList.add(criteriaBuilder.lessThan(playerRoot.get("birthday"),playerFilterCriteria.getSqlBefore()));
            }

            if(Objects.nonNull(playerFilterCriteria.getOrder()) && !playerFilterCriteria.getOrder().equals(PlayerOrder.ID)){
               playerPage.setSortBy(playerFilterCriteria.getOrder().getFieldName());
            }
        }

       return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
    }

    private void setOrder(PlayerPage playerPage, CriteriaQuery<Player> criteriaQuery, Root<Player> playerRoot) {
        if(playerPage.getSortDirection().equals(Sort.Direction.ASC)){
            criteriaQuery.orderBy(criteriaBuilder.asc(playerRoot.get(playerPage.getSortBy())));
        }else criteriaQuery.orderBy(criteriaBuilder.desc(playerRoot.get(playerPage.getSortBy())));

    }

    private Pageable getPageable(PlayerPage playerPage) {
        Sort sort = Sort.by(playerPage.getSortDirection(), playerPage.getSortBy());
        return PageRequest.of(playerPage.getPageNumber(), playerPage.getPageSize(), sort);
    }

    private long getPlayerCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Player> countRoot = countQuery.from(Player.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
