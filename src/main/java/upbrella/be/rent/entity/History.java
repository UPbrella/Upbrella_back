package upbrella.be.rent.entity;

import lombok.*;
import upbrella.be.store.entity.StoreMeta;
import upbrella.be.umbrella.entity.Umbrella;
import upbrella.be.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class History {

    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    @JoinColumn(name = "umbrella_id")
    private Umbrella umbrella;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "rent_store_meta_id")
    private StoreMeta rentStoreMeta;
    @ManyToOne
    @JoinColumn(name = "return_store_meta_id")
    private StoreMeta returnStoreMeta;
    private LocalDateTime rentedAt;
    private LocalDateTime returnedAt;
    private String etc;

    public static History ofCreatedByNewRent(Umbrella umbrella, User user, StoreMeta rentStoreMeta) {
        return History.builder()
                .umbrella(umbrella)
                .user(user)
                .rentStoreMeta(rentStoreMeta)
                .rentedAt(LocalDateTime.now())
                .build();
    }
}
