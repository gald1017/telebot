package flinkbot;

public class isArabic extends isHebrew{
    @Override
    public boolean filter(InputMessage inputMessage) throws Exception {
        return !inputMessage.is_hebrew;
    }
}
