package com.example.AestiqueClothing.Product;

import com.example.AestiqueClothing.Category.CategoryRepository;
import com.example.AestiqueClothing.Size.ProductSize;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
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
    public String saveProduct(@RequestParam List<String> sizes, @RequestParam List<Integer> quantities, @ModelAttribute Product product, BindingResult bindingResult, Model model) {
        for (int i = 0; i < sizes.size(); i++) {
            ProductSize size = new ProductSize();
            size.setSize(sizes.get(i));
            size.setQuantity(quantities.get(i));
            size.setProduct(product);
            product.addSize(size);
        }
        productRepository.save(product);
        return "redirect:/products";
    }

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "product/show-all";
    }
}
