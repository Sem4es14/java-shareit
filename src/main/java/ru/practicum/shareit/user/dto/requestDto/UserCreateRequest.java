package ru.practicum.shareit.user.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
    @NotNull
    private String name;
    @NotNull
    @Email
    private String email;
}
