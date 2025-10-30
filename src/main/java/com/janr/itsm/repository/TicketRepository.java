package com.janr.itsm.repository;

import com.janr.itsm.auth.model.User;
import com.janr.itsm.enums.Status;
import com.janr.itsm.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByCreatedBy(User createdBy);

    List<Ticket> findByAssignedToOrStatus(User assignedTo, Status ticketStatus);
}
