package spendreport;

import org.apache.flink.shaded.curator5.com.google.common.base.Objects;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDate;

@JsonSerialize
public class InputMessage {
//    public String message;
    @JsonProperty("text")
    public String text;
    @JsonProperty("date")
    public LocalDate date;
    @JsonProperty("chat_id")
    public long chatId;
    @JsonProperty("sender")
    public long sender;
    @JsonProperty("is_hebrew")
    public Boolean is_hebrew;

    public InputMessage() {
    }

    public InputMessage(String text, LocalDate date, Integer chat_id, Integer sender, Boolean is_hebrew) {
        this.text = text;
        this.date = date;
        this.chatId = chat_id;
        this.sender = sender;
        this.is_hebrew = is_hebrew;
    }

    public String getText() {
        return text;
    }

    public LocalDate getDate() {
        return date;
    }
    public long getChatId() {
        return chatId;
    }
    public long getSender() {
        return sender;
    }

    public Boolean getIs_hebrew() {
        return is_hebrew;
    }

    public Object get_input_message() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InputMessage that = (InputMessage) o;
        return java.util.Objects.equals(text, that.text) &&
                Objects.equal(date, that.date) &&
                Objects.equal(chatId, that.chatId) &&
                Objects.equal(sender, that.sender) &&
                Objects.equal(is_hebrew, that.is_hebrew);
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(text, date, chatId, sender, is_hebrew);
    }

}
