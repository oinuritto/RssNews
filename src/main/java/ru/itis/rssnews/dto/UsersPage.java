package ru.itis.rssnews.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersPage {
    private List<UserDto> users;
    private Integer totalPagesCount;
}