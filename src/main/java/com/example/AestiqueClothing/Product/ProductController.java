package com.example.AestiqueClothing.Product;

import com.example.AestiqueClothing.Category.CategoryRepository;
import com.example.AestiqueClothing.Size.ProductSize;
import jakarta.validation.Valid;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    private ProductRepository productRepository;
    private ProductService productService;
    private CategoryRepository categoryRepository;

    public ProductController(ProductRepository productRepository, ProductService productService, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productService = productService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("areImagesSelected", true);
        model.addAttribute("wrongSizeDetails", false);
        model.addAttribute("areSizesEmpty", false);
        return "product/add";
    }

    @PostMapping("/submit")
    public String saveProduct(@RequestParam List<String> sizes, @RequestParam List<Integer> quantities, @RequestParam("images") MultipartFile[] images, @RequestParam(value = "mainImageIndex", required = false) Integer mainImageIndex, @ModelAttribute @Valid Product product, BindingResult bindingResult, Model model) {
        if (mainImageIndex == null || mainImageIndex < 0 || mainImageIndex >= images.length) {
            mainImageIndex = 0;
        }
        return productService.submitProduct(sizes, quantities, images, mainImageIndex, product, bindingResult, model);
    }

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "product/show-all";
    }
}
