package kg.neobis.rentit.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`chat_message`")
public class ChatMessage {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "chat_message_seq"
    )
    @SequenceGenerator(
            name = "chat_message_seq",
            sequenceName = "chat_message_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String content;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_updated")
    private Date dateUpdated;

    @Column(name = "date_deleted")
    private Date dateDeleted;
}
