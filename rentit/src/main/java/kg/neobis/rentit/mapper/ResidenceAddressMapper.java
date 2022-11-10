package kg.neobis.rentit.mapper;

import kg.neobis.rentit.dto.ResidenceAddressDto;
import kg.neobis.rentit.entity.ResidenceAddress;

public class ResidenceAddressMapper {

    public static ResidenceAddress residenceAddressDtoToResidenceAddress(ResidenceAddressDto residenceAddressDto) {
        ResidenceAddress residenceAddress = ResidenceAddress.builder()
                .id(residenceAddressDto.getId())
                .region(residenceAddressDto.getRegion())
                .cityOrVillage(residenceAddressDto.getCityOrVillage())
                .district(residenceAddressDto.getDistrict())
                .street(residenceAddressDto.getStreet())
                .houseNumber(residenceAddressDto.getHouseNumber())
                .apartmentNumber(residenceAddressDto.getApartmentNumber())
                .build();

        return residenceAddress;
    }

    public static ResidenceAddressDto residenceAddressToResidenceAddressDto(ResidenceAddress residenceAddress) {
        ResidenceAddressDto residenceAddressDto = ResidenceAddressDto.builder()
                .id(residenceAddress.getId())
                .region(residenceAddress.getRegion())
                .cityOrVillage(residenceAddress.getCityOrVillage())
                .district(residenceAddress.getDistrict())
                .street(residenceAddress.getStreet())
                .houseNumber(residenceAddress.getHouseNumber())
                .apartmentNumber(residenceAddress.getApartmentNumber())
                .build();

        return residenceAddressDto;
    }
}
