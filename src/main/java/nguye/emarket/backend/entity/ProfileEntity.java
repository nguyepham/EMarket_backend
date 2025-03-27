package nguye.emarket.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "profile", schema = "emarket")
@Getter
public class ProfileEntity {

    @Id
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "ID")
    private UserEntity user;

    @Setter
    @Column(name = "AUTHORITY", nullable = false, length = 16)
    private String authority;

    @Setter
    @Column(name = "FIRST_NAME", length = 36)
    private String firstName;

    @Setter
    @Column(name = "LAST_NAME", length = 36)
    private String lastName;

    @Setter
    @Column(name = "EMAIL", length = 100)
    private String email;

    @Setter
    @Column(name = "AGE")
    private int age;

    @Setter
    @Column(name = "GENDER", length = 7)
    private String gender;

    protected ProfileEntity() {
    }

    public ProfileEntity(UserEntity user, int age) {
        this.user = user;
        this.authority = "ROLE_CUSTOMER";
        this.age = age;
    }
}
