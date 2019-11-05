package com.quemepongo.persistence.partialsettlement;

import com.quemepongo.domain.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract,Long>, ContractRepositoryCustom {


}
