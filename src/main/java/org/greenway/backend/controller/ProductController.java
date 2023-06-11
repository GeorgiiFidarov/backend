package org.greenway.backend.controller;

import jakarta.validation.Valid;
import org.greenway.backend.dto.ProductDTO;
import org.greenway.backend.model.Category;
import org.greenway.backend.model.PageSettings;
import org.greenway.backend.service.CategoryService;
import org.greenway.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }



    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody @Valid ProductDTO productDTO){
        Optional<Category> optionalCategory = categoryService.getCategoryById(productDTO.getCategoryId());
        if (optionalCategory.isEmpty()){
            return new ResponseEntity<>(
                    new ApiResponse(false,"Category does not exist"), HttpStatus.BAD_REQUEST);
        }
        productService.createProduct(productDTO,optionalCategory.get());
        return new ResponseEntity<>(
                new ApiResponse(true,"Product added"), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductDTO>> getProducts(){
        List<ProductDTO> products = productService.getAllProducts();
        return new ResponseEntity<>(products,HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> getProduct(@PathVariable("id")Long id){
        if (Objects.nonNull(productService.getProductById(id))){
            ProductDTO body = productService.getProductById(id).orElse(null);
            return new ResponseEntity<>(body,HttpStatus.OK);
        }
        return new ResponseEntity<>("Nothing found with this id = "+id,HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable("id") Long id, @RequestBody @Valid ProductDTO productDTO) throws Exception {
        Optional<Category> optionalCategory = categoryService.getCategoryById(productDTO.getCategoryId());
        if (optionalCategory.isEmpty()){
            return new ResponseEntity<>
                    (new ApiResponse(false,"Category does not exist"), HttpStatus.BAD_REQUEST);
        }
        productService.updateProduct(productDTO,id);
        return new ResponseEntity<>
                (new ApiResponse(true,"Product added"), HttpStatus.CREATED);
    }

    @GetMapping("/category/{id}/products")
    public ResponseEntity<Map<String,Page<ProductDTO>>> getAllProductsByCategoryId(
            @PathVariable("id") Long id,
            PageSettings pageSettings,
            @RequestParam(defaultValue = "") String searchKey) {

        Map<String,Page<ProductDTO>> categoryProducts = new HashMap<>();
        Optional<Category> optionalCategory = categoryService.getCategoryById(id);
        if (optionalCategory.isEmpty()){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        Page<ProductDTO> products = productService.getAllProductsByCategoryId(id,pageSettings,searchKey);
        System.out.println(products);
        categoryProducts.put("products",products);
        return new ResponseEntity<>
                (categoryProducts,HttpStatus.OK);
    }
    @GetMapping("/{id}/products")
    public int getAmountOfProductsInCategory(@PathVariable("id") Long id){
        return productService.getAmountOfProductsInCategory(id);
    }
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@RequestBody @Valid ProductDTO productDTO) {
        if (Objects.nonNull(productService.getProductById(productDTO.getId()))){
            productService.deleteProduct(productDTO);
            return new ResponseEntity<>(
                    new ApiResponse(true,"Product deleted"),HttpStatus.OK);
        }
        return new ResponseEntity<>(
                new ApiResponse(false,"There is nothing to delete"),HttpStatus.NOT_FOUND);
    }
}
