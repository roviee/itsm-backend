package com.janr.itsm.controller;

import com.janr.itsm.auth.model.User;
import com.janr.itsm.model.Ticket;
import com.janr.itsm.response.ApiResponse;
import com.janr.itsm.service.ticket.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/admin/get")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllTicket() {
        return okResponse(ticketService.getAllTicket());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'SUPPORT_STAFF', 'ADMIN')")
    public ResponseEntity<ApiResponse> getTicketById(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        return okResponse(ticketService.getTicketById(id, currentUser));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'SUPPORT_STAFF', 'ADMIN')")
    public ResponseEntity<ApiResponse> createTicket(@RequestBody Ticket ticket) {
        return okResponse(ticketService.createTicket(ticket));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'SUPPORT_STAFF', 'ADMIN')")
    public ResponseEntity<ApiResponse> getTickets(@AuthenticationPrincipal User currentUser) {
        return okResponse(ticketService.getTicketForUser(currentUser));
    }

    @PutMapping("/{id}/assign/{staffId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> assignTicket(@PathVariable Long id, @PathVariable Long staffId) {
        return okResponse(ticketService.assignTicket(id, staffId));
    }

    @PatchMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ApiResponse> updatePatchPriority (@PathVariable Long id, @RequestBody Ticket ticket) {
        return okResponse(ticketService.updatePatchPriority(id, ticket));
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
