package chv.has.model;

import chv.has.model.interfaces.MessageInterface;

import java.util.Date;

/**
 * @author Christopher Anciaux
 */
public class Message implements MessageInterface {
    protected int id;

    protected String content;

    protected Date endDate;

    protected Integer repetitionFrequency;

    public Message(int id, String content, Date endDate, int repetitionFrequency) {
        this.id = id;
        this.content = content;
        this.endDate = endDate;
        this.repetitionFrequency = repetitionFrequency;
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

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return True is the message shouldn't be shown, false otherwise
     */
    public boolean isEnded() {
        return null == this.getEndDate() || 0 >= this.getEndDate().compareTo(new Date());
    }

    public Integer getRepetitionFrequency() {
        return this.repetitionFrequency;
    }

    public void setRepetitionFrequency(int repetitionFrequency) {
        this.repetitionFrequency = repetitionFrequency;
    }
}
