package kg.neobis.rentit.service;

import kg.neobis.rentit.dto.*;
import kg.neobis.rentit.entity.*;
import kg.neobis.rentit.enums.AuthProvider;
import kg.neobis.rentit.enums.Status;
import kg.neobis.rentit.exception.AlreadyExistException;
import kg.neobis.rentit.exception.BadRequestException;
import kg.neobis.rentit.exception.ResetPasswordCodeExpirationException;
import kg.neobis.rentit.exception.ResourceNotFoundException;
import kg.neobis.rentit.mapper.*;
import kg.neobis.rentit.repository.*;
import kg.neobis.rentit.security.jwt.JwtProvider;
import kg.neobis.rentit.security.response.JwtResponse;
import kg.neobis.rentit.utils.EmailUtility;
import kg.neobis.rentit.validator.PhoneNumberValidator;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final Long resetPasswordCodeExpirationMs;

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final ImageService imageService;

    private final ImageUserService imageUserService;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final JavaMailSender javaMailSender;

    private final ComplaintRepository complaintRepository;

    private final ReviewRepository reviewRepository;

    private final ProductRepository productRepository;

    private final ProductService productService;

    private final ImageRepository imageRepository;


    public UserService(@Value("${resetPasswordCodeExpirationMs}")Long resetPasswordCodeExpirationMs, UserRepository userRepository,
                       RoleService roleService, ImageService imageService, ImageUserService imageUserService,
                       BCryptPasswordEncoder passwordEncoder, JwtProvider jwtProvider,
                       JavaMailSender javaMailSender, ComplaintRepository complaintRepository,
                       ReviewRepository reviewRepository, ProductRepository productRepository, ProductService productService, ImageRepository imageRepository) {
        this.resetPasswordCodeExpirationMs = resetPasswordCodeExpirationMs;
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.imageService = imageService;
        this.imageUserService = imageUserService;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.javaMailSender = javaMailSender;
        this.complaintRepository = complaintRepository;
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.productService = productService;
        this.imageRepository = imageRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("???? ?????????????? ?????????? ???????????????????????? ?? ?????????????????????? ????????????: " + email));

        return User.getUserDetails(user);
    }

    public List<UserDto> getAllUsers() {
        Iterable<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<UserDto>();

        for (User user : users){
            userDtos.add(UserMapper.userToUserDto(user));
        }

        return userDtos;
    }

    public List<UserDto> getAllNotVerifiedUsers() {
        Iterable<User> users = userRepository.findAllNotVerifiedUsers();
        List<UserDto> userDtos = new ArrayList<UserDto>();

        for (User user : users){
            userDtos.add(UserMapper.userToUserDto(user));
        }

        return userDtos;
    }

    public User getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("???????????????????????? ???? ????????????!"));

        return user;
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("???????????????????????? ???? ????????????!"));

        return UserMapper.userToUserDto(user);
    }

    public UserDto getUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("???????????????????????? ???? ????????????!"));

        return UserMapper.userToUserDto(user);
    }

    public User create(User user, Map<String, Object> attributes) {
        Role role = RoleMapper.roleDtoToRole(roleService.getRoleByName("USER"));

        User userPrincipal = User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(role)
                .blocked(false)
                .build();

        userPrincipal.setAttributes(attributes);

        return userPrincipal;
    }

    @Transactional
    public UserDto registerUserIncomplete(UserIncompleteRegisterDto userIncompleteRegisterDTO) {
        if (userRepository.existsByEmail(userIncompleteRegisterDTO.getEmail())) {
            throw new AlreadyExistException("???????????????????????? ?? ?????????? email: " + userIncompleteRegisterDTO.getEmail() + " ?????? ????????????????????");
        }

        Role role = RoleMapper.roleDtoToRole(roleService.getRoleByName("USER"));

        User user = User.builder()
                .firstName(userIncompleteRegisterDTO.getFirstName())
                .email(userIncompleteRegisterDTO.getEmail())
                .passportData(new PassportData(" ", null, " "))
                .registeredAddress(new RegisteredAddress(" ", " ", " ", " ", 0, 0))
                .residenceAddress(new ResidenceAddress(" ", " ", " ", " ", 0, 0))
                .role(role)
                .blocked(false)
                .password(passwordEncoder.encode(userIncompleteRegisterDTO.getPassword()))
                .isRegistrationComplete(false)
                .isVerifiedByTechSupport(false)
                .status(Status.ACTIVE)
                .provider(AuthProvider.local)
                .build();

        List<ImageUser> imageUsers = new ArrayList<>();

        for(int i = 1; i <= 4; i++) {
            Image image = new Image(" ", " ");
            ImageUser imageUser = new ImageUser();

            imageUser.setOrderNumber((byte) i);
            imageUser.setImage(imageService.saveImage(image));

            imageUsers.add(imageUserService.saveImageUser(imageUser));
        }

        user.setImageUser(imageUsers);

        return UserMapper.userToUserDto(userRepository.save(user));
    }

    @Transactional
    public UserDto registerUserCompleteParameter(UserCompleteRegisterDto userCompleteRegisterDto) {
        User user = userRepository.findById(userCompleteRegisterDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("???????????????????????? ???? ????????????!"));

        try {
            long tin = Long.parseLong(userCompleteRegisterDto.getPassportData().getTin());
        } catch (Exception ex) {
            throw new IllegalArgumentException("???????????????? ??????!");
        }

        if(userRepository.existsByPassportDataTin(userCompleteRegisterDto.getPassportData().getTin())) {
            throw new AlreadyExistException("???????????????????????? ?? ?????????? ??????: " + userCompleteRegisterDto.getPassportData().getTin() + " ?????? ????????????????????");
        }

        if(!PhoneNumberValidator.isPhoneNumberValid(userCompleteRegisterDto.getPhoneNumber())) {
            throw new IllegalArgumentException("?????????? ???????????????? - " + userCompleteRegisterDto.getPhoneNumber() + " ????????????????");
        }

        user.setFirstName(userCompleteRegisterDto.getFirstName());
        user.setLastName(userCompleteRegisterDto.getLastName());
        user.setMiddleName(userCompleteRegisterDto.getMiddleName());
        user.setPhoneNumber(userCompleteRegisterDto.getPhoneNumber());
        user.setDateOfBirth(userCompleteRegisterDto.getDateOfBirth());
        user.setBlocked(false);
        user.setPassportData(PassportDataMapper
                .passportDataDtoToPassportData(userCompleteRegisterDto.getPassportData()));
        user.setRegisteredAddress(RegisteredAddressMapper
                .registeredAddressDtoToRegisteredAddress(userCompleteRegisterDto.getRegisteredAddress()));
        user.setResidenceAddress(ResidenceAddressMapper
                .residenceAddressDtoToResidenceAddress(userCompleteRegisterDto.getResidenceAddress()));

        return UserMapper.userToUserDto(userRepository.save(user));
    }

    @Transactional
    public UserDto registerUserCompleteFiles(Long id, MultipartFile[] multipartFiles) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("???????????????????????? ???? ????????????!"));

        if(multipartFiles.length != 3) {
            throw new IllegalArgumentException("???????????? ???????? 3 ???????????????????? ????????????????");
        }

        List<ImageUser> imageUsers = user.getImageUser();

        for(int i = 1; i < imageUsers.size(); i++) {
            if(!multipartFiles[i - 1].isEmpty()) {
                imageUsers.get(i).setImage(imageService
                        .replaceImage(imageUsers.get(i).getImage().getId(),
                                multipartFiles[i - 1])
                );
            }
        }

        user.setRegistrationComplete(true);
        user.setImageUser(imageUsers);

        return UserMapper.userToUserDto(userRepository.save(user));
    }

    @Transactional
    public UserDto registerUserComplete(UserCompleteRegisterDto userCompleteRegisterDto, MultipartFile[] multipartFiles) {
        User user = userRepository.findById(userCompleteRegisterDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("???????????????????????? ???? ????????????!"));

        try {
            long tin = Long.parseLong(userCompleteRegisterDto.getPassportData().getTin());
        } catch (Exception ex) {
            throw new IllegalArgumentException("???????????????? ??????!");
        }

        if(userRepository.existsByPassportDataTin(userCompleteRegisterDto.getPassportData().getTin())) {
            throw new AlreadyExistException("???????????????????????? ?? ?????????? ??????: " + userCompleteRegisterDto.getPassportData().getTin() + " ?????? ????????????????????");
        }

        if(!PhoneNumberValidator.isPhoneNumberValid(userCompleteRegisterDto.getPhoneNumber())) {
            throw new IllegalArgumentException("?????????? ???????????????? - " + userCompleteRegisterDto.getPhoneNumber() + " ????????????????");
        }

        if(multipartFiles.length != 3) {
            throw new IllegalArgumentException("???????????? ???????? 3 ???????????????????? ????????????????");
        }

        List<ImageUser> imageUsers = user.getImageUser();

        for(int i = 1; i < imageUsers.size(); i++) {
            if(!multipartFiles[i - 1].isEmpty()) {
                imageUsers.get(i).setImage(imageService
                        .replaceImage(imageUsers.get(i).getImage().getId(),
                                multipartFiles[i - 1])
                );
            }
        }

        user.setFirstName(userCompleteRegisterDto.getFirstName());
        user.setLastName(userCompleteRegisterDto.getLastName());
        user.setPhoneNumber(userCompleteRegisterDto.getPhoneNumber());
        user.setDateOfBirth(userCompleteRegisterDto.getDateOfBirth());
        user.setBlocked(false);
        user.setPassportData(PassportDataMapper
                .passportDataDtoToPassportData(userCompleteRegisterDto.getPassportData()));
        user.setRegisteredAddress(RegisteredAddressMapper
                .registeredAddressDtoToRegisteredAddress(userCompleteRegisterDto.getRegisteredAddress()));
        user.setResidenceAddress(ResidenceAddressMapper
                .residenceAddressDtoToResidenceAddress(userCompleteRegisterDto.getResidenceAddress()));
        user.setRegistrationComplete(true);
        user.setImageUser(imageUsers);

        return UserMapper.userToUserDto(userRepository.save(user));
    }

    public UserDto acceptUser(Long id) throws MessagingException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("???????????????????????? ???? ????????????!"));

        user.setVerifiedByTechSupport(true);

        EmailUtility.sendUserAcceptanceMessageToEmail(user.getEmail(),
                user.getFirstName() + " " + user.getLastName(), javaMailSender);

        return UserMapper.userToUserDto(userRepository.save(user));
    }

    public UserDto rejectUser(Long id) throws MessagingException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("???????????????????????? ???? ????????????!"));

        user.setLastName("");
        user.setPhoneNumber("");
        user.setDateOfBirth(null);

        PassportData passportData = user.getPassportData();
        passportData.setTin("");
        passportData.setDateOfIssue(null);
        passportData.setAuthority("");
        user.setPassportData(passportData);

        RegisteredAddress registeredAddress = user.getRegisteredAddress();
        registeredAddress.setRegion("");
        registeredAddress.setCityOrVillage("");
        registeredAddress.setDistrict("");
        registeredAddress.setStreet("");
        registeredAddress.setHouseNumber(0);
        registeredAddress.setApartmentNumber(0);
        user.setRegisteredAddress(registeredAddress);

        ResidenceAddress residenceAddress = user.getResidenceAddress();
        residenceAddress.setRegion("");
        residenceAddress.setCityOrVillage("");
        residenceAddress.setDistrict("");
        residenceAddress.setStreet("");
        residenceAddress.setHouseNumber(0);
        residenceAddress.setApartmentNumber(0);
        user.setResidenceAddress(residenceAddress);

        List<ImageUser> imageUsers = user.getImageUser();

        for(int i = 1; i < 4; i++) {
            Image image = imageUsers.get(i).getImage();
            image.setUrl("");

            imageUsers.get(i).setImage(image);
        }

        user.setImageUser(imageUsers);

        user.setRegistrationComplete(false);
        user.setVerifiedByTechSupport(false);

        EmailUtility.sendUserRejectionMessageToEmail(user.getEmail(),
                user.getFirstName() + " " + user.getLastName(), javaMailSender);

        return UserMapper.userToUserDto(userRepository.save(user));
    }

    @Transactional
    public MessageResponse sendResetPasswordCode(String email) throws MessagingException {

        Integer code = RandomUtils.nextInt(100000, 999999);

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("???? ?????????????? ?????????? ???????????????????????? ?? email: " + email));

        user.setResetPasswordCode(code);
        user.setCodeExpirationDate(Instant.now().plusMillis(resetPasswordCodeExpirationMs));
        userRepository.save(user);

        EmailUtility.sendConfirmationCodeToEmail(email, code, javaMailSender);

        return new MessageResponse("???? ?????????????????? ?????? ?????????????????????????? ???? ???????? ?????????????????????? ??????????. ????????????????????, ?????????????????? ???????? ??????????!");
    }

    public JwtResponse verifyResetPasswordCodeExpirationDate(Integer code) {
        User user = userRepository.findUserByResetPasswordCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("???? ?????????????? ?????????? ???????????????????????? ?? ??????????: " + code));

        if(user.getCodeExpirationDate().compareTo(Instant.now()) < 0) {
            user.setResetPasswordCode(null);
            user.setCodeExpirationDate(null);
            userRepository.save(user);
            throw new ResetPasswordCodeExpirationException("???????? ???????????????? ???????? ?????????????????????????? ??????????! ????????????????????, ?????????????????? ?????? ????????????????...");
        }

        return JwtResponse.builder()
                .accessToken(jwtProvider.generateAccessToken(user))
                .refreshToken("")
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .middleName(user.getMiddleName())
                .dateOfBirth(user.getDateOfBirth())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().getName())
                .isRegistrationComplete(user.isRegistrationComplete())
                .isVerifiedByTechSupport(user.isVerifiedByTechSupport())
                .status(user.getStatus())
                .imageUser(user.getImageUser())
                .build();
    }

    @Transactional
    public MessageResponse updatePassword(ChangePasswordDto changePasswordDTO) {
        User user = userRepository.findById(changePasswordDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("???????????????????????? ???? ????????????!"));

        if(changePasswordDTO.getOldPassword().equals("")) {
            String encodedPassword = passwordEncoder.encode(changePasswordDTO.getNewPassword());

            user.setPassword(encodedPassword);
            user.setResetPasswordCode(null);
            user.setCodeExpirationDate(null);

            userRepository.save(user);
        } else {
            if(passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
                String encodedPassword = passwordEncoder.encode(changePasswordDTO.getNewPassword());

                user.setPassword(encodedPassword);
                user.setResetPasswordCode(null);
                user.setCodeExpirationDate(null);

                userRepository.save(user);
            } else {
                throw new IllegalArgumentException("???????????????? ???????????? ????????????");
            }
        }
        return new MessageResponse("???????????? ?????? ?????????????? ????????????????!");
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public MessageResponse deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("???????????????????????? ???? ????????????!"));

        userRepository.delete(user);

        return new MessageResponse("???????????????????????? ?????????????? ????????????!");
    }

    public UserProfileDto getProfile() {
        User user = userRepository.findByEmail(getAuthentication().getName());

        if (user == null) {
            throw new ResourceNotFoundException("User is not authenticated");
        }

        return mapToProfileDto(user);
    }

    public UserProfileDto getProfileById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User was not found with ID: " + id)
                );

        return mapToProfileDto(user);
    }

    public UserProfileDto mapToProfileDto(User user) {
        int rating = 100 - complaintRepository
                .getComplaintsNumberByAddresseeId(user.getId());

        for(Product p: user.getProducts()) {
            List<Review> reviews = reviewRepository.findAllByProductId(p.getId());

            double sum = (double) reviews.stream()
                    .mapToInt(Review::getStar).sum() / reviews.size();

            if(sum >= 4.9) {
                rating += 5;
            }
        }

        UserProfileDto dto = new UserProfileDto();

        dto.setProfileRating(rating > 100 ? 100 : Math.max(rating, 1));
        dto.setUserId(user.getId());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setFullName(user.getLastName() + " " + user.getFirstName());
        dto.setProducts(
                productRepository.findAllByUserIdOrderByActiveAscCreationTimeDesc(user.getId()).stream()
                        .map(productService::mapToProductPageDto)
                        .collect(Collectors.toList())
        );

        List<ImageUser> images = user.getImageUser();

        images.forEach(i -> {
            if(i.getOrderNumber() == (byte) 1) {
                dto.setImageUrl(i.getImage().getUrl());
            }
        });

        return dto;
    }

    public String updateMainImage(Long imageId, MultipartFile file) {
        Image image = imageService.replaceImage(imageId, file);

        if(image == null) {
            throw new BadRequestException("?????????????????? ???????????? ?? ???????????????? ???????????????????? ????????????????.");
        }

        return "???????????????? ???????? ?????????????? ????????????????.";
    }

    public String deleteMainImage(Long imageId) throws IOException {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Image was not found with ID: " + imageId)
                );

        imageService.deleteImageFromCloudinary(image.getPublicId());

        image.setUrl("");
        image.setPublicId("");

        imageRepository.save(image);

        return "???????????????? ???????? ?????????????? ??????????????.";
    }

}
