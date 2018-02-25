package chv.has.model.communications;

/**
 * @author Christopher Anciaux
 */
public class MessageStatus {
    private int recipientId;

    private int campaignId;

    private String status;

    public MessageStatus(int recipientId, int campaignId, String status) {
        this.recipientId = recipientId;
        this.campaignId = campaignId;
        this.status = status;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
