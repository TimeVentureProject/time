package com.TimeVenture.model.dto.member;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class UpdateNicknameRequest {
    private String email;
    private String newNickname;
}
