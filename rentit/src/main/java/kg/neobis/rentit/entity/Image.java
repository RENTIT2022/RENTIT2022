package kg.neobis.rentit.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`image`")
public class Image {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "image_seq"
    )
    @SequenceGenerator(
            name = "image_seq",
            sequenceName = "image_seq",
            allocationSize = 1
    )
    private Long id;

    private String url;

    private String publicId;

    public Image(String url, String publicId) {
        this.url = url;
        this.publicId = publicId;
    }
}
