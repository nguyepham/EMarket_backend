package nguye.emarket.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product_image", schema = "emarket")
@Getter
public class ProductImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, updatable = false)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID", nullable = false)
    private ProductEntity product;

    @Setter
    @Column(name = "IMAGE_URL", columnDefinition = "TEXT")
    private String imageUrl;

    protected ProductImageEntity() {
    }

    public ProductImageEntity(ProductEntity product, String imageUrl) {
        this.product = product;
        this.imageUrl = imageUrl;
    }
}
