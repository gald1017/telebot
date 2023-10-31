package flinkbot;

import org.apache.flink.shaded.curator5.com.google.common.base.Objects;

public class MessageObject {
    public String text;
    public long date;
    public long chat_id;
    public boolean is_hebrew;

    public int message_count;

    public MessageObject() {
    }

    public MessageObject(String text, long date, long chat_id, boolean is_hebrew) {
        this.text = text;
        this.date = date;
        this.chat_id = chat_id;
        this.is_hebrew = is_hebrew;
        this.message_count = 1;
    }
    public MessageObject(String text, long date, long chat_id, int message_count, boolean is_hebrew) {
        this.text = text;
        this.date = date;
        this.chat_id = chat_id;
        this.is_hebrew = is_hebrew;
        this.message_count = message_count;
    }

    public boolean getIs_hebrew() {
        return is_hebrew;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MessageObject that = (MessageObject) o;
        return java.util.Objects.equals(text, that.text) &&
                Objects.equal(date, that.date) &&
                Objects.equal(chat_id, that.chat_id);
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(text, date, chat_id);
    }

}
