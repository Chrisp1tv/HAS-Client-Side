package chv.has.model.interfaces;

/**
 * @author Christopher Anciaux
 */
public interface MessageInterface {
    int getId();

    void setId(int id);

    String getContent();

    void setContent(String content);

    String getColor();

    void setColor(String color);

    boolean isBold();

    void setBold(boolean bold);
}
