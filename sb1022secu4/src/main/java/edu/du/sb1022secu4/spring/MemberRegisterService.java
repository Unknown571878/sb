package edu.du.sb1022secu4.spring;

import edu.du.sb1022secu4.entity.Member;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class MemberRegisterService {
	private MemberDao memberDao;

	private PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder();}

	public MemberRegisterService(MemberDao memberDao) {
		this.memberDao = memberDao;
	}

	public Long regist(RegisterRequest req) {
		Member member = memberDao.selectByEmail(req.getEmail());
		if (member != null) {
			throw new DuplicateMemberException("dup email " + req.getEmail());
		}
		Member newMember = new Member(
				req.getEmail(), passwordEncoder().encode(req.getPassword()), req.getName(),
				LocalDateTime.now());
		memberDao.insert(newMember);
		return newMember.getId();
	}
}
