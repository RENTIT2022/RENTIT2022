package kg.neobis.rentit.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`residence_address`")
public class ResidenceAddress {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "residence_address_seq"
    )
    @SequenceGenerator(
            name = "residence_address_seq",
            sequenceName = "residence_address_seq",
            allocationSize = 1
    )
    private Long id;

    private String region;

    @Column(name = "city_or_village")
    private String cityOrVillage;

    private String district;

    private String street;

    @Column(name = "house_number")
    private int houseNumber;

    @Column(name = "apartment_number")
    private int apartmentNumber;

    public ResidenceAddress(String region, String cityOrVillage, String district, String street, int houseNumber, int apartmentNumber) {
        this.region = region;
        this.cityOrVillage = cityOrVillage;
        this.district = district;
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
    }
}
