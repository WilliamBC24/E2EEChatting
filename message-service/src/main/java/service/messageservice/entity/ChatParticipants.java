package service.messageservice.entity;

public record ChatParticipants(String sender, String receiver) {
    public String getOtherParticipant(String sender) {
        return sender.equals(this.sender) ? this.receiver : this.sender;
    }
}
