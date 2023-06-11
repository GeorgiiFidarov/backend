package org.greenway.backend.controller;

import jakarta.validation.Valid;
import org.greenway.backend.model.Category;
import org.greenway.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;
    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping("/")
    public ResponseEntity<HashMap<String,Object>> getCategories(){
        List<Category> listOfCategories = categoryService.getAllCategories();
        HashMap<String,Object> body = new HashMap<>();
        body.put("categories",listOfCategories);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCategory(@PathVariable("id") Long id){
        if (Objects.nonNull(categoryService.getCategoryById(id))){
            Category body = categoryService.getCategoryById(id).orElse(null);
            return new ResponseEntity<>(body,HttpStatus.OK);
        }
        return new ResponseEntity<>("Nothing found with this id = "+id,HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCategory(@Valid @RequestBody Category category){
        if (Objects.nonNull(categoryService.getCategoryByName(category.getCategoryName()))){
            return new ResponseEntity<>(new ApiResponse(false,"category already exists"),HttpStatus.CONFLICT);
        }
        categoryService.createCategory(category);
        return new ResponseEntity<>(new ApiResponse(true, "created the category"), HttpStatus.CREATED);
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable("id") Long id,@Valid @RequestBody Category category){
        if (Objects.nonNull(categoryService.getCategoryById(id))){
            categoryService.updateCategory(id,category);
            return new ResponseEntity<>(new ApiResponse(true, "updated the category"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(false, "category does not exist"), HttpStatus.NOT_FOUND);
    }
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@Valid @RequestBody Category category){
        if (Objects.nonNull(categoryService.getCategoryById(category.getId()))){
            categoryService.deleteCategory(category);
            return new ResponseEntity<>(new ApiResponse(true,category.getCategoryName()+": Was deleted"),HttpStatus.OK);
        }
        return new ResponseEntity<>(
                new ApiResponse(false,"Category with name "+category.getCategoryName()+ " was not found"),HttpStatus.NOT_FOUND);
    }
    @PostMapping("/delete/all")
    public ResponseEntity<ApiResponse> deleteAllCategories(){
        categoryService.deleteAllCategories();
        return new ResponseEntity<>(new ApiResponse
                (true,"Everything cleared up"),HttpStatus.OK);

    }
}
