package com.cartravel.booking.dto.user;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
public class UserRequestDTO {
    @NotBlank(message = "Name is required") private String name;
    @NotBlank(message = "Phone is required") @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone must be valid") private String phone;
    public UserRequestDTO() {}
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; } public void setPhone(String phone) { this.phone = phone; }
}