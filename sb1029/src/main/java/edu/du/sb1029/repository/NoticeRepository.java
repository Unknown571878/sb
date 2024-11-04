package edu.du.sb1029.repository;

import edu.du.sb1029.entity.Notice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    public List<Notice> findAllByOrderByNoticeIdDesc(Pageable pageable);


}
