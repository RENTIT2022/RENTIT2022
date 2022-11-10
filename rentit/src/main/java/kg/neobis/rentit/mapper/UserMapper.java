package kg.neobis.rentit.mapper;

import kg.neobis.rentit.dto.UserDto;
import kg.neobis.rentit.entity.User;

public class UserMapper {

    public static User userDtoToUser(UserDto userDTO) {
        User user = User.builder()
                .id(userDTO.getId())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .middleName(userDTO.getMiddleName())
                .dateOfBirth(userDTO.getDateOfBirth())
                .passportData(PassportDataMapper.passportDataDtoToPassportData(userDTO.getPassportData()))
                .registeredAddress(RegisteredAddressMapper.registeredAddressDtoToRegisteredAddress(userDTO.getRegisteredAddress()))
                .residenceAddress(ResidenceAddressMapper.residenceAddressDtoToResidenceAddress(userDTO.getResidenceAddress()))
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .role(RoleMapper.roleDtoToRole(userDTO.getRole()))
                .password(userDTO.getPassword())
                .isRegistrationComplete(userDTO.isRegistrationComplete())
                .isVerifiedByTechSupport(userDTO.isVerifiedByTechSupport())
                .status(userDTO.getStatus())
                .imageUser(userDTO.getImageUser())
                .provider(userDTO.getProvider())
                .providerId(userDTO.getProviderId())
                .attributes(userDTO.getAttributes())
                .resetPasswordCode(userDTO.getResetPasswordCode())
                .codeExpirationDate(userDTO.getCodeExpirationDate())
                .build();

        return user;
    }

    public static UserDto userToUserDto(User user) {
        UserDto userDTO = UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .middleName(user.getMiddleName())
                .dateOfBirth(user.getDateOfBirth())
                .passportData(PassportDataMapper.passportDataToPassportDataDto(user.getPassportData()))
                .registeredAddress(RegisteredAddressMapper.registeredAddressToRegisteredAddressDto(user.getRegisteredAddress()))
                .residenceAddress(ResidenceAddressMapper.residenceAddressToResidenceAddressDto(user.getResidenceAddress()))
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(RoleMapper.roleToRoleDto(user.getRole()))
                .password(user.getPassword())
                .isRegistrationComplete(user.isRegistrationComplete())
                .isVerifiedByTechSupport(user.isVerifiedByTechSupport())
                .status(user.getStatus())
                .imageUser(user.getImageUser())
                .provider(user.getProvider())
                .providerId(user.getProviderId())
                .attributes(user.getAttributes())
                .resetPasswordCode(user.getResetPasswordCode())
                .codeExpirationDate(user.getCodeExpirationDate())
                .build();

        return userDTO;
    }
}
