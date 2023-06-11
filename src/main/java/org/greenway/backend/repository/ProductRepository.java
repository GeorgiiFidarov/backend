package org.greenway.backend.repository;

import org.greenway.backend.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllProductsByCategoryId(Long id);
    List<Product> findAllProductsByCategoryId(Long id, Pageable pageable);
    List<Product> findAllProductsByCategoryIdAndNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
        Long id, String name, String description,Pageable pageable);

}
