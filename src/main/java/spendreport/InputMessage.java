package spendreport;

import org.apache.flink.shaded.curator5.com.google.common.base.Objects;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
//@JsonIgnoreProperties(ignoreUnknown = true)
public class InputMessage {
//    public String message;
    @JsonProperty("text")
    public String text;
//    @JsonDeserialize(using = LocalDateDeserializer.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssXXX")
    @JsonProperty("date")
    public long date;
    @JsonProperty("chat_id")
    public long chat_id;

    @JsonProperty("sender")
    public long sender;
    @JsonProperty("is_hebrew")
    public Boolean is_hebrew;

    public InputMessage() {
    }

    public InputMessage(String text, int date, Integer chat_id, Integer sender, Boolean is_hebrew) {
        this.text = text;
        this.date = date;
        this.chat_id = chat_id;
        this.sender = sender;
        this.is_hebrew = is_hebrew;
    }

    public String getText() {
        return text;
    }

    public long getDate() {
        return date;
    }
    public long getChat_id() {
        return chat_id;
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
                Objects.equal(chat_id, that.chat_id) &&
                Objects.equal(sender, that.sender) &&
                Objects.equal(is_hebrew, that.is_hebrew);
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(text, date, chat_id, sender, is_hebrew);
    }

}
