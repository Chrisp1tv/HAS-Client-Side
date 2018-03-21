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

    Date getEndDate();

    void setEndDate(Date endDate);

    boolean isEnded();

    Integer getRepetitionFrequency();

    void setRepetitionFrequency(int repetitionFrequency);
}
