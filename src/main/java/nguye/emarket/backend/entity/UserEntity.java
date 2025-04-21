package nguye.emarket.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user", schema = "emarket")
@Getter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, updatable = false)
    private int id;

    @Setter
    @Column(name = "IMAGE_URL", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "USERNAME", nullable = false, updatable = false, unique = true, length = 36)
    private String username;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<CartItemEntity> cartItems = new HashSet<>();

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<OrderEntity> orders = new HashSet<>();

    protected UserEntity() {
    }

    public UserEntity(String username) {
        this.username = username;
    }
}
