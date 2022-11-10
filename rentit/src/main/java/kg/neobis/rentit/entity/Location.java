package kg.neobis.rentit.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`location`")
public class Location {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "location_seq"
    )
    @SequenceGenerator(
            name = "location_seq",
            sequenceName = "location_seq",
            allocationSize = 1
    )
    private Long id;

    private double x;

    private double y;

}
