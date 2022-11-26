package kg.neobis.rentit.service;

import kg.neobis.rentit.dto.*;
import kg.neobis.rentit.entity.*;
import kg.neobis.rentit.entity.Calendar;
import kg.neobis.rentit.exception.AlreadyExistException;
import kg.neobis.rentit.exception.BadRequestException;
import kg.neobis.rentit.exception.ProductViolationException;
import kg.neobis.rentit.exception.ResourceNotFoundException;
import kg.neobis.rentit.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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

    private final ImageRepository imageRepository;

    private final ImageService imageService;

    private final LocationRepository locationRepository;

    private final CategoryFieldRepository categoryFieldRepository;

    private final ViewRepository viewRepository;

    private final CalendarRepository calendarRepository;

    private final BookingRepository bookingRepository;

    private final ComplaintRepository complaintRepository;


    public List<ProductPageDto> getProductBySearch(String text) {
        return productRepository.getProductBySearch(text.toLowerCase()).stream()
                .filter(Product::getActive)
                .map(this::mapToProductPageDto)
                .collect(Collectors.toList());
    }

    public List<ProductPageDto> getMainPageProducts(int callNumber) {
        return productRepository.getMainPageProducts(callNumber * 20)
                .stream()
                .filter(Product::getActive)
                .map(
                        this::mapToProductPageDto
                )
                .collect(Collectors.toList());
    }

    public List<ProductPageDto> getMainPageProductsByCategory(int callNumber, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Category was not found with ID: "
                                + categoryId)
                );

        return productRepository.getMainPageProductsByCategory(callNumber * 20, category.getId())
                .stream()
                .filter(Product::getActive)
                .map(
                        this::mapToProductPageDto
                )
                .collect(Collectors.toList());
    }

    public List<ProductPageDto> getUserProducts() {
        return getAuthentication().getProducts().stream()
                .map(
                        this::mapToProductPageDto
                )
                .collect(Collectors.toList());
    }

    public List<ProductPageDto> getRecommendations() {
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

        if (categories.isEmpty()) {
            return productRepository.getRandomProducts().stream()
                    .filter(Product::getActive)
                    .map(this::mapToProductPageDto)
                    .collect(Collectors.toList());
        }

        return productRepository.getRandomProductsByCategory(categories).stream()
                .filter(Product::getActive)
                .map(this::mapToProductPageDto)
                .collect(Collectors.toList());
    }

    public List<ProductPageDto> getFavorites() {
        return getAuthentication().getFavoriteProducts().stream()
                .map(this::mapToProductPageDto)
                .collect(Collectors.toList());
    }

    private ProductPageDto mapToProductPageDto(Product product) {
        ProductPageDto dto = new ProductPageDto();

        dto.setProductId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setPrice(product.getPrice());
        dto.setClickNumber(product.getClickedNum());

        if(getAuthentication() == null) {
            dto.setFavorite(false);
        } else {
            dto.setFavorite(getAuthentication().getFavoriteProducts().contains(product));
        }

        dto.setActive(product.getActive());

        ImageProduct imageProduct =
                imageProductRepository.findByProductIdAndOrderNumber(product.getId(), (byte) 1);

        if (imageProduct != null) {
            Image image = imageProduct.getImage();
            if(image != null && image.getUrl().startsWith("http")) {
                dto.setMainImageUrl(image.getUrl().replace("http", "https"));
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
        dto.setLikedNum(product.getFavoriteUsers().size());
        dto.setFavorite(getAuthentication().getFavoriteProducts().contains(product));
        dto.setMinimumBookingNumberDay(product.getMinimumBookingNumberDay());
        dto.setActive(product.getActive());
        dto.setBlocked(product.getUser().getBlocked());

        dto.setImages(imageProductRepository.findByProductIdOrderByOrderNumberAsc(productId).stream()
                .map(e -> {
                    ProductImageDto productImageDto = new ProductImageDto();

                    productImageDto.setImageId(e.getImage().getId());
                    productImageDto.setImageUrl(e.getImage().getUrl().replace("http", "https"));
                    productImageDto.setOrderNumber(e.getOrderNumber());

                    return productImageDto;
                })
                .collect(Collectors.toList())
        );

        List<Review> reviews = product.getReviews();

        double rating = (double) reviews.stream()
                .map(Review::getStar)
                .mapToInt(Integer::intValue)
                .sum() / reviews.size();

        dto.setRating(String.format("%.1f", rating));

        Set<FieldProduct> fieldProducts = product.getFieldProducts();

        TreeMap<String, String> characteristics = new TreeMap<>();

        for (FieldProduct entity : fieldProducts) {
            characteristics.put(entity.getField().getName(), entity.getValue());
        }

        dto.setCharacteristics(characteristics);

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

    public List<ProductReviewDto> getProductReviews(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product was not found with ID: " + productId)
                );

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

        return reviewsDto;
    }

    @Transactional
    public ProductRegistrationDto publishProductDetails(ProductRegistrationDto dto) {
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

        LocalDate bookFrom = product.getBookDateFrom();
        LocalDate bookTill = product.getBookDateTill();

        while (bookFrom.isBefore(bookTill.plusDays(1))) {
            Calendar calendar = new Calendar();

            calendar.setDate(bookFrom);
            calendar.setProduct(product);
            calendar.setBooked(false);
            calendar.setUser(null);

            calendarRepository.save(calendar);

            bookFrom = bookFrom.plusDays(1);
        }

        saveFieldProductValues(
                categoryFieldRepository.findByCategoryIdOrderByFieldName(category.getId()), product, dto.getFieldValue()
        );

        dto.setProductId(product.getId());

        return dto;
    }

    public String publishProductImage(MultipartFile file, Long productId, byte order) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product was not found with ID: " + productId)
                );

        ImageProduct imageProduct = imageProductRepository.findByProductIdAndOrderNumber(productId, order);

        if (imageProduct == null) {

            imageProduct = new ImageProduct();

            imageProduct.setProduct(product);
            imageProduct.setImage(imageService.saveImage(file));
            imageProduct.setOrderNumber(order);

            imageProductRepository.save(imageProduct);

            return "Images was saved successfully.";
        } else {
            throw new AlreadyExistException("There is already image product exists with the given order.");
        }
    }

    @Transactional
    public ProductRegistrationDto updateProductDetails(Long productId, ProductRegistrationDto dto) {
        User user = userRepository.findByEmail(UserService.getAuthentication().getName());

        if (user == null) {
            throw new ResourceNotFoundException("User is not authenticated");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product was not found with ID: "
                                + productId)
                );

        if (!getAuthentication().getProducts().contains(product)) {
            throw new ProductViolationException("Product violation.");
        }

        Calendar maxDateCalendar = calendarRepository.getMaxCalendar();

        List<Calendar> calendar = product.getCalendars();

        if(dto.getBookDateFrom().isBefore(LocalDate.now())) {
            throw new BadRequestException("Начальная дата должны быть не раньше сегодняшней.");
        } else {
            calendarRepository.cutProductCalendarBeforeDate(dto.getBookDateFrom());
        }

        if(dto.getBookDateTill().isAfter(maxDateCalendar.getDate())) {
            LocalDate startingDate = maxDateCalendar.getDate().plusDays(1);

            while (startingDate.isBefore(dto.getBookDateTill().plusDays(1))) {
                Calendar entity = new Calendar();

                entity.setProduct(product);
                entity.setDate(startingDate);
                entity.setBooked(false);

                calendar.add(entity);

                startingDate = startingDate.plusDays(1);
            }
        } else if(dto.getBookDateTill().isBefore(maxDateCalendar.getDate())) {
            calendarRepository.cutProductCalendarAfterDate(dto.getBookDateTill());
        }

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

    public String updateProductReplaceImage(MultipartFile file, Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Image was not found with ID: " + imageId)
                );

        Image updatedImage = imageService.replaceProductImage(file, image);

        return updatedImage.getUrl();
    }

    @Transactional
    public String deleteProductImage(Long imageId, Long productId, int orderNumber) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Image was not found with ID: " + imageId)
                );

        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product was not found with ID: "
                                + productId)
                );

        User owner = product.getUser();

        User authUser = getAuthentication();

        if(!owner.getId().equals(authUser.getId())) {
            throw new ProductViolationException("Product violation.");
        }

        imageProductRepository.deleteByImageIdAndProductId(image.getId(), product.getId());

        imageProductRepository.setOrderNumbersDown(orderNumber, productId);

        return "Image was successfully deleted.";
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

        if (!getAuthentication().getProducts().contains(product)) {
            throw new ProductViolationException("Product violation.");
        } else if (product.getActive()) {
            return "The product is already activated.";
        }

        product.setActive(true);

        productRepository.save(product);

        return "The product is activated.";
    }

    public String deactivateProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product was not found with ID: "
                                + productId)
                );

        if (!getAuthentication().getProducts().contains(product)) {
            throw new ProductViolationException("Product violation.");
        } else if (!product.getActive()) {
            return "The product is already deactivated.";
        }

        product.setActive(false);

        productRepository.save(product);

        return "The product is deactivated.";
    }

    public String addProductToFavorites(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product was not found with ID: "
                                + productId)
                );

        User user = userRepository.findByEmail(UserService.getAuthentication().getName());

        if (user.getId().equals(product.getUser().getId())) {
            throw new BadRequestException("You cannot add your product to favorites.");
        }

        List<Product> favorites = user.getFavoriteProducts();

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

        List<Product> favorites = user.getFavoriteProducts();

        favorites.remove(product);

        userRepository.save(user);

        return "The product was removed from favorites.";
    }

    public String deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product was not found with ID: " + productId)
                );

        Booking bookings = bookingRepository.getBookingsByProductIdAndDate(productId, LocalDate.now());

        if (bookings != null) {
            throw new BadRequestException("Вы не можете удалить продукт, так как есть активные бронирования, " +
                    "которые все еще действительны");
        }

        productRepository.delete(product);

        return "Product was deleted successfully";
    }

    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByEmail(authentication.getName());
    }

}
