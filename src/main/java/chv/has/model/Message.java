package chv.has.model;

import chv.has.model.interfaces.MessageInterface;

import java.util.Date;

/**
 * @author Christopher Anciaux
 */
public class Message implements MessageInterface {
    private int id;

    private String content;

    private String color;

    private boolean bold;

    private Date endDate;

    private Integer repetitionFrequency;

    public Message(int id, String content, String color, boolean bold, Date endDate, int repetitionFrequency) {
        this.id = id;
        this.content = content;
        this.color = color;
        this.bold = bold;
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

    public Date getEndDate() {
        return this.endDate;
    }

    public boolean isEnded() {
        return null == this.getEndDate() || 0 >= this.getEndDate().compareTo(new Date());
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getRepetitionFrequency() {
        return this.repetitionFrequency;
    }

    public void setRepetitionFrequency(int repetitionFrequency) {
        this.repetitionFrequency = repetitionFrequency;
    }
}
