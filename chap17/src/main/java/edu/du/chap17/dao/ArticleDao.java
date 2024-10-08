package edu.du.chap17.dao;

import edu.du.chap17.model.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleDao {

    @Select("select count(*) from article")
    public int selectCount();

    @Select("select article_id id, group_id groupId, sequence_no sequenceNumber, posting_date postingDate, read_count readCount, writer_name writerName, password, title from article order by sequence_no desc limit #{firstRow}, #{endRow}")
    public List<Article> select(int firstRow, int endRow);

    @Insert("insert into article "
            + "(group_id groupId, sequence_no sequenceNumber, posting_date postingDate, read_count readCount, writer_name writerName, password, title, content) "
            + "values (#{groupId}, #{sequenceNumber}, #{postingDate}, 0, #{writerName}, #{password}, #{title}, #{content})")
    public int insert(Article article);

    @Select("select article_id id, group_id groupId, sequence_no sequenceNumber, posting_date postingDate, read_count readCount, writer_name writerName, password, title, content from article where article_id = #{articleId}")
    public Article selectById(int articleId);

    @Update("update article "
            + "set read_count = read_count + 1 "
            + "where article_id = #{articleId}")
    public void increaseReadCount(int articleId);

    @Select("select min(sequence_no) from article "
            + "where sequence_no < ? and sequence_no >= ?")
    public String selectLastSequenceNumber(String searchMaxSeqNum, String searchMinSeqNum);

    @Update("update article "
            + "set title = ?, content = ? where article_id = ?")
    public int update(Article article);

    @Delete("delete from article "
            + "where article_id = ?")
    public void delete(int articleId);
}
