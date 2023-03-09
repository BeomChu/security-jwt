package me.beomchu.security.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue
    private Long id;

    private String username;

    private String password;

    private String email;
    private String provider;
    private String providerId;

    private String role;

    private LocalDateTime createDate;

    @PrePersist
    public void createDate(){
        this.createDate = LocalDateTime.now();
    }

    @Builder
    public User(String username, String password, String email, String provider, String providerId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.role = "ROLE_USER";
    }
}
