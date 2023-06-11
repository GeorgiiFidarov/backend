package org.greenway.backend.service;

import org.greenway.backend.model.Category;
import org.greenway.backend.model.Product;
import org.greenway.backend.repository.CategoryRepository;
import org.greenway.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }
    public List<Category> getAllCategories(){
        return categoryRepository.findAllByOrderByIdAsc();
    }
    public void createCategory(Category category){
        categoryRepository.save(category);
    }
    public Category getCategoryByName(String name){
        return categoryRepository.findByCategoryName(name);
    }
    public Optional<Category> getCategoryById(Long id){
        return categoryRepository.findById(id);
    }

    public void deleteCategory(Category category){
        List<Product> defaultCategoryProducts = productRepository.findAllProductsByCategoryId(category.getId());
        if (Objects.nonNull(defaultCategoryProducts)){
            for (Product product: defaultCategoryProducts){
                product.setCategory(getCategoryById(0L).orElse(null));
            }
        }
        categoryRepository.delete(category);
    }
    public void deleteAllCategories(){
        categoryRepository.deleteAll();
    }
    public void updateCategory(Long id, Category newCategory){
        Category category = categoryRepository.findById(id).get();
        category.setCategoryName(newCategory.getCategoryName());
        category.setDescription(newCategory.getDescription());
        category.setImageUrl(newCategory.getImageUrl());
        categoryRepository.save(category);
    }
}
