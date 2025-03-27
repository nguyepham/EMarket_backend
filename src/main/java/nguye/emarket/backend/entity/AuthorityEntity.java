package nguye.emarket.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "authority", schema = "emarket")
@Getter
public class AuthorityEntity implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, updatable = false)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID", nullable = false)
    private UserEntity user;

    @Setter
    @Column(name = "ROLE", nullable = false, length = 15)
    private String authority;

    protected AuthorityEntity() {
    }

    public AuthorityEntity(UserEntity user, String authority) {
        this.user = user;
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
