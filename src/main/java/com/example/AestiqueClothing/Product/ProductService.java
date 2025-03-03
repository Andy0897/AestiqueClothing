package com.example.AestiqueClothing.Product;

import com.example.AestiqueClothing.Brand.BrandRepository;
import com.example.AestiqueClothing.Category.CategoryRepository;
import com.example.AestiqueClothing.Size.ProductSize;
import com.example.AestiqueClothing.Size.ProductSizeRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {
    private ProductRepository productRepository;
    private BrandRepository brandRepository;
    private CategoryRepository categoryRepository;
    private ProductSizeRepository productSizeRepository;

    public ProductService(ProductRepository productRepository, BrandRepository brandRepository, CategoryRepository categoryRepository, ProductSizeRepository productSizeRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
        this.productSizeRepository = productSizeRepository;
    }

    @Transactional
    public String submitProduct(List<String> sizes, List<Integer> quantities, MultipartFile[] images, Integer mainImageIndex, Product product, BindingResult bindingResult, Model model) {
        List<byte[]> imageList = new ArrayList<>();
        boolean nullImages = true;
        boolean invalidSizes = false;
        try {
            for (MultipartFile file : images) {
                if (!file.isEmpty()) {
                    imageList.add(file.getBytes());
                    nullImages = false;
                }
            }
            product.setImages(imageList);
            product.setMainImageIndex(mainImageIndex);;
        } catch (IOException e) {
            model.addAttribute("product", product);
            model.addAttribute("hasUploadError", true);
            return "product/add";
        }

        for (int i = 0; i < sizes.size(); i++) {
            ProductSize productSize = new ProductSize();
            productSize.setSize(sizes.get(i));
            productSize.setQuantity(quantities.get(i));
            productSize.setProduct(product);
            if(checkIfSizeValid(productSize)){
                product.addSize(productSize);
            }
            else {
                invalidSizes = true;
                break;
            }
        }
        if(bindingResult.hasFieldErrors("title") || bindingResult.hasFieldErrors("description") ||
                bindingResult.hasFieldErrors("brand") || bindingResult.hasFieldErrors("category") ||
                bindingResult.hasFieldErrors("material") || bindingResult.hasFieldErrors("color") ||
                bindingResult.hasFieldErrors("gender") || bindingResult.hasFieldErrors("price") ||
                invalidSizes || nullImages) {
            model.addAttribute("product", product);
            model.addAttribute("brands", brandRepository.findAll());
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("materials", Arrays.asList("Памук", "Полиестер", "Вълна", "Кожа", "Кашмир", "Лен", "Деним", "Коприна", "Еластан", "Акрил"));
            model.addAttribute("colors", Arrays.asList("Черен", "Бял", "Син", "Червен", "Зелен", "Жълт", "Кафяв", "Сив", "Оранжев", "Розов", "Лилав"));
            model.addAttribute("areImagesSelected", !nullImages);
            model.addAttribute("wrongSizeDetails", invalidSizes);
            model.addAttribute("areSizesEmpty", product.getSizes().isEmpty());
            return "product/add";
        }
        product.setPopularity(1);

        productRepository.save(product);
        return "redirect:/products";
    }

    public String submitIncreasePopularity(Long productId) {
        Product product = productRepository.findById(productId).get();
        product.setPopularity(product.getPopularity() + 1);
        productRepository.save(product);
        return "redirect:/products/" + productId;
    }

    private boolean checkIfSizeValid(ProductSize productSize) {
        if(productSize.getSize() == null || productSize.getQuantity() == null || productSize.getSize().isEmpty() || productSize.getQuantity() < 0) {
            return false;
        }
        return true;
    }

    public void updateSizeQuantity(Product product, Long sizeId, int newQuantity) {
        for (ProductSize size : product.getSizes()) {
            if (size.getId().equals(sizeId)) {
                size.setQuantity(size.getQuantity() + newQuantity);
                productSizeRepository.save(size);
                break;
            }
        }
    }

}
