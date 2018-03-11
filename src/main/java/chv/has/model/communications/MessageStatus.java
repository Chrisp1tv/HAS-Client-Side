package chv.has.model.communications;

/**
 * @author Christopher Anciaux
 */
public class MessageStatus {
    protected int recipientId;

    protected int campaignId;

    protected String status;

    public MessageStatus(int recipientId, int campaignId, String status) {
        this.recipientId = recipientId;
        this.campaignId = campaignId;
        this.status = status;
    }

    public int getRecipientId() {
        return this.recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public int getCampaignId() {
        return this.campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
