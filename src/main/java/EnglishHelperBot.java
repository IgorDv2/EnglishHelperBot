import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;

public  class EnglishHelperBot extends TelegramLongPollingBot {

    public static final String BOT_TOKEN = "5903766429:AAGNBtj3oXkL2-d4lwIYkVt2kJY9aeicR_A";

    public static final String BOT_USERNAME = "English_HelperJ_bot";

    public static void setMenu(int menu) {
        EnglishHelperBot.menu = menu;
    }

    public static int getMenu() {
        return menu;
    }

    public static int menu = 0;                                        //Переменная для управлением меню выбора

    private static long chat_id;
    private static Update updateBuffer;                                 //Буфер для апдейтов чата
    private static String answerToBot;                                  //Буфер для строки ввода пользователя
    private Phrasal case1 = new Phrasal("C:\\Users\\Odd\\IdeaProjects\\EnglishHelperJ\\EnglishHelperJ\\PhrasalV.txt");
    private InfinitiveGerund case2 = new InfinitiveGerund("C:\\Users\\Odd\\IdeaProjects\\EnglishHelperJ\\EnglishHelperJ\\InfinitiveOrGerund.txt");

    public EnglishHelperBot() throws IOException {
    }

    public void Start() throws TelegramApiException{                                //Подключение к боту
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
    public void onUpdateReceived(Update update) {                                   //метод, постоянно запрашивающий апдейты из чата
        updateBuffer = update;                                                      //Копия апдейта в буфере, чтобы можно было им воспользоваться в других методах
        if (updateBuffer.hasMessage()) {
            chat_id = updateBuffer.getMessage().getChatId();
            answerToBot = updateBuffer.getMessage().getText();

            try {
                BotLogic();                                                          //Метод с командами чату и логикой для тестов
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void BotLogic() throws IOException {
        if(getMenu() == 0) {
            switch (answerToBot) {
                case "/help":
                    sendMessage("Привет, я English Helper! Я провожу различные тесты на знание английского.\n"+
                            "Для проведения теста введите /test");
                    break;
                case "/test":
                    StartTesting();                                                   //Метод для дублирования шапки текста в консоль
                    sendMessage("Для проведения теста по Phrasal verbs, Prepositional phrases, Word patterns Введите '1'\n"+
                            "Для проведения теста - Инфинитив или Герундий Введите '2'\n"+
                            "Для завершения теста введите /finish\n");
                    setMenu(1);                                                         //При запуске теста происходит считывание других комманд
                    break;
                case "/finish":
                    break;
                default:
                    sendMessage("Я не понимаю :(");
            }
        } else if(getMenu() == 1){
            switch (answerToBot){
                case "1":
                    setMenu(2);                         //При запуске теста
                    case1.FileToArray();              //Создается массив рандомизированных вопросов на основе текстового файла
                    case1.StartTest();                //Отображается шапка с текстом для начала теста
                    case1.showQuestion();             //Отображается первый вопрос
                    break;
                case "2":
                    setMenu(3);                         //При запуске теста
                    case2.FileToArray();              //Создается массив рандомизированных вопросов на основе текстового файла
                    case2.StartTest();                //Отображается шапка с текстом для начала теста
                    case2.showQuestion();             //Отображается первый вопрос
                    break;
                case "/finish":
                    setMenu(0);
                    break;
                case "/help":
                    sendMessage("Для проведения теста по Phrasal verbs, Prepositional phrases, Word patterns Введите '1'\n"+
                            "Для проведения теста - Инфинитив или Герундий Введите '2'\n"+
                            "Для завершения теста введите /finish\n");
                default:
                    sendMessage("Я не понимаю :(");
            }
        } else if(getMenu() == 3){
            if(updateBuffer.hasMessage()) {                         //При каждом апдейте с запущенным тестом
                InfinitiveGerund.setBotInfAnswer(answerToBot);
                case2.InfGerundTesting();                           //Метод тестирования совершает одну итерацию
            }
        } else if(getMenu() == 2){
            if(updateBuffer.hasMessage()) {                         //При каждом апдейте с запущенным тестом
                Phrasal.setBotPhraseAnswer(answerToBot);
                case1.PhrasalTesting();                           //Метод тестирования совершает одну итерацию
            }
        }
    }

    public void sendMessage(String messageText) {                   //Общий метод для отображения сообещий ботом
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText(messageText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    void StartTesting(){
        System.out.println("Для проведения теста по Phrasal verbs, Prepositional phrases, Word patterns \nВведите '1'\n");
        System.out.println("Для проведения теста - Инфинитив или Герундий\nВведите '2'");
        System.out.println("Для завершения теста введите finish");
        System.out.println("Для пропуска вопроса введите next");
    }
}
