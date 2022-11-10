package kg.neobis.rentit.service;

import kg.neobis.rentit.dto.*;
import kg.neobis.rentit.entity.*;
import kg.neobis.rentit.exception.BadRequestException;
import kg.neobis.rentit.exception.ResourceNotFoundException;
import kg.neobis.rentit.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final FieldProductRepository fieldProductRepository;

    private final ImageProductRepository imageProductRepository;

    private final ImageService imageService;

    private final ImageRepository imageRepository;

    private final LocationRepository locationRepository;

    private final CategoryFieldRepository categoryFieldRepository;

    private final ViewRepository viewRepository;


    public List<ProductMainPageDto> getProductBySearch(String text) {
        return productRepository.getProductBySearch(text).stream()
                .map(this::mapToProductMainPageDto)
                .collect(Collectors.toList());
    }

    public List<ProductMainPageDto> getMainPageProducts(int callNumber) {
        return productRepository.getMainPageProducts(callNumber * 20)
                .stream()
                .map(
                        this::mapToProductMainPageDto
                )
                .collect(Collectors.toList());
    }

    public List<ProductMainPageDto> getMainPageProductsByCategory(int callNumber, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Category was not found with ID: "
                                + categoryId)
                );

        return productRepository.getMainPageProductsByCategory(callNumber * 20, category.getId())
                .stream()
                .map(
                        this::mapToProductMainPageDto
                )
                .collect(Collectors.toList());
    }

    private ProductMainPageDto mapToProductMainPageDto(Product product) {
        ProductMainPageDto dto = new ProductMainPageDto();

        dto.setProductId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setPrice(product.getPrice());
        dto.setFavorite(getAuthentication().getFavorites().contains(product));

        List<ImageProduct> imageProductList = product.getImageProduct();

        for (ImageProduct imageProduct : imageProductList) {

            if (imageProduct.getOrderNumber() == 0) {
                dto.setMainImageUrl(imageProduct.getImage().getUrl());
                break;
            }
        }

        return dto;
    }

    public ProductDetailsDto getProductDetails(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product was not found with ID: " + productId)
                );

        ProductDetailsDto dto = new ProductDetailsDto();

        dto.setProductId(productId);
        dto.setTitle(product.getTitle());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setLocation(product.getLocation());
        dto.setClickNumber(product.getClickedNum());
        dto.setFavorite(getAuthentication().getFavorites().contains(product));
        dto.setMinimumBookingNumberDay(product.getMinimumBookingNumberDay());

        dto.setImages(imageProductRepository.findByProductIdOrderByOrderNumberAsc(productId).stream()
                .map(e -> e.getImage().getUrl())
                .collect(Collectors.toList())
        );

        Set<FieldProduct> fieldProducts = product.getFieldProducts();

        TreeMap<String, String> characteristics = new TreeMap<>();

        for (FieldProduct entity : fieldProducts) {
            characteristics.put(entity.getField().getName(), entity.getValue());
        }

        dto.setCharacteristics(characteristics);

        List<ProductReviewDto> reviewsDto = new ArrayList<>();

        product.getReviews().forEach(
                review -> {
                    ProductReviewDto reviewDto = new ProductReviewDto();

                    reviewDto.setName(review.getUser().getLastName() + " " + review.getUser().getFirstName());
                    reviewDto.setStar(review.getStar());
                    reviewDto.setText(review.getText());
                    reviewDto.setDate(review.getDateTime().toLocalDate());

                    reviewsDto.add(reviewDto);
                }
        );

        dto.setReviews(reviewsDto);

        product.setClickedNum(product.getClickedNum() + 1);

        productRepository.save(product);

        View view = viewRepository.findByUserIdAndCategoryId(
                getAuthentication().getId(),
                product.getCategory().getId()
        );

        if (view == null) {
            view = new View();

            view.setUser(getAuthentication());
            view.setCategory(product.getCategory());
            view.setViewNum(1);
        } else {
            view.setViewNum(view.getViewNum() + 1);
        }

        viewRepository.save(view);

        return dto;
    }

    public List<ProductMainPageDto> getRecommendations() {
        List<View> views = viewRepository.findAllByUserId(getAuthentication().getId());

        List<Long> categories = new ArrayList<>();

        int max = 0;

        for (View view : views) {
            if (view.getViewNum() > max) {
                if (categories.size() > 1) {
                    categories.clear();
                }
                categories.add(view.getCategory().getId());
                max = view.getViewNum();
            } else if (view.getViewNum() == max) {
                categories.add(view.getCategory().getId());
            }
        }

        if(categories.isEmpty()) {
            return productRepository.getRandomProducts().stream()
                    .map(this::mapToProductMainPageDto)
                    .collect(Collectors.toList());
        }

        return productRepository.getRandomProductsByCategory(categories).stream()
                .map(this::mapToProductMainPageDto)
                .collect(Collectors.toList());
    }

    public List<ProductMainPageDto> getFavorites() {
        return getAuthentication().getFavorites().stream()
                .map(this::mapToProductMainPageDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductRegistrationDto publishProduct(ProductRegistrationDto dto, MultipartFile[] multipartFiles) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Category was not found with ID: "
                                + dto.getCategoryId())
                );

        Location location = new Location();

        location.setX(dto.getLocationX());
        location.setY(dto.getLocationY());

        locationRepository.save(location);

        User user = userRepository.findByEmail(UserService.getAuthentication().getName());

        if (user == null) {
            throw new ResourceNotFoundException("User is not authenticated");
        }

        Product product = Product.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .currency("сом")
                .minimumBookingNumberDay(dto.getMinimumBookingNumberDay())
                .bookDateFrom(dto.getBookDateFrom())
                .bookDateTill(dto.getBookDateTill())
                .creationTime(LocalDateTime.now())
                .location(location)
                .user(user)
                .category(category)
                .active(true)
                .build();

        productRepository.save(product);

        saveFieldProductValues(
                categoryFieldRepository.findByCategoryIdOrderByFieldName(category.getId()), product, dto.getFieldValue()
        );

        for (int i = 0; i < 5; i++) {
            ImageProduct imageProduct = new ImageProduct();

            imageProduct.setProduct(product);
            if (multipartFiles[i].isEmpty()) {
                Image image = new Image();

                image.setUrl("");
                image.setPublicId("");

                imageProduct.setImage(imageRepository.save(image));
            } else {
                imageProduct.setImage(imageService.saveImage(multipartFiles[i]));
            }
            imageProduct.setOrderNumber((byte) (i));

            imageProductRepository.save(imageProduct);
        }

        dto.setProductId(product.getId());

        return dto;
    }

    @Transactional
    public ProductRegistrationDto updateProduct(Long productId,
                                                ProductRegistrationDto dto, MultipartFile[] multipartFiles) {
        User user = userRepository.findByEmail(UserService.getAuthentication().getName());

        if (user == null) {
            throw new ResourceNotFoundException("User is not authenticated");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product was not found with ID: "
                                + productId)
                );

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Category was not found with ID: "
                                + dto.getCategoryId())
                );

        product.setCategory(category);

        Location location = product.getLocation();

        location.setX(dto.getLocationX());
        location.setY(dto.getLocationY());

        product.setTitle(dto.getTitle());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setCurrency("сом");
        product.setMinimumBookingNumberDay(dto.getMinimumBookingNumberDay());
        product.setBookDateFrom(dto.getBookDateFrom());
        product.setBookDateTill(dto.getBookDateTill());
        product.setLocation(location);
        product.setUser(user);
        product.setCategory(category);
        product.setActive(true);

        List<ImageProduct> imageProducts = imageProductRepository.findByProductIdOrderByOrderNumberAsc(productId);

        for (int i = 0; i < imageProducts.size(); i++) {
            if(multipartFiles.length > i) {
                if (multipartFiles[i].isEmpty()) {
                    imageProducts.get(i).getImage().setUrl("");
                    imageProducts.get(i).getImage().setPublicId("");
                } else {
                    imageService.replaceImage(multipartFiles[i], imageProducts.get(i).getImage());
                }
            }
        }

        HashMap<String, String> fieldProductsMap = dto.getFieldValue();

        Set<FieldProduct> fieldProducts = product.getFieldProducts();

        if (product.getCategory().getId().equals(dto.getCategoryId())) {

            for (FieldProduct entity : fieldProducts) {
                if (fieldProductsMap.containsKey(entity.getField().getName())) {
                    entity.setValue(fieldProductsMap.get(entity.getField().getName()));
                }
            }
        } else {

            fieldProductRepository.deleteAll(fieldProducts);

            product.setCategory(category);

            saveFieldProductValues(
                    categoryFieldRepository.findByCategoryIdOrderByFieldName(category.getId()), product, dto.getFieldValue()
            );
        }

        productRepository.save(product);

        dto.setProductId(product.getId());

        return dto;
    }

    @Transactional
    public void saveFieldProductValues(List<CategoryField> categoryFields, Product product, HashMap<String, String> fieldProducts) {
        for (CategoryField entity : categoryFields) {
            FieldProduct fieldProduct = new FieldProduct();

            fieldProduct.setId(entity.getField(), product);
            fieldProduct.setProduct(product);
            fieldProduct.setField(entity.getField());
            fieldProduct.setValue(!fieldProducts.containsKey(entity.getField().getName()) ? "" :
                    fieldProducts.get(entity.getField().getName()).isEmpty() ? ""
                    : fieldProducts.get(entity.getField().getName()));

            fieldProductRepository.save(fieldProduct);
        }
    }

    public String activateProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product was not found with ID: "
                                + productId)
                );

        product.setActive(true);

        return "The product activated.";
    }

    public String deactivateProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product was not found with ID: "
                                + productId)
                );

        product.setActive(false);

        return "The product deactivated.";
    }

    public String addProductToFavorites(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product was not found with ID: "
                                + productId)
                );

        User user = userRepository.findByEmail(UserService.getAuthentication().getName());

        if(user.getId().equals(product.getUser().getId())) {
            throw new BadRequestException("You cannot add your product to favorites.");
        }

        List<Product> favorites = user.getFavorites();

        favorites.add(product);

        userRepository.save(user);

        return "The product was added to favorites.";
    }

    public String deleteProductFromFavorites(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product was not found with ID: "
                                + productId)
                );

        User user = userRepository.findByEmail(UserService.getAuthentication().getName());

        List<Product> favorites = user.getFavorites();

        favorites.remove(product);

        userRepository.save(user);

        return "The product was removed from favorites.";
    }

    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByEmail(authentication.getName());
    }

}
