package com.janr.itsm.request;

import com.janr.itsm.enums.Priority;
import com.janr.itsm.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketUpdateRequest {
    private Status status;
    private Priority priority;
}
