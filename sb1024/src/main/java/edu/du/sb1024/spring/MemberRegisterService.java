package edu.du.sb1024.spring;

import edu.du.sb1024.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MemberRegisterService {

	@Autowired
	private MemberDao memberDao;

	private PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public Long regist(RegisterRequest req) {
		Optional<Member> member = memberDao.selectByEmail(req.getEmail());
		if (member.isPresent()) {
			throw new DuplicateMemberException("dup email " + req.getEmail());
		}

		Member newMember = Member.builder()
				.email(req.getEmail())
				.password(passwordEncoder().encode(req.getPassword()))
				.username(req.getName())
				.regdate(LocalDateTime.now())
				.role("USER")
				.build();
		memberDao.insert(newMember);
		System.out.println("====>" + newMember);
		return newMember.getId();
	}
}
