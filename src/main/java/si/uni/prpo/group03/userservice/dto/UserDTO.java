package si.uni.prpo.group03.userservice.dto;

public class UserDTO {

    private final String username;
    private final String email;
    private final String firstName;
    private final String lastName;

    public UserDTO(String username, String email, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
