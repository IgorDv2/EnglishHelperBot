import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.Scanner;

public  class EnglishHelperBot extends TelegramLongPollingBot {

    public static final String BOT_TOKEN = "5968444384:AAHhKT990kcYwv6W7HLJ3uqhXZalO0TzGY8";

    public static final String BOT_USERNAME = "NASA_picture_test_bot";

    public static final String URI = "https://api.nasa.gov/planetary/apod?api_key=qu3RbJ7SHYNBbsCegaiKEYWJ7hJleu4xaEKRDmT7";

    public static long chat_id;
    Update updateCash;

    public EnglishHelperBot()  {
    }

    public void Start() throws TelegramApiException{
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(this);
    }
    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            chat_id = update.getMessage().getChatId();
            BotLogic();
        }
    }

    public void BotLogic(){
        switch (updateCash.getMessage().getText()) {
            case "/help":
                sendMessage("Привет, я English Helper! Я провожу различные тесты на знание английского");
                break;
            case "/give":
                    
                break;
            default:
                sendMessage("Я не понимаю :(");
        }
    }

    private void sendMessage(String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText(messageText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    static int StartTesting() throws IOException {
        int testingType;
        String pathBuff;
        Scanner read = new Scanner(System.in);
        System.out.println("Для проведения теста по Phrasal verbs, Prepositional phrases, Word patterns \nВведите '1'\n");
        System.out.println("Для проведения теста - Инфинитив или Герундий\nВведите '2'");
        System.out.println("Для завершения теста введите finish");
        System.out.println("Для пропуска вопроса введите next");
        testingType = read.nextInt();


        switch(testingType){ //Вызывается метод для определения типа теста
            case 1:
                pathBuff = "C:\\Users\\Odd\\IdeaProjects\\EnglishHelperJ\\EnglishHelperJ\\PhrasalV.txt";
                Phrasal case1 = new Phrasal(pathBuff);
                case1.Start();
                break;

            case 2:
                pathBuff = "C:\\Users\\Odd\\IdeaProjects\\EnglishHelperJ\\EnglishHelperJ\\InfinitiveOrGerund.txt";
                InfinitiveGerund case2 = new InfinitiveGerund(pathBuff);
                case2.Start();
                break;
        }
        return 0;
    }
}
