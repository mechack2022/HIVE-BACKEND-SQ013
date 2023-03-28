package com.example.hive.controller;

import com.example.hive.constant.AppConstants;
import com.example.hive.dto.request.TaskDto;
import com.example.hive.dto.response.AppResponse;
import com.example.hive.dto.response.TaskResponseDto;
import com.example.hive.entity.User;
import com.example.hive.exceptions.ResourceNotFoundException;
import com.example.hive.repository.UserRepository;
import com.example.hive.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private TaskService taskService;
    private UserRepository userRepository;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/")
//    @PreAuthorize("hasRole('TASKER')")
    public ResponseEntity<AppResponse<TaskResponseDto>> createTask(@Valid @RequestBody TaskDto taskDto, HttpServletRequest request) {
        AppResponse<TaskResponseDto> createdTask = taskService.createTask(taskDto, request);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PostMapping("/{taskId}/accept")
    public ResponseEntity<String> acceptTask(@PathVariable("taskId") String taskId, Principal principal) {
        try {
            String email = principal.getName();
            User currentUser = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("user not found"));
            taskService.acceptTask(currentUser, taskId);
            return new ResponseEntity<>("Task accepted", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>("Task not available", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{taskId}")
    public AppResponse<TaskResponseDto> updateTask(
            @PathVariable UUID taskId,
            @RequestBody TaskDto taskDto) {
        return taskService.updateTask(taskId, taskDto);

    }

    @GetMapping(path = "task/details/{taskId}")
    public ResponseEntity<AppResponse<TaskResponseDto>> findTaskById(@PathVariable UUID taskId) {
        TaskResponseDto taskFound = taskService.findTaskById(taskId);

        // creates an ApiResponse object with the retrieved task data
        AppResponse<TaskResponseDto> apiResponse = new AppResponse<>();
        apiResponse.setResult(taskFound);
        apiResponse.setStatusCode(HttpStatus.FOUND.toString()); // a status code indicating success
        apiResponse.setMessage("Task fetched successfully"); // a message describing the response

        // returns an HTTP response with a JSON response containing the ApiResponse object
        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("task/list")
    public ResponseEntity<AppResponse<Object>> findAllTasks(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        var tasksFound = taskService.findAll(pageNo, pageSize, sortBy, sortDir);

        return ResponseEntity.status(200).body(AppResponse.builder().statusCode("00").isSuccessful(true).result(tasksFound).build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<TaskResponseDto>> searchTasks(
            @RequestParam(value = "text") String text,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(taskService.searchTasksBy(text, pageNo, pageSize, sortBy, sortDir));
    }
}

