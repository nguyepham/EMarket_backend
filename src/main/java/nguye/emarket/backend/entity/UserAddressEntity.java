package nguye.emarket.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_address", schema = "emarket")
@Getter
public class UserAddressEntity {

    @Id
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "ID")
    private UserEntity user;

    @Setter
    @Column(name = "PROVINCE", length = 40)
    private String province;

    @Setter
    @Column(name = "DISTRICT", length = 40)
    private String district;

    @Setter
    @Column(name = "STREET_AND_NUMBER", length = 40)
    private String streetAndNumber;

    protected UserAddressEntity() {
    }

    public UserAddressEntity(UserEntity user, String province, String district, String streetAndNumber) {
        this.user = user;
        this.province = province;
        this.district = district;
        this.streetAndNumber = streetAndNumber;
    }
}
