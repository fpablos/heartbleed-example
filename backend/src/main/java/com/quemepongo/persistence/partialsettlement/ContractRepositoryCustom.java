package com.quemepongo.persistence.partialsettlement;

import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepositoryCustom {

    void modifyContractDate(Long contractId, String deliveryDate);
}