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
@Table(name = "`view`")
public class View {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "view_seq"
    )
    @SequenceGenerator(
            name = "view_seq",
            sequenceName = "view_seq",
            allocationSize = 1
    )
    private Long id;

    private int viewNum;

    @ManyToOne
    @JoinColumn(
            name = "user_id"
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name = "category_id"
    )
    private Category category;

}
