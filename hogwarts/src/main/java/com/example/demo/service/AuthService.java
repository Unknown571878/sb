package com.example.demo.service;

import com.example.demo.entity.Users;
import com.example.demo.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {

	private final UsersRepository usersRepository;

	public AuthInfo authenticate(String id, String password) {
		Users member = usersRepository.findById(id);
		if (member == null) {
			throw new WrongIdPasswordException();
		}
		if (!member.matchPassword(password)) {
			throw new WrongIdPasswordException();
		}
		return new AuthInfo(member.getUId(),
				member.getId(),
				member.getName(),
				member.getJob());
	}

}
