package com.example.demo.service;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginCommand {

	private String id;
	private String password;
	private boolean rememberEmail;

}
