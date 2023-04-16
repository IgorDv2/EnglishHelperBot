import java.io.IOException;

public class BotLogic {
    public void setMenu(String menu) {
        this.menu = menu;
    }
    public String getMenu() {
        return menu;
    }
    private String menu = "main";

    protected static EnglishHelperBot sendbot;                                          //Экземпляр бота для отправки ботом сообщений

    static {
        try {
            sendbot = new EnglishHelperBot();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private Phrasal case1 = new Phrasal("C:\\Users\\Odd\\IdeaProjects\\EnglishHelperBot\\PhrasalV.txt");                                //РАЗОБРАТЬСЯ, стоит ли делать final объекты и как это сделать
    private InfinitiveGerund case2 = new InfinitiveGerund("C:\\Users\\Odd\\IdeaProjects\\EnglishHelperBot\\InfinitiveOrGerund.txt");

    private Articles case3 = new Articles("C:\\Users\\Odd\\IdeaProjects\\EnglishHelperBot\\Articles.txt");

    public int doTheThing(String messege) throws IOException {
        switch(getMenu()){
            case "main":
                mainMenu(messege);
                break;
            case "testing type menu":
                testingTypeMenu(messege);
                break;
            case "question number":
                questionNumberSet(messege);
                break;
            case "Phrasal":
                PhrasalTestIteration(messege);
                break;
            case "InfiniteGerund":
                InfiniteGerundTestIteration(messege);
                break;
            case "Articles":
                ArticleTestIteration(messege);
                break;
        }
        return 0;
    }
    private int mainMenu(String menuCommands){
        switch (menuCommands) {
            case "/help":
                sendbot.sendMessage("Привет, я English Helper! Я провожу различные тесты на знание английского.\n"+
                        "Для проведения теста введите /test");
                break;
            case "/test":                                                         //запускает тест
                sendbot.sendMessage("Для проведения теста по Phrasal verbs, Prepositional phrases, Word patterns Введите '1'\n"+
                        "Для проведения теста - Инфинитив или Герундий Введите '2'\n"+
                        "Для проведения теста - на знание артиклей Введите '3'\n"+
                        "Для завершения теста введите /finish\n"+
                        "Чтобы задать количество вопросов в тесте введите /numberQ");
                setMenu("testing type menu");                                                         //При запуске теста происходит считывание других комманд
                EnglishHelperBot.setMenu(1);
                break;
            case "/finish":
                break;
            default:
                sendbot.sendMessage("Я не понимаю :(");
        }
        return 0;
    }

    private int testingTypeMenu(String menuCommands) throws IOException {
        switch (menuCommands) {
            case "1":
                setMenu("Phrasal");                                 //При запуске теста
                if (Phrasal.getIsArrayDone() == false) {     //если файл еще не прочтен
                    case1.setTestingType("Phrasal");
                    case1.FileToArray();                    //Создается список вопросов и ответов на основе текстового файла
                    Phrasal.setIsArrayDone(true);
                }
                case1.Randomize();                          //создается случайный порядок вопросов
                case1.StartTest();                          //Отображается шапка с текстом для начала теста
                case1.showQuestion();                       //Отображается первый вопрос
                break;
            case "2":
                setMenu("InfiniteGerund");                                     //При запуске теста
                if (InfinitiveGerund.getIsArrayDone() == false) {
                    case2.setTestingType("InfinitiveGerund");
                    case2.FileToArray();                        //Создается список вопросов и ответов на основе текстового файла
                    InfinitiveGerund.setIsArrayDone(true);
                }
                case2.Randomize();                          //создается случайный порядок вопросов
                case2.StartTest();                          //Отображается шапка с текстом для начала теста
                case2.showQuestion();                       //Отображается первый вопрос
                break;
            case "3":
                setMenu("Articles");                                     //При запуске теста
                if (Articles.getIsArrayDone() == false) {
                    case3.setTestingType("Articles");
                    case3.FileToArray();                        //Создается список вопросов и ответов на основе текстового файла
                    Articles.setIsArrayDone(true);
                }
                case3.Randomize();                          //создается случайный порядок вопросов
                case3.StartTest();                          //Отображается шапка с текстом для начала теста
                case3.showQuestion();                       //Отображается первый вопрос
                break;
            case "/finish":
                setMenu("main");
                break;
            case "/help":
                sendbot.sendMessage("Для проведения теста по Phrasal verbs, Prepositional phrases, Word patterns Введите '1'\n" +
                        "Для проведения теста - Инфинитив или Герундий Введите '2'\n" +
                        "Для завершения теста введите /finish\n" +
                        "Чтобы задать количество вопросов в тесте введите /numberQ");
                break;
            case "/numberQ":                                //команда, запускающая подменю для считывания желаемого количества вопросов в тесте
                sendbot.sendMessage("Введите количество вопросов в тесте от 20 до 200,\n");
                setMenu("question number");
                break;
            default:
                sendbot.sendMessage("Я не понимаю :(");
        }
        return 0;
    }
    private int questionNumberSet(String menuCommands){
        if(Integer.parseInt(menuCommands)>=20 && Integer.parseInt(menuCommands)<=200){        //считывает фиксированное количество вопросов в тесте (по умолчанию 50)
            CoreTesting.setFixedNumberOfQuestions(Integer.parseInt(menuCommands));
            setMenu("testing type menu");                                                                     //возвращает в меню выбора теста
        }
        else
            sendbot.sendMessage("Невозможное количество вопросов");
        return 0;
    }

    private int PhrasalTestIteration(String answer){
        if(CoreTesting.getFixedNumberOfQuestions()>case1.QuestionNumber)
            CoreTesting.setFixedNumberOfQuestions(case1.QuestionNumber);
        if(EnglishHelperBot.updateBuffer.hasMessage()) {                         //При каждом апдейте с запущенным тестом
            Phrasal.setBotPhraseAnswer(answer);
            case1.PhrasalTesting();                             //Метод тестирования совершает одну итерацию
        }

        return 0;
    }

    private int InfiniteGerundTestIteration(String answer){
        if(CoreTesting.getFixedNumberOfQuestions()>case2.QuestionNumber)            //если заданное количество вопросов превышает максимальное, установить максимум вопросов из файла
            CoreTesting.setFixedNumberOfQuestions(case2.QuestionNumber);
        if(EnglishHelperBot.updateBuffer.hasMessage()) {                         //При каждом апдейте с запущенным тестом
            InfinitiveGerund.setBotInfAnswer(answer);
            case2.InfGerundTesting();                           //Метод тестирования совершает одну итерацию
        }

        return 0;
    }

    private int ArticleTestIteration(String answer){
        if(CoreTesting.getFixedNumberOfQuestions()>case3.QuestionNumber)
            CoreTesting.setFixedNumberOfQuestions(case3.QuestionNumber);
        if(EnglishHelperBot.updateBuffer.hasMessage()) {                         //При каждом апдейте с запущенным тестом
            Articles.setBotArtAnswer(answer);
            case3.ArticlesTesting();                             //Метод тестирования совершает одну итерацию
        }

        return 0;
    }
}
