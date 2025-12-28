package com.janr.itsm.controller;

import com.janr.itsm.auth.model.User;
import com.janr.itsm.enums.Priority;
import com.janr.itsm.model.Ticket;
import com.janr.itsm.request.TicketUpdateRequest;
import com.janr.itsm.response.ApiResponse;
import com.janr.itsm.response.TicketResponse;
import com.janr.itsm.service.ticket.TicketService;
import com.janr.itsm.utils.AppConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tickets")
@Validated
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/admin/get")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllTicket(
            //Pagination
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false)
            @Min(value = 0, message = "Page number must be 0 or greater")
            Integer pageNumber,

            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false)
            @Min(value = 1, message = "Page size must be at least 1")
            @Max(value = 100, message = "Page size must not exceed 100")
            Integer pageSize,

            //Sorting
            @RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {

        TicketResponse ticketResponse = ticketService.getAllTicket(pageNumber, pageSize, sortBy, sortDir);
        return okResponse(ticketResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'SUPPORT_STAFF', 'ADMIN')")
    public ResponseEntity<ApiResponse> getTicketById(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        return okResponse(ticketService.getTicketById(id, currentUser));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'SUPPORT_STAFF', 'ADMIN')")
    public ResponseEntity<ApiResponse> createTicket(@Valid @RequestBody Ticket ticket) {
        return okResponse(ticketService.createTicket(ticket));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'SUPPORT_STAFF', 'ADMIN')")
    public ResponseEntity<ApiResponse> getTickets(@AuthenticationPrincipal User currentUser) {
        return okResponse(ticketService.getTicketForUser(currentUser));
    }

    @PutMapping("/{id}/assign/{staffId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> assignTicket(@PathVariable Long id, @PathVariable Long staffId) {
        return okResponse(ticketService.assignTicket(id, staffId));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'SUPPORT_STAFF', 'ADMIN')")
    public ResponseEntity<ApiResponse> updateTicket (@PathVariable Long id, @RequestBody TicketUpdateRequest ticket,
                                                     @AuthenticationPrincipal User currentUser) {
        return okResponse(ticketService.updateTicket(id, ticket, currentUser));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteTicket(@PathVariable Long id){
        ticketService.deleteTicket(id);
        return ResponseEntity.ok(new ApiResponse("Delete Ticket!", id));
    }


    private ResponseEntity<ApiResponse> okResponse (Object data) {
        return ResponseEntity.ok(new ApiResponse("success", data));
    }
}
