/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hello;

import javax.microedition.lcdui.StringItem;

/**
 *
 * @author Matthias Clausen
 */
public class ShoppingItem extends StringItem {
    private int id;
    private boolean done;
    private String author;

    public ShoppingItem(int ID, String text, int Done, String Author) {
        /* "zero width non breaking space" as bold text,
            since empty bold text messes with the element wrapping :( */
        super("\ufeff", text);
        id = ID;
        done = Done==1;
        author = Author;
    }
    public ShoppingItem(String bold, String normal) {
        super(bold, normal);
    }
    public ShoppingItem(String normal) {
        super("", normal);
    }
    public ShoppingItem () {
        super("", "");
    }
    public int getId() {
        return id;
    }
}
