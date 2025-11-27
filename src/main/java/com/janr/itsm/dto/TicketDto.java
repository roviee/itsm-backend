package com.janr.itsm.dto;

import com.janr.itsm.auth.dto.UserDto;
import com.janr.itsm.enums.Priority;
import com.janr.itsm.enums.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {
    private Long id;

    private String ticketNumber;

    @NotBlank(message = "Title must not blank")
    private String title;

    @NotBlank(message = "Description must not blank")
    private String description;

    @NotBlank(message = "Category must not blank")
    private String category;

    private Priority priority;

    private Status status;

    private UserDto createdBy;

    private UserDto assignedTo;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
