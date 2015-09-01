package com.ikota.flickrclient.data.model;

import com.google.gson.Gson;
import com.ikota.flickrclient.data.DataHolder;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;


@RunWith(RobolectricTestRunner.class)
public class CommentListTest extends TestCase{

    @Test
    public void readItem() throws Exception{
        Gson gson = new Gson();
        CommentList item = gson.fromJson(DataHolder.COMMENT_LIST_JSON, CommentList.class);
        assertEquals("20623135501", item.comments.photo_id);
        assertEquals("ok", item.stat);
        CommentList.Comment comment = item.comments.comment.get(0);
        assertEquals("30134429-20623135501-72157657254905896", comment.id);
        assertEquals("43287538@N00", comment.author);
        assertEquals("¡! Nature B■x !¡", comment.authorname);
        assertEquals("2918", comment.iconserver);
        assertEquals(3, comment.iconfarm);
        assertEquals("1439709894", comment.datecreate);
        assertEquals("https://www.flickr.com/photos/cannon_s5_is/20623135501/#comment72157657254905896", comment.permalink);
        assertEquals("", comment.path_alias);
        assertEquals("", comment.realname);
        assertEquals("C'est magnifique !", comment._content);
    }

}
