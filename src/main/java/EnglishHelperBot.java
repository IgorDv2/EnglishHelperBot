import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.Scanner;

public  class EnglishHelperBot extends TelegramLongPollingBot {

    public static final String BOT_TOKEN = "5903766429:AAGNBtj3oXkL2-d4lwIYkVt2kJY9aeicR_A";

    public static final String BOT_USERNAME = "English_HelperJ_bot";

    public static long chat_id;
    static Update update2;
    Update updateCash;
    static String botAnswer;
    static int menu = 0;
    InfinitiveGerund case2 = new InfinitiveGerund("C:\\Users\\Odd\\IdeaProjects\\EnglishHelperJ\\EnglishHelperJ\\InfinitiveOrGerund.txt");
    Phrasal case1 = new Phrasal("C:\\Users\\Odd\\IdeaProjects\\EnglishHelperJ\\EnglishHelperJ\\PhrasalV.txt");
    public EnglishHelperBot() throws IOException {
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
        update2 = update;
        if (update2.hasMessage()) {
            chat_id = update2.getMessage().getChatId();
            botAnswer = update2.getMessage().getText();

            try {
                BotLogic();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void BotLogic() throws IOException {
        if(menu == 0) {
            switch (botAnswer) {
                case "/help":
                    sendMessage("Привет, я English Helper! Я провожу различные тесты на знание английского.");
                    break;
                case "/s":
                    StartTesting();
                    sendMessage("Для проведения теста по Phrasal verbs, Prepositional phrases, Word patterns Введите '1'\n"+
                            "Для проведения теста - Инфинитив или Герундий Введите '2'\n"+
                            "Для завершения теста введите finish\n"+
                            "Для пропуска вопроса введите next");
                    menu = 1;
                    break;
                default:
                    sendMessage("Я не понимаю :(");
            }
        } else if(menu == 1){
            switch (botAnswer){
                case "1":
                    case1.Start();
                    break;
                case "2":
                    menu = 3;
                    case2.FileToArray();
                    case2.StartTest();
                    case2.showQuestion();
                    break;
            }
        } else if(menu == 3){
            //case2.showQuestion();
            if(update2.hasMessage()) {
                InfinitiveGerund.botInfAnswer = botAnswer;
                //sendMessage(case2.botInfAnswer+" ");
                case2.InfGerundTesting();
            }
        }
    }

    public void sendMessage(String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText(messageText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    int StartTesting() throws IOException {
        //int testingType;

        Scanner read = new Scanner(System.in);
        System.out.println("Для проведения теста по Phrasal verbs, Prepositional phrases, Word patterns \nВведите '1'\n");
        System.out.println("Для проведения теста - Инфинитив или Герундий\nВведите '2'");
        System.out.println("Для завершения теста введите finish");
        System.out.println("Для пропуска вопроса введите next");
        //testingType = read.nextInt();


        /*switch(botAnswer){ //Вызывается метод для определения типа теста
            case "1":
                case1.Start();
                break;

            case "2":
                case2.Start();
                break;
        }*/
        return 0;
    }
}
