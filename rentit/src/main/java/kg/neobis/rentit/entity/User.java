package kg.neobis.rentit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kg.neobis.rentit.enums.AuthProvider;
import kg.neobis.rentit.enums.Status;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`user`")
public class User implements OAuth2User, UserDetails {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_seq"
    )
    @SequenceGenerator(
            name = "user_seq",
            sequenceName = "user_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "passport_data_id"
    )
    private PassportData passportData;

    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "registered_address_id"
    )
    private RegisteredAddress registeredAddress;

    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "residence_address_id"
    )
    private ResidenceAddress residenceAddress;

    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(
            name = "role_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_USER_ROLE")
    )
    private Role role;

    private String password;

    @Column(name = "is_registration_complete")
    private boolean isRegistrationComplete;

    @Column(name = "is_verified_by_tech_support")
    private boolean isVerifiedByTechSupport;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    @Transient
    private Map<String, Object> attributes;

    @Column(name = "reset_password_code")
    @JsonIgnore
    private Integer resetPasswordCode;

    @Column(name = "code_expiration_date")
    @JsonIgnore
    private Instant codeExpirationDate;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private Set<Product> products;

    @ManyToMany(
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "acc_owner_follower",
            joinColumns = @JoinColumn(name = "acc_owner"),
            inverseJoinColumns = @JoinColumn(name = "follower")
    )
    private List<User> followers;

    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "user_favorite",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> favoriteProducts;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @JoinColumn(
            name = "image_user_id",
            referencedColumnName = "id"
    )
    private List<ImageUser> imageUser = new ArrayList<>();

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "user"
    )
    private List<View> views;

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY
    )
    private List<Booking> bookings;


    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static UserDetails getUserDetails(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getStatus().equals(Status.ACTIVE),
                true,
                true,
                true,
                user.role.getAuthorities()
        );
    }
}
