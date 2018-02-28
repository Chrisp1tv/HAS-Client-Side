package chv.has.model.interfaces;

import java.util.Date;

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

    Date getEndDate();

    void setEndDate(Date endDate);

    int getRepetitionFrequency();

    void setRepetitionFrequency(int repetitionFrequency);
}
