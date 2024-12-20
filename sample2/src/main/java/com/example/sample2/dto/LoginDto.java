package com.example.sample2.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
public class LoginDto {
	
	@Min(100000)
	@Max(2147483646)
	private Integer id;
	@Size(min = 6, max = 20, message = "패스워드는 6~20자 사이여야합니다.")
	private String password;
	private String rememberId;
	
}
