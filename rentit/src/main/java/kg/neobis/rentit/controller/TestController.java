package kg.neobis.rentit.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping(value = "/all", produces = "application/json")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping(value = "/user", produces = "application/json")
    @PreAuthorize("hasAuthority('UPDATE_USER')")
    public String patientAccess() {
        return "User Content.";
    }

    @GetMapping(value = "/doctor", produces = "application/json")
    @PreAuthorize("hasAuthority('CREATE_USER')")
    public String doctorAccess() {
        return "Tech Support Board.";
    }

    @GetMapping(value = "/admin", produces = "application/json")
    @PreAuthorize("hasAuthority('DELETE_USER')")
    public String adminAccess() {
        return "Admin Board.";
    }
}
