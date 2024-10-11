package edu.du.myboard.dto;

import lombok.Data;

@Data
public class CommentDto {
    private int commentIdx;

    private int boardIdx;

    private String contents;

    private String c_contents;

    private String creatorId;

    private String createdDatetime;

    private String updaterId;

    private String updatedDatetime;
}
