package nguye.emarket.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product", schema = "emarket")
@Getter
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, updatable = false)
    private int id;

    @Setter
    @Column(name = "COVER_IMAGE_URL", columnDefinition = "TEXT")
    private String coverImageUrl;

    @Setter
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Setter
    @Column(name = "UNIT_PRICE", nullable = false)
    private int unitPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SELLER_USERNAME", referencedColumnName = "USERNAME", nullable = false)
    private UserEntity seller;

    @Setter
    @Column(name = "UPDATED_AT", nullable = false)
    private Timestamp updatedAt;

    @Setter
    @Column(name = "SHORT_DESCRIPTION", length = 100, columnDefinition = "TEXT")
    private String shortDescription;

    @Setter
    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @Setter
    @Column(name = "QTY_IN_STOCK", nullable = false)
    private int qtyInStock;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ProductImageEntity> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<CartItemEntity> cartItems = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrderItemEntity> orderItems = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "category_product",
            schema = "emarket",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private final List<CategoryEntity> categories = new ArrayList<>();

    protected ProductEntity() {
    }

    public ProductEntity(
            UserEntity seller,
            String name,
            int unitPrice,
            Timestamp updatedAt,
            String shortDescription,
            int qtyInStock) {
        this.seller = seller;
        this.name = name;
        this.unitPrice = unitPrice;
        this.updatedAt = updatedAt;
        this.shortDescription = shortDescription;
        this.qtyInStock = qtyInStock;
    }
}
