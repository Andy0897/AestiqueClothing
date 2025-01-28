package com.example.AestiqueClothing.Product;

import com.example.AestiqueClothing.Category.CategoryRepository;
import com.example.AestiqueClothing.Size.ProductSize;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public String submitProduct(List<String> sizes, List<Integer> quantities, MultipartFile[] images, Integer mainImageIndex, Product product, BindingResult bindingResult, Model model) {
        List<byte[]> imageList = new ArrayList<>();
        boolean nullImages = true;

        try {
            for (MultipartFile file : images) {
                if (!file.isEmpty()) {
                    imageList.add(file.getBytes());
                    nullImages = false;
                }
            }
        } catch (IOException e) {
            model.addAttribute("product", product);
            model.addAttribute("hasUploadError", true);
            return "product/add";
        }

        for (int i = 0; i < sizes.size(); i++) {
            ProductSize size = new ProductSize();
            size.setSize(sizes.get(i));
            size.setQuantity(quantities.get(i));
            size.setProduct(product);
            product.addSize(size);
        }
        if(bindingResult.hasErrors() || checkIfSizesValid(product.getSizes()) || nullImages) {
            model.addAttribute("product", new Product());
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("areImagesSelected", !nullImages);
            model.addAttribute("wrongSizeDetails", checkIfSizesValid(product.getSizes()));
            model.addAttribute("areSizesEmpty", product.getSizes().isEmpty());
        }
    }

    private boolean checkIfSizesValid(List<ProductSize> productSizes) {
        boolean areValid = true;
        if(productSizes.stream().anyMatch(productSize -> productSize.getSize().isEmpty())) {
            areValid = false;
        }
        if(productSizes.stream().anyMatch(productSize -> productSize.getQuantity() < 0)) {
            areValid = false;
        }
        return false;
    }
}
