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
@Table(name = "`registered_address`")
public class RegisteredAddress {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "registered_address_seq"
    )
    @SequenceGenerator(
            name = "registered_address_seq",
            sequenceName = "registered_address_seq",
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

    public RegisteredAddress(String region, String cityOrVillage, String district, String street, int houseNumber, int apartmentNumber) {
        this.region = region;
        this.cityOrVillage = cityOrVillage;
        this.district = district;
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
    }
}
