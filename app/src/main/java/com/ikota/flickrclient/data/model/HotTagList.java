package com.ikota.flickrclient.data.model;


import java.util.List;

public class HotTagList {
    public HotTags hottags;
    public String stat;

    public static class HotTags {
        public String period;
        public int count;
        public List<Tag> tag;
    }

    public static class Tag {
        public int score;
        public String _content;
    }

}
