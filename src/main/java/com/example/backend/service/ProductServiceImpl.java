package com.example.backend.service;

import com.example.backend.exception.ProductException;
import com.example.backend.model.Category;
import com.example.backend.model.Product;
import com.example.backend.repository.CategoryRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.request.CreateProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    private ProductRepository productRepo;
    private UserService userService;
    private CategoryRepository categoryRepo;

    public ProductServiceImpl(ProductRepository productRepo,UserService userService, CategoryRepository categoryRepo) {
        this.productRepo = productRepo;
        this.userService = userService;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public Product createProduct(CreateProductRequest req) {
        Category topLevel = categoryRepo.findByName(req.getTopLavelCategory());
        if(topLevel==null){
            Category topLavelCategory = new Category();
            topLavelCategory.setName(req.getTopLavelCategory());
            topLavelCategory.setLevel(1);

            topLevel=categoryRepo.save(topLavelCategory);
        }
        Category secondLevel = categoryRepo.findByNameAndParant(req.getSecondLavelCategory(),topLevel.getName());
        if(secondLevel==null){
            Category secondLavelCategory = new Category();
            secondLavelCategory.setName(req.getSecondLavelCategory());
            secondLavelCategory.setParentCategory(topLevel);
            secondLavelCategory.setLevel(2);

            secondLevel=categoryRepo.save(secondLavelCategory);
        }
        Category thirdLevel = categoryRepo.findByNameAndParant(req.getThirdLavelCategory(),secondLevel.getName());
        if(thirdLevel==null){
            Category thirdLavelCategory = new Category();
            thirdLavelCategory.setName(req.getThirdLavelCategory());
            thirdLavelCategory.setParentCategory(secondLevel);
            thirdLavelCategory.setLevel(3);

            thirdLevel=categoryRepo.save(thirdLavelCategory);
        }

        Product product = new Product();
        product.setTitle(req.getTitle());
        product.setColor(req.getColor());
        product.setDescription(req.getDescription());
        product.setDiscountedPrice(req.getDiscountedPrice());
        product.setDiscountPresent(req.getDiscountPresent());
        product.setImageUrl(req.getImageUrl());
        product.setBrand(req.getBrand());
        product.setPrice(req.getPrice());
        product.setSizes(req.getSize());
        product.setQuantity(req.getQuantity());
        product.setCategory(thirdLevel);
        product.setCreatedAt(LocalDateTime.now());
        Product saveProduct = productRepo.save(product);
        System.out.println("products =" + product);
        return saveProduct;
    }

    @Override
    public void deleteProduct(Long productId) throws ProductException {
        Product product = findProductById(productId);
        product.getSizes().clear();
        productRepo.delete(product);
    }


    @Override
    public Product updateProduct(Long productId, Product product) throws ProductException {
        Product p = findProductById(productId);
        if(product.getQuantity()!=0){
            p.setQuantity(product.getQuantity());
        }

        return productRepo.save(p);
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {
        Optional<Product> opt = productRepo.findById(productId);
        if(opt.isPresent()){
            return opt.get();
        }
        throw new ProductException("Product not found with id " + productId);
    }

    @Override
    public List<Product> findProductByCartegory(String category) {
        return null;
    }

    @Override
    public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, double minPrice, double maxPrice, double minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {

        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        List<Product> products = productRepo.filterProducts(category,minPrice,maxPrice,minDiscount,sort);
        if(!colors.isEmpty()){
            products=products.stream().filter(p -> colors.stream().anyMatch(c->c.equalsIgnoreCase(p.getColor())))
                    .collect(Collectors.toList());
        }
        if(stock!=null){
            if(stock.equals("in_stock")){
                products=products.stream().filter(p->p.getQuantity()>0).collect(Collectors.toList());
            }
            else if(stock.equals("out_of_stock")){
                products=products.stream().filter(p->p.getQuantity()<1).collect(Collectors.toList());
            }
        }

        int startIndex=(int) pageable.getOffset();
        int endIndex=Math.min(startIndex+pageable.getPageSize(), products.size());
        List<Product> pageCOntent = products.subList(startIndex,endIndex);
        Page<Product> filteredProducts = new PageImpl<>(pageCOntent,pageable,products.size());

        return filteredProducts;
    }

    @Override
    public List<Product> findAllProduct() {
        return productRepo.findAll();
    }
}
