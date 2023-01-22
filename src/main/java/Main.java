import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, TelegramApiException {
        EnglishHelperBot bot = new EnglishHelperBot();
        bot.Start();                                                //Подключение к телеграм боту
    }
}