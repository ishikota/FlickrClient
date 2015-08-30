package com.ikota.flickrclient.data.model;

@SuppressWarnings("unused")
public class PeopleInfo {
    public Person person;
    public String stat;

    public static class Person {
        public String id;
        public String nsid;
        public int ispro;
        public int can_buy_pro;
        public String iconserver;
        public int iconfarm;
        public String path_alias;
        public String has_stats;
        public UserName username;
        public RealName realname;
        public Location location;
        public Description description;
        public ProfileURL profileurl;
        public Photos photos;
    }

    public static class UserName {
        public String _content;
    }

    public static class RealName {
        public String _content;
    }

    public static class Location {
        public String _content;
    }

    public static class Description {
        public String _content;
    }

    public static class Photos {
        public Count count;
        public Views views;
    }

    public static class ProfileURL {
        public String _content;
    }

    public static class Count {
        public int _content;
    }

    public static class Views {
        public String _content;
    }
}
