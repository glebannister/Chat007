package android.example.chat007.data;

public class SecretMessage {

    private String name;
    private String text;
    private String messageUrl;
    private String sender;
    private String recipient;

    public SecretMessage(){

    }


    public SecretMessage(String name, String text, String messageUrl, String sender, String recipient) {
        this.name = name;
        this.text = text;
        this.messageUrl = messageUrl;
        this.sender = sender;
        this.recipient = recipient;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMessageUrl() {
        return messageUrl;
    }

    public void setMessageUrl(String messageUrl) {
        this.messageUrl = messageUrl;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
