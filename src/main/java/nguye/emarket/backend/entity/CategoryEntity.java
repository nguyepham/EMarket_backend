package nguye.emarket.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category", schema = "emarket")
@Getter
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, updatable = false)
    private int id;

    @Setter
    @Column(name = "NAME", unique = true, nullable = false, length = 50)
    private String name;

    @Setter
    @Column(name = "LEVEL", nullable = false)
    private int level;

    @Setter
    @Column(name = "PATH", nullable = false)
    private String path;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "PARENT_NAME", referencedColumnName = "NAME", nullable = false)
    @Column(name = "PARENT_NAME", nullable = false)
    private String parentName;

    @ManyToMany(mappedBy = "categories")
    private final List<ProductEntity> products = new ArrayList<>();

    protected CategoryEntity() {
    }

    public CategoryEntity(String name, int level, String path, String parent) {
        this.name = name;
        this.level = level;
        this.path = path;
        this.parentName = parent;
    }
}
