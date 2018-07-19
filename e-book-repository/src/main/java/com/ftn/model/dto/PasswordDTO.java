package com.ftn.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by Jasmina on 19/07/2018.
 */
@Data
public class PasswordDTO {

    @NotNull
    private String oldPassword;

    @NotNull
    private String newPassword;

    @NotNull
    private String repeatedPassword;
}
