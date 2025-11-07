package com.janr.itsm.service.ticket;

import com.janr.itsm.auth.dto.UserDto;
import com.janr.itsm.auth.enums.Role;
import com.janr.itsm.auth.model.User;
import com.janr.itsm.auth.repository.UserRepository;
import com.janr.itsm.dto.TicketDto;
import com.janr.itsm.enums.Priority;
import com.janr.itsm.enums.Status;
import com.janr.itsm.exceptions.NotFoundException;
import com.janr.itsm.model.Ticket;
import com.janr.itsm.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketImpl implements TicketService {
    final TicketRepository ticketRepository;
    final UserRepository userRepository;

    @Override
    public List<TicketDto> getAllTicket() {
        return getConvertedTicketItem(ticketRepository.findAll());
    }

    @Override
    public TicketDto getTicketById(Long id, User currentUser) {
        Ticket ticket =  ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        if (canViewTicket(currentUser, ticket)) {
            return convertToDto(ticket);
        }

        throw new AccessDeniedException("You cannot access this ticket");
    }

    private boolean canViewTicket(User user, Ticket ticket) {
        return switch (user.getRole()) {
            case ADMIN -> true;
            case SUPPORT_STAFF -> {
                yield user.equals(ticket.getAssignedTo());
            }
            default -> user.equals(ticket.getCreatedBy());
        };
    }

    @Override
    public TicketDto createTicket(Ticket ticket) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalStateException("Authenticated user not found in database: " + email));

            ticket.setCreatedBy(user);
            ticket.setAssignedTo(null);
            ticket.setStatus(Status.OPEN);

            if (ticket.getPriority() != null) {
                ticket.setPriority(ticket.getPriority());
            } else {
                ticket.setPriority(Priority.MODERATE);
            }

            if (ticket.getTitle().toLowerCase().contains("urgent")) {
                ticket.setPriority(Priority.CRITICAL);
            }

            return convertToDto(ticketRepository.save(ticket));
        } else {
            throw new IllegalStateException("User not authenticated or authentication principal is not a UserDetails instance");
        }
    }

    @Override
    public List<TicketDto> getTicketForUser(User currentUser) {
        return getConvertedTicketItem(
                switch (currentUser.getRole()) {
                    case ADMIN -> ticketRepository.findAll();
                    case SUPPORT_STAFF -> ticketRepository.findByAssignedTo(currentUser);
                    default -> ticketRepository.findByCreatedBy(currentUser);
                }
        );
    }

    @Override
    public TicketDto assignTicket(Long ticketId, Long staffId) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));;

        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new IllegalStateException("Staff not found"));

        if (staff.getRole()  != Role.SUPPORT_STAFF) {
            throw new IllegalStateException("Cannot assign ticket to non-staff user");
        }

        ticket.setAssignedTo(staff);
        ticket.setStatus(Status.IN_PROGRESS);

        return convertToDto(ticketRepository.save(ticket));
    }

    @Override
    public TicketDto updatePatchPriority(Long id, Ticket ticket) {
        Ticket ticketUpdated = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));;

        ticketUpdated.setPriority(ticket.getPriority());

        return convertToDto(ticketRepository.save(ticketUpdated));

    }

    @Override
    public void deleteTicket(Long id) {
        ticketRepository.findById(id)
            .ifPresentOrElse(ticketRepository::delete, () -> {
                throw new NotFoundException("Ticket not found");
       });
    }

    @Override
    public List<TicketDto> getConvertedTicketItem(List<Ticket> tickets) {
        return tickets.stream().map(this::convertToDto).toList();
    }

    @Override
    public TicketDto convertToDto(Ticket ticket) {
        TicketDto ticketDto = new TicketDto();
        ticketDto.setId(ticket.getId());
        ticketDto.setTitle(ticket.getTitle());
        ticketDto.setDescription(ticket.getDescription());
        ticketDto.setCategory(ticket.getCategory());
        ticketDto.setPriority(ticket.getPriority());
        ticketDto.setStatus(ticket.getStatus());
        ticketDto.setCreatedBy(mapToUserDto(ticket.getCreatedBy()));
        ticketDto.setAssignedTo(mapToUserDto(ticket.getAssignedTo()));
        ticketDto.setCreatedAt(ticket.getCreatedAt());
        ticketDto.setUpdatedAt(ticket.getUpdatedAt());
        return  ticketDto;

    }

    private UserDto mapToUserDto(User user) {
        if (user == null) return null;
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFullname(user.getFullname());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}
