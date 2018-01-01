package chv.has.model;

import chv.has.model.interfaces.MessageInterface;

/**
 * @author Christopher Anciaux
 */
public class Message implements MessageInterface {
    private int id;

    private String content;

    private String color;

    private boolean bold;

    public Message(int id, String content, String color, boolean bold) {
        this.id = id;
        this.content = content;
        this.color = color;
        this.bold = bold;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isBold() {
        return this.bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }
}
