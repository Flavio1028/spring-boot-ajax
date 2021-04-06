package com.codeup.repository;

import java.util.List;

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