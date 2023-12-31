package com.joseleonardo.lojavirtual.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.joseleonardo.lojavirtual.model.ContaReceber;

@Repository
@Transactional
public interface ContaReceberRepository extends JpaRepository<ContaReceber, Long> {

}
