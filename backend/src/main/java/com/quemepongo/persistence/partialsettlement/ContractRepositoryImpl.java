package com.quemepongo.persistence.partialsettlement;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ContractRepositoryImpl implements ContractRepositoryCustom {

    @PersistenceContext
    EntityManager em;


    @Override
    public void modifyContractDate(Long contractId, String deliveryDate) {
        em
                .createNativeQuery("UPDATE quemepongo.contracts SET delivery_date = \'" + deliveryDate + "\' WHERE id = " + contractId)
                .executeUpdate();
    }
}



