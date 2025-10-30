package com.janr.itsm.service.ticket;

import com.janr.itsm.auth.model.User;
import com.janr.itsm.dto.TicketDto;
import com.janr.itsm.model.Ticket;

import java.util.List;

public interface TicketService {
    List<TicketDto> getAllTicket();
    TicketDto getTicketById(Long id, User currentUser);
    TicketDto createTicket(Ticket ticket);
    List<TicketDto> getTicketForUser(User currentUser);
    TicketDto assignTicket(Long ticketId,  Long staffId);
    TicketDto convertToDto(Ticket ticket);
    List<TicketDto> getConvertedTicketItem(List<Ticket> tickets);
    TicketDto updatePatchPriority(Long id, Ticket ticket);
    void deleteTicket(Long id);

}
