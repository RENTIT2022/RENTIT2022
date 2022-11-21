package kg.neobis.rentit.mapper;

import kg.neobis.rentit.dto.RegisteredAddressDto;
import kg.neobis.rentit.entity.RegisteredAddress;

public class RegisteredAddressMapper {

    public static RegisteredAddress registeredAddressDtoToRegisteredAddress(RegisteredAddressDto registeredAddressDto) {
       RegisteredAddress registeredAddress = RegisteredAddress.builder()
                .id(registeredAddressDto.getId())
                .region(registeredAddressDto.getRegion())
                .cityOrVillage(registeredAddressDto.getCityOrVillage())
                .district(registeredAddressDto.getDistrict())
                .street(registeredAddressDto.getStreet())
                .houseNumber(registeredAddressDto.getHouseNumber())
                .apartmentNumber(registeredAddressDto.getApartmentNumber())
                .build();

        return registeredAddress;
    }

    public static RegisteredAddressDto registeredAddressToRegisteredAddressDto(RegisteredAddress registeredAddress) {
        RegisteredAddressDto registeredAddressDto = RegisteredAddressDto.builder()
                .id(registeredAddress.getId())
                .region(registeredAddress.getRegion())
                .cityOrVillage(registeredAddress.getCityOrVillage())
                .district(registeredAddress.getDistrict())
                .street(registeredAddress.getStreet())
                .houseNumber(registeredAddress.getHouseNumber())
                .apartmentNumber(registeredAddress.getApartmentNumber())
                .build();

        return registeredAddressDto;
    }
}
