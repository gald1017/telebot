package flinkbot;

import org.apache.flink.shaded.curator5.com.google.common.base.Objects;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

public class NewsSummarization {
    public String text;
    public long date;
    public long chat_id;

    public NewsSummarization() {
    }

    public NewsSummarization(String text, long date, long chat_id) {
        this.text = text;
        this.date = date;
        this.chat_id = chat_id;
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
        NewsSummarization that = (NewsSummarization) o;
        return java.util.Objects.equals(text, that.text) &&
                Objects.equal(date, that.date) &&
                Objects.equal(chat_id, that.chat_id);
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(text, date, chat_id);
    }

}
