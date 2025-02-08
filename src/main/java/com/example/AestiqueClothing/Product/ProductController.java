package com.example.AestiqueClothing.Product;

import com.example.AestiqueClothing.Brand.*;
import com.example.AestiqueClothing.Category.*;
import com.example.AestiqueClothing.ImageEncoder;
import jakarta.validation.Valid;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductController {
    private ProductRepository productRepository;
    private ProductService productService;
    private BrandRepository brandRepository;
    private CategoryRepository categoryRepository;

    public ProductController(ProductRepository productRepository, ProductService productService, BrandRepository brandRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productService = productService;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("brands", ((List<Brand>) brandRepository.findAll()).stream().sorted(Comparator.comparing(brand -> brand.getName())));
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("materials", Arrays.asList("Памук", "Полиестер", "Вълна", "Кожа", "Кашмир", "Лен", "Деним", "Коприна", "Еластан", "Акрил"));
        model.addAttribute("colors", Arrays.asList("Черен", "Бял", "Син", "Червен", "Зелен", "Жълт", "Кафяв", "Сив", "Оранжев", "Розов", "Лилав"));
        model.addAttribute("areImagesSelected", true);
        model.addAttribute("wrongSizeDetails", false);
        model.addAttribute("areSizesEmpty", false);
        return "product/add";
    }

    @PostMapping("/submit")
    public String saveProduct(@RequestParam(required = false) List<String> sizes, @RequestParam(required = false) List<Integer> quantities, @RequestParam("images") MultipartFile[] images, @RequestParam(value = "mainImageIndex", required = false) Integer mainImageIndex, @ModelAttribute @Valid Product product, BindingResult bindingResult, Model model) {
        if (sizes == null) {
            sizes = new ArrayList<>();
        }

        if (quantities == null) {
            quantities = new ArrayList<>();
        }

        if (mainImageIndex == null || mainImageIndex < 0 || mainImageIndex >= images.length) {
            mainImageIndex = 0;
        }
        return productService.submitProduct(sizes, quantities, images, mainImageIndex, product, bindingResult, model);
    }

    @GetMapping
    public String getShowAllProducts(@RequestParam(value = "brand", required = false) Long brandId,
                                     @RequestParam(value = "category", required = false) Long categoryId,
                                     @RequestParam(value = "material", required = false) String material,
                                     @RequestParam(value = "color", required = false) String color,
                                     @RequestParam(value = "minPrice", required = false) Integer minPrice,
                                     @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
                                     @RequestParam(value = "gender", required = false) String gender, Model model) {
        List<Product> products = ((List<Product>) productRepository.findAll()).stream()
                .filter(product -> (brandId == null || product.getBrand().getId().equals(brandId)))
                .filter(product -> (categoryId == null || product.getCategory().getId().equals(categoryId)))
                .filter(product -> ((material == null || material.isEmpty()) || product.getMaterial().equalsIgnoreCase(material)))
                .filter(product -> ((color == null || color.isEmpty()) || product.getColor().equalsIgnoreCase(color)))
                .filter(product -> (minPrice == null || product.getPrice() >= minPrice))
                .filter(product -> (maxPrice == null || product.getPrice() <= maxPrice))
                .filter(product -> ((gender == null || gender.isEmpty()) || product.getGender().equalsIgnoreCase(gender)))
                .collect(Collectors.toList());
        model.addAttribute("products", products);
        model.addAttribute("encoder", new ImageEncoder());
        model.addAttribute("brands", ((List<Brand>) brandRepository.findAll()).stream().sorted(Comparator.comparing(brand -> brand.getName())));
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("materials", Arrays.asList("Памук", "Полиестер", "Вълна", "Кожа", "Кашмир", "Лен", "Деним", "Коприна", "Еластан", "Акрил"));
        model.addAttribute("colors", Arrays.asList("Черен", "Бял", "Син", "Червен", "Зелен", "Жълт", "Кафяв", "Сив", "Оранжев", "Розов", "Лилав"));
        return "product/show-all";
    }

    @GetMapping("/bestsellers")
    public String getShowPopularProducts(@RequestParam(value = "brand", required = false) Long brandId,
                                     @RequestParam(value = "category", required = false) Long categoryId,
                                     @RequestParam(value = "material", required = false) String material,
                                     @RequestParam(value = "color", required = false) String color,
                                     @RequestParam(value = "minPrice", required = false) Integer minPrice,
                                     @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
                                     @RequestParam(value = "gender", required = false) String gender, Model model) {
        List<Product> products = (List<Product>) productRepository.findAll();
        products.sort(Comparator.comparing(Product::getPopularity));
        Collections.reverse(products);
        if (products.size() > 20) {
            products = products.subList(0, 19);
        }
        products = products.stream()
                .filter(product -> (brandId == null || product.getBrand().getId().equals(brandId)))
                .filter(product -> (categoryId == null || product.getCategory().getId().equals(categoryId)))
                .filter(product -> ((material == null || material.isEmpty()) || product.getMaterial().equalsIgnoreCase(material)))
                .filter(product -> ((color == null || color.isEmpty()) || product.getColor().equalsIgnoreCase(color)))
                .filter(product -> (minPrice == null || product.getPrice() >= minPrice))
                .filter(product -> (maxPrice == null || product.getPrice() <= maxPrice))
                .filter(product -> ((gender == null || gender.isEmpty()) || product.getGender().equalsIgnoreCase(gender)))
                .collect(Collectors.toList());

        model.addAttribute("products", products);
        model.addAttribute("encoder", new ImageEncoder());
        model.addAttribute("brands", ((List<Brand>) brandRepository.findAll()).stream().sorted(Comparator.comparing(brand -> brand.getName())));
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("materials", Arrays.asList("Памук", "Полиестер", "Вълна", "Кожа", "Кашмир", "Лен", "Деним", "Коприна", "Еластан", "Акрил"));
        model.addAttribute("colors", Arrays.asList("Черен", "Бял", "Син", "Червен", "Зелен", "Жълт", "Кафяв", "Сив", "Оранжев", "Розов", "Лилав"));
        return "product/show-bestsellers";
    }

    @GetMapping("/{productId}")
    public String getShowSingleProduct(@PathVariable("productId") Long productId, Model model) {
        Product product = productRepository.findById(productId).get();
        model.addAttribute("product", product);
        model.addAttribute("encoder", new ImageEncoder());
        return "product/show-single";
    }

    @PostMapping("/submit-increase-popularity/{productId}")
    public String getSubmitIncreasePopularity(@PathVariable("productId") Long productId) {
        return productService.submitIncreasePopularity(productId);
    }
}