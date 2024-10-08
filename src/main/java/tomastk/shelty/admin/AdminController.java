package tomastk.shelty.admin;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tomastk.shelty.user.Role;
import tomastk.shelty.user.User;

import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor

public class AdminController {
    private AdminService adminService;

    @PostMapping("/create")
    public ResponseEntity<AdminResponse> createAdmin(@RequestBody AdminRequest createDetails) {
        return ResponseEntity.ok(adminService.createAdmin(createDetails));
    }
    @PutMapping("/update")
    public ResponseEntity<AdminResponse> updateAdmin(@RequestBody User adminToUpdate) {
        return ResponseEntity.ok(adminService.updateAdmin(adminToUpdate));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable int id) {
        return ResponseEntity.ok(adminService.deleteAdmin(id));
    }

    @GetMapping("/users/all")
    public ResponseEntity<List> getUsers(){
        return ResponseEntity.ok(adminService.getUsers());
    }

    @GetMapping("/users")
    public ResponseEntity<List> getUsersByRole(@RequestParam Role role) {
        return ResponseEntity.ok(adminService.getUsersByRole(role));
    }

}
