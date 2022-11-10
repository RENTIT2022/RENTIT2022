package kg.neobis.rentit.security.oauth2;

import kg.neobis.rentit.entity.*;
import kg.neobis.rentit.enums.AuthProvider;
import kg.neobis.rentit.enums.Status;
import kg.neobis.rentit.exception.OAuth2AuthenticationProcessingException;
import kg.neobis.rentit.mapper.RoleMapper;
import kg.neobis.rentit.repository.UserRepository;
import kg.neobis.rentit.security.oauth2.user.OAuth2UserInfo;
import kg.neobis.rentit.security.oauth2.user.OAuth2UserInfoFactory;
import kg.neobis.rentit.service.ImageService;
import kg.neobis.rentit.service.ImageUserService;
import kg.neobis.rentit.service.RoleService;
import kg.neobis.rentit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final ImageService imageService;
    private final ImageUserService imageUserService;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Электронная почта не найдена от поставщика OAuth2");
        }

        Optional<User> userOptional = userRepository.findUserByEmail(oAuth2UserInfo.getEmail());
        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Похоже, вы зарегистрированы на " +
                        user.getProvider() + " аккаунт. Пожалуйста, используйте свой " + user.getProvider() +
                        " аккаунт для входа.");
            }
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return userService.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();

        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());

        String[] name = oAuth2UserInfo.getName().split(" ");
        if(name.length == 2) {
            user.setFirstName(name[0]);
            user.setLastName(name[1]);
        } else {
            user.setFirstName(oAuth2UserInfo.getName());
        }

        user.setEmail(oAuth2UserInfo.getEmail());
        user.setPassportData(new PassportData(" ", null, " "));
        user.setRegisteredAddress(new RegisteredAddress(" ", " ", " ", " ", 0, 0));
        user.setResidenceAddress(new ResidenceAddress(" ", " ", " ", " ", 0, 0));
        user.setRegistrationComplete(false);
        user.setVerifiedByTechSupport(false);
        user.setStatus(Status.ACTIVE);

        Role role = RoleMapper.roleDtoToRole(roleService.getRoleByName("USER"));
        user.setRole(role);

        List<ImageUser> imageUsers = new ArrayList<>();

        Image image = new Image(oAuth2UserInfo.getImageUrl(), " ");
        ImageUser imageUser = new ImageUser();
        imageUser.setOrderNumber((byte) 1);
        imageUser.setImage(imageService.saveImage(image));
        imageUsers.add(imageUserService.saveImageUser(imageUser));

        for(int i = 2; i <= 4; i++) {
            Image image1 = new Image(" ", " ");
            ImageUser imageUser1 = new ImageUser();

            imageUser1.setOrderNumber((byte) i);
            imageUser1.setImage(imageService.saveImage(image1));

            imageUsers.add(imageUserService.saveImageUser(imageUser1));
        }

        user.setImageUser(imageUsers);

        return userRepository.save(user);
    }
}
