package com.janr.itsm.response;

import com.janr.itsm.dto.TicketDto;

import java.util.List;

public record TicketResponse(List<TicketDto> ticketDtos,
                             Integer pageNumber,
                             Integer pageSize,
                             long totalElements,
                             int totalPages,
                             boolean isFirst,
                             boolean isLast) {
}
