package com.codeup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codeup.domain.Promocao;

@Repository
public interface PromocaoRepository extends JpaRepository<Promocao, Long> {

}