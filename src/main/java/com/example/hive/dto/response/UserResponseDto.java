package com.example.hive.dto.response;

import com.example.hive.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

    private String fullName;

    private String email;

    private String phoneNumber;

    private String address;

    private List<Task> userTasks;
}
