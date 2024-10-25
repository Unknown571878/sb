package edu.du.sb1024.spring;

import lombok.Data;

@Data
public class RegisterRequest {

	private String email;
	private String password;
	private String confirmPassword;
	private String name;
	private String role;




	public boolean isPasswordEqualToConfirmPassword() {
		return password.equals(confirmPassword);
	}
}
