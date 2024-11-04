package edu.du.sb1030.repository;

import edu.du.sb1030.member.AuthInfo;
import edu.du.sb1030.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthInfoRepository extends JpaRepository<AuthInfo, Long> {
    AuthInfo findByEmail(String email);
}
