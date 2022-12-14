package kg.neobis.rentit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.neobis.rentit.dto.BlockedUserDto;
import kg.neobis.rentit.dto.ComplaintDto;
import kg.neobis.rentit.dto.ComplaintRegistrationDto;
import kg.neobis.rentit.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/complaints")
@Validated
@RequiredArgsConstructor
@Tag(name = "Complaint Controller", description = "The Category API with documentation annotations")
public class ComplaintController {

    private final ComplaintService complaintService;


    @PostMapping("/add")
    @Operation(summary = "Добавить жалобу на профиль")
    public ResponseEntity<String> addComplaint(@RequestBody ComplaintRegistrationDto dto) {
        return ResponseEntity.ok(complaintService.addComplaint(dto));
    }

    @DeleteMapping("/remove/{complaintId}")
    @Operation(summary = "Удалить существующую жалобу")
    public ResponseEntity<String> removeComplaint(@PathVariable Long complaintId) {
        return ResponseEntity.ok(complaintService.removeComplaint(complaintId));
    }

    @GetMapping("/get-blocked-users")
    @Operation(summary = "Получить заблокированных пользователей")
    public ResponseEntity<List<BlockedUserDto>> getBlockedUsers() {
        return ResponseEntity.ok(complaintService.getBlockedUsers());
    }

    @GetMapping("/get-sent-from-user/{offset}")
    @Operation(summary = "Получить список жалоб отправленных пользователем")
    public ResponseEntity<List<ComplaintDto>> getComplaintsSentFromUser(@PathVariable int offset) {
        return ResponseEntity.ok(complaintService.getComplaintsSentFromUser(offset));
    }

    @GetMapping("/get-sent-to-user/{offset}")
    @Operation(summary = "Получить список жалоб отправленных на профиль пользователя")
    public ResponseEntity<List<ComplaintDto>> getComplaintsSentToUser(@PathVariable int offset) {
        return ResponseEntity.ok(complaintService.getComplaintsSentToUser(offset));
    }

}
