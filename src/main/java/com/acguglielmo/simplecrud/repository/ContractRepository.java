package com.acguglielmo.simplecrud.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acguglielmo.simplecrud.entity.Contract;
import com.acguglielmo.simplecrud.entity.ContractId;

public interface ContractRepository extends JpaRepository<Contract, ContractId> {

	Optional<Contract> findByIdNumberAndIdCustomerCnpj(String number, String cnpj);

}
