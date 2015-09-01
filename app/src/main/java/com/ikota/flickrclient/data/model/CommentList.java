package com.ikota.flickrclient.data.model;


import java.util.List;

@SuppressWarnings("unused")
public class CommentList {
    public Comments comments;
    public String stat;

    public static class Comments {
        public String photo_id;
        public List<Comment> comment;
    }

    public static class Comment {
        public String id;
        public String author;
        public String authorname;
        public String iconserver;
        public int iconfarm;
        public String datecreate;
        public String permalink;
        public String path_alias;
        public String realname;
        public String _content;
    }
}
