package shoppingMall.project.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refreshToken")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",updatable = false)
    private Long id;
    @Column(name = "member_id",nullable = false,unique = true)
    private Long userId;
    @Column(name = "refreshToken",nullable = false)
    private String refreshToken;

    public RefreshToken(Long memberId, String refreshToken){
        this.userId = memberId;
        this.refreshToken =refreshToken;
    }
    public RefreshToken update(String refreshToken){
        this.refreshToken = refreshToken;
        return this;
    }
}
