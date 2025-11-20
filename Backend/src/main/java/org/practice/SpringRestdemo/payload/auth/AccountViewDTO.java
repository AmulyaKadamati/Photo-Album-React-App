package org.practice.SpringRestdemo.payload.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountViewDTO {

    private Long id;

    private String email;

    private String authorities;

}
