/*
    2025/02/04
    전규찬
    User 모델 클래스 생성
*/
package com.shelfy.dto;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private int userId;
    private String userUid;
    private String userPwd;
    private String userEmail;
    private String userNick;
    private String userProfile;
    private String userGenre;
    private String userCreatedAt;
    private String userUpdatedAt;
    private String userDeletedAt;
    private boolean success;
}
