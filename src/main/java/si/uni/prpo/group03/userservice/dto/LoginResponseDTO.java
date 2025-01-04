package si.uni.prpo.group03.userservice.dto;

public class LoginResponseDTO {

    private final String token;
    private final String username;
    private final String email;

    public LoginResponseDTO(String token, String username, String email) {
        this.token = token;
        this.username = username;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}