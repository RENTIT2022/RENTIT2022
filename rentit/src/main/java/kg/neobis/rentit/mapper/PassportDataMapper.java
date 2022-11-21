package kg.neobis.rentit.mapper;

import kg.neobis.rentit.dto.PassportDataDto;
import kg.neobis.rentit.entity.PassportData;

public class PassportDataMapper {

    public static PassportData passportDataDtoToPassportData(PassportDataDto passportDataDto) {
        PassportData passportData = PassportData.builder()
                .id(passportDataDto.getId())
                .tin(passportDataDto.getTin())
                .dateOfIssue(passportDataDto.getDateOfIssue())
                .authority(passportDataDto.getAuthority())
                .build();

        return passportData;
    }

    public static PassportDataDto passportDataToPassportDataDto(PassportData passportData) {
       PassportDataDto passportDataDto = PassportDataDto.builder()
                .id(passportData.getId())
                .tin(passportData.getTin())
                .dateOfIssue(passportData.getDateOfIssue())
                .authority(passportData.getAuthority())
                .build();

        return passportDataDto;
    }
}
