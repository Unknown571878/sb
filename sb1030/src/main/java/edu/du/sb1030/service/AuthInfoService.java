package edu.du.sb1030.service;

import edu.du.sb1030.exception.WrongIdPasswordException;
import edu.du.sb1030.member.AuthInfo;
import edu.du.sb1030.member.Member;
import edu.du.sb1030.repository.AuthInfoRepository;
import edu.du.sb1030.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthInfoService {
    private final AuthInfoRepository authInfoRepository;
    private final MemberRepository memberRepository;

    public AuthInfo authenticate(String email, String password) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new WrongIdPasswordException();
        }
        if(!member.getPassword().equals(password)) {
            throw new WrongIdPasswordException();
        }
        return new AuthInfo(member.getId(),
                member.getEmail(),
                member.getPassword());
    }
}
