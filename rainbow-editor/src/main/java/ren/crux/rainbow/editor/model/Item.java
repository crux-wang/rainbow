package ren.crux.rainbow.editor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ren.crux.rainbow.core.model.CommentText;
import ren.crux.rainbow.core.model.RequestMethod;

/**
 * Item
 *
 * @author wangzhihui
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    private String name;
    private String[] path;
    private RequestMethod method;
    private CommentText commentText;


    public Item(String name, CommentText commentText) {
        this.name = name;
        this.commentText = commentText;
    }

    public Item(String name, String[] path, CommentText commentText) {
        this.name = name;
        this.path = path;
        this.commentText = commentText;
    }
}
