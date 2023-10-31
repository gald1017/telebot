package flinkbot;

import org.apache.flink.shaded.curator5.com.google.common.base.Objects;

public class ConcatenatedMessages {
    public int message_count;
    public String concatenated_messages;

    public long date;
    public long chat_id;

    public ConcatenatedMessages() {
    }

    public ConcatenatedMessages(String concatenated_messages, long date, long chat_id, int message_count) {
        this.concatenated_messages = concatenated_messages;
        this.date = date;
        this.chat_id = chat_id;
        this.message_count = message_count;
    }

    public ConcatenatedMessages(NewsSummarization newsSummarization, int message_count) {
        this.concatenated_messages = newsSummarization.text;
        this.date = newsSummarization.date;
        this.chat_id = newsSummarization.chat_id;
        this.message_count = message_count;
    }

    public String getConcatenated_messages() {
        return concatenated_messages;
    }

    public long getDate() {
        return date;
    }

    public long getChat_id() {
        return chat_id;
    }

    public int getMessage_count() {
        return message_count;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConcatenatedMessages that = (ConcatenatedMessages) o;

        return java.util.Objects.equals(concatenated_messages, that.concatenated_messages) &&
                Objects.equal(date, that.date) &&
                Objects.equal(chat_id, that.chat_id) &&
                Objects.equal(message_count, that.message_count);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(concatenated_messages, date, chat_id, message_count);
    }
}
