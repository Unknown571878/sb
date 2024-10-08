package edu.du.chap17;

import edu.du.chap17.dao.ArticleDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Chap17ApplicationTests {

    @Autowired
    ArticleDao articleDao;

    @Test
    void test1(){
        System.out.println(articleDao.selectCount());
    }

}
