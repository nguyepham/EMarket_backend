package nguye.emarket.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "password", schema = "emarket")
@Getter
public class PasswordEntity {

    @Id
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "ID")
    private UserEntity user;

    @Setter
    @Column(name = "TEXT", nullable = false, length = 60)
    private String text;

    @Setter
    @Column(name = "UPDATED_AT", nullable = false)
    private java.sql.Timestamp updatedAt;

    protected PasswordEntity() {
    }

    public PasswordEntity(UserEntity user, String text, Timestamp updatedAt) {
        this.user = user;
        this.text = text;
        this.updatedAt = updatedAt;
    }
}
