package com.codeup.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.codeup.domain.Promocao;

@Repository
public interface PromocaoRepository extends JpaRepository<Promocao, Long> {
	
	@Query("SELECT count(p.id) as count, max(p.dtCadastro) as lastDate FROM Promocao p WHERE p.dtCadastro > :data")
	public Map<String, Object> totalAndUltimaPromocaoByDataCadastro(@Param("data") LocalDateTime data);
	
	@Query("SELECT p.dtCadastro FROM Promocao p")
	public Page<LocalDateTime> findUltimaDataPromocao(Pageable pageable);
	
	@Query("SELECT p FROM Promocao p WHERE p.preco = :preco ")
	public Page<Promocao> findByPreco(@Param("preco") BigDecimal preco, Pageable page);

	@Query("SELECT p FROM Promocao p WHERE p.titulo LIKE %:search% OR p.site LIKE %:search% OR p.categoria.titulo LIKE %:search%")
	public Page<Promocao> findByTituloOrSiteOrCategoria(@Param("search") String search, Pageable page);

	@Query("SELECT p FROM Promocao p WHERE p.site LIKE :site")
	public Page<Promocao> findBySite(@Param("site") String site, Pageable page);

	@Query("SELECT DISTINCT p.site FROM Promocao p WHERE p.site LIKE %:site%")
	public List<String> findSitesByTermo(@Param("site") String site);

	@Transactional(readOnly = false)
	@Modifying
	@Query("UPDATE Promocao p SET p.likes = p.likes + 1 WHERE p.id = :id")
	public void updateSomarLikes(@Param("id") Long id);

	@Query("SELECT p.likes FROM Promocao p WHERE p.id = :id")
	public int findLikesById(@Param("id") Long id);

}