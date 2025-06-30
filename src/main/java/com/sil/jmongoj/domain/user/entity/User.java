package com.sil.jmongoj.domain.user.entity;

import com.sil.jmongoj.global.entity.Base;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User extends Base {

    @Id
    private String id;
    @Indexed(unique = true)
    private String username;    // 아이디
    private String password;    // 비밀번호
    private String name;        // 이름
    private String email;       // 이메일
    private String role;        // 롤
    private LocalDateTime joinedAt; // 가입일시
    private LocalDateTime signedAt; // 로그인일시
    @Builder.Default
    private boolean enabled = true; // 활성화여부

}