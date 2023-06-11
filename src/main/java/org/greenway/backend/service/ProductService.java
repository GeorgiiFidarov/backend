package org.greenway.backend.service;

import org.greenway.backend.dto.ProductDTO;
import org.greenway.backend.model.Category;
import org.greenway.backend.model.PageSettings;
import org.greenway.backend.model.Product;
import org.greenway.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public void createProduct(ProductDTO productDTO, Category category){
        Product product = new Product();
        product.setDescription(productDTO.getDescription());
        product.setImage(productDTO.getImage());
        product.setName(productDTO.getName());
        product.setCategory(category);
        product.setPrice(productDTO.getPrice());

        productRepository.save(product);
    }

    public Optional<ProductDTO> getProductById(Long id){
        Optional<Product> foundProduct = productRepository.findById(id);
        ProductDTO foundProductDTO = getProductDTO(foundProduct.orElseThrow());
        return Optional.ofNullable(foundProductDTO);
    }

    public void deleteProduct(ProductDTO productDTO){
        productRepository.delete(getProduct(productDTO));
    }

    public Page<ProductDTO> getAllProductsByCategoryId(Long id,PageSettings pageSettings,String searchKey){
        Sort productSort = pageSettings.buildSort();
        Pageable productPage = PageRequest.of(
                pageSettings.getPage(),
                pageSettings.getElementPerPage(),
                productSort);
        if (searchKey.equals("")){
            return new PageImpl<>(productRepository.findAllProductsByCategoryId(id,productPage).stream().map(
                    this::getProductDTO).collect(Collectors.toList()));
        }else {
            return new PageImpl<>(productRepository.
                    findAllProductsByCategoryIdAndNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                    id,searchKey,searchKey,productPage).stream().map(
                    this::getProductDTO).collect(Collectors.toList()));
        }

    }
    public int getAmountOfProductsInCategory(Long id){
        return productRepository.findAllProductsByCategoryId(id).size();
    }
    public List<ProductDTO> getAllProducts(){
        List<Product> allProducts = productRepository.findAll();

        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product:allProducts){
            productDTOS.add(getProductDTO(product));
        }
        return productDTOS;
    }

    //изменение продукта без КАТЕГОРИИ
    public void updateProduct(ProductDTO productDTO, Long id) throws Exception {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()){
            throw new Exception("product not present");
        }
        Product product = optionalProduct.get();

        product.setDescription(productDTO.getDescription());
        product.setImage(productDTO.getImage());
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());

        productRepository.save(product);
    }

    //Делает из ПРОДУКТА ---> ДТО
    public ProductDTO getProductDTO(Product product){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setDescription(product.getDescription());
        productDTO.setImage(product.getImage());
        productDTO.setName(product.getName());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setPrice(product.getPrice());
        productDTO.setId(product.getId());

        return productDTO;
    }

    //Делает из ДТО ---> ПРОДУКТ
    public Product getProduct(ProductDTO productDTO){
        Product product = new Product();
        product.setDescription(productDTO.getDescription());
        product.setImage(productDTO.getImage());
        product.setName(productDTO.getName());
        product.setCategory(productRepository.findById(productDTO.getId()).orElseThrow().getCategory());
        product.setPrice(productDTO.getPrice());
        product.setId(productDTO.getId());

        return product;
    }

}
