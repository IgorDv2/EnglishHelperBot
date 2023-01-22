import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;

public class CoreTesting {
    protected char[][] QuestionArr = new char[300][100];                            //массив для хранения строк, содержащих вопросы
    protected char[][] AnswerArr = new char[300][100];                              //массив для хранения правильных ответов
    protected int[] WrongAnswer = new int[300];                                     //массив для хранения номеров вопросов/ответов, где была ошибка
    protected int[] MissedQuestions = new int[300];                                 //массив для номеров вопросов которые были пропущены
    protected int QuestionNumber = 0;                                               //общее число вопросов/ответов
    protected int[] RandomNumberArrPointer;                                         //Указатель на массив со случайными числами
    protected int ErrorsNumber = 0;                                                 //общее количество ошибок

    protected int WrongIndex = 0;                                                             //индекс массива вопросов/ответов c ошибками
    protected int MissedIndex = 0;                                                            //индекс массива пропущенных впросов

    protected int activeQuestionNumber = 0;            //Номер текущего вопроса
    private  boolean TestingEndFlag = false;
    protected static EnglishHelperBot sendbot;                //Экземпляр бота для отправки ботом сообщений

    static {
        try {
            sendbot = new EnglishHelperBot();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setTestingType(int testingType) {
        TestingType = testingType;
    }

    protected static int TestingType = 0;                                    //хранит число, соответствующее типу тестирования
    protected int typeCommand = 1;                                        //управляющая переменная
    protected String DataFilePath;                                          //Строка для хранения пути к текстовому файлу с вопросами


    CoreTesting(String path) {                           //Конструктор принимает строку с путем к файлу
        DataFilePath = path;
    }

    public int FileToArray() throws IOException {

        int answerIndexChar;                                    //Индекс
        int characterNumber;                                     //Буфер для длинны строки
        String bufferString;
        String QuestionBuff;                                    //Буфер строки вопроса
        String AnswerBuff;                                      //Буфер строки ответа

        File DataFile = new File(DataFilePath);
        FileReader fr = new FileReader(DataFile);
        BufferedReader reader = new BufferedReader(fr);

        bufferString = reader.readLine();                                                               //Считывается первая строка из файла

        while (bufferString != null) {                                                                     //пока не закончатся строки в файле

            if (bufferString.length() == 0)                                                                //если строка пустая то она пропускается
                bufferString = reader.readLine();                                                       //повторный вызов функции переходит к следующей строке в файле

            characterNumber = bufferString.length();
            QuestionArr[QuestionNumber] = bufferString.toCharArray();                                    //кладем строку в массив посимвольно

            for (int x = 0; x < characterNumber; x++) {                                                    //цикл для определения "зоны ответа"
                if ((QuestionArr[QuestionNumber][x] == '\t')) {                                            //табуляция указывает на то, что после нее искомый для данной строчки предлог
                    QuestionArr[QuestionNumber][x] = ' ';                                                //убераем табуляцию
                    x++;
                    answerIndexChar = 0;                                                                //обнуляем счетчик начального символа "ответа"
                    AnswerBuff = "";
                    QuestionBuff = "";

                    for (int y = 0; y < x; y++) {
                        QuestionBuff += Character.toString(QuestionArr[QuestionNumber][y]);               //Конкатенацией собирается строка ответа по символьно до индекса табуляции
                    }

                    QuestionBuff += " ";

                    while (x < characterNumber) {                                                            //пока текущая строка не закончится
                        AnswerArr[QuestionNumber][answerIndexChar] = QuestionArr[QuestionNumber][x];        //кладем предлог в массив для ответов посимвольно
                        AnswerBuff += Character.toString(AnswerArr[QuestionNumber][answerIndexChar]);         //Конкатенацией собираем строку ответа

                        x++;
                        answerIndexChar++;

                    }

                    AnswerArr[QuestionNumber] = AnswerBuff.toCharArray();
                    QuestionArr[QuestionNumber] = QuestionBuff.toCharArray();

                }

            }

            if (QuestionArr[QuestionNumber][0] != '\n' && QuestionArr[QuestionNumber][0] != 0 && QuestionArr[QuestionNumber][0] != '*')  //это условие позволяет пропускать пустые строки и строки, начинающиеся с *
                QuestionNumber++;                                                                                                     //только при соблюдения этого условия, идет запись следующей строки в массив

            bufferString = reader.readLine();                                                                                         //Считывается следующая строка из файла

        }

        Randomize();                                                                                                                   //сразу после созданияя массивов с вопросами и ответами - формируется массив случайных чисел
        return 0;
    }

    private int Randomize() {                                                                //метод для созданиия последовательности случайных неповторяющихся чисел
        int arrlength;
        arrlength = QuestionNumber;
        int[] nonReccurentCheckArr = new int[arrlength];                                    //буферный массив, для чисел, которые уже были использованы (чтобы чтсла не повторялись)
        int[] randomNumbersArr = new int[arrlength];                                        //массив с конечной последовательностью неповторяющихся случайных чисел

        int RandBuff;                                                                        //числовой буфер для сгенерированного числа
        int x = 0;

        SecureRandom secureRandom = new SecureRandom();

        RandomNumberArrPointer = randomNumbersArr;                                    //передаем адрес первого элемента массива в POINT


        for (int i = 0; i < QuestionNumber; i++) {                                    //буферный массив заполняем числами, которые никак не могут быть индексом массива
            nonReccurentCheckArr[i] = -1;
        }

        for (; ; ) {                                                                    //цикл работает до тех пор, пока не будет сгенерированно количество чисел равное количеству вопросов в тесте
            RandBuff = secureRandom.nextInt(QuestionNumber - 1);

            if (nonReccurentCheckArr[RandBuff] == -1) {                                //если свежесгенерированное число еще не использованно, записываем его в массив
                randomNumbersArr[x] = RandBuff;
                nonReccurentCheckArr[RandBuff] = -2;                                //делаем отметку в буферном массиве, что это число использованно
                x++;
            }

            if (x == QuestionNumber - 1) break;

        }
        return 0;
    }


    static String getAnswer(String botanswer) {
        String answer;
        answer = botanswer;
        return answer;
    }

    int checkAnswer(String answer, String rightAnswer) {
//Принимает результат ввода и возвращает 1 при правельном ответе,
//2 при неправильном
//3 при введении команды "next" - для пропуска вопроса и выведения правильного ответа
//и 0 при команде finish

        String commandFinish = "/finish";
        String commandNext = "/next";
        String commandHelp = "/help";

        if (answer.equals(rightAnswer)) {
            sendbot.sendMessage("Correct");
            System.out.println("Correct\n");
            return 1;
        } else if (answer.equals(commandFinish)) {
            return 0;
        } else if (answer.equals(commandNext)) {
            return 3;
        }else if(answer.equals(commandHelp)){
            return 4;
        } else {
            sendbot.sendMessage("INCORRECT");
            System.out.println("INCORRECT\n");
            return 2;
        }

    }

    public int TestingSummery() {
        String buff1, buff2;                                           //буферные строки

        System.out.println("Всего " + ErrorsNumber + " ошибок  в " + QuestionNumber + " вопросах");
        System.out.println("*Ошибки:\n");
        sendbot.sendMessage("Всего " + ErrorsNumber + " ошибок  в " + QuestionNumber + " вопросах\n" +
                "Ошибки:\n");


        for (int i = 0; i < WrongIndex; i++) {                            //выдает на экран все правильные фразы в которых была допущена ошибка
            buff1 = String.valueOf(QuestionArr[WrongAnswer[i]]);
            buff2 = String.valueOf(AnswerArr[WrongAnswer[i]]);

            System.out.print(QuestionArr[WrongAnswer[i]]);
            System.out.println(AnswerArr[WrongAnswer[i]]);

            sendbot.sendMessage(buff1 + " " + buff2);
        }


        System.out.println("\n*Пропущенные вопросы:\n");
        sendbot.sendMessage("Пропущенные вопросы:");

        for (int x = 0; x < MissedIndex; x++) {                            //выдает на экран все фразы, которые были пропущены
            buff1 = String.valueOf(QuestionArr[MissedQuestions[x]]);
            buff2 = String.valueOf(AnswerArr[MissedQuestions[x]]);

            System.out.print(QuestionArr[MissedQuestions[x]]);
            System.out.println(AnswerArr[MissedQuestions[x]]);

            sendbot.sendMessage(buff1 + " " + buff2);
        }

        return 0;
    }

    void showQuestion() {                                            //Метод, выводящий вопрос в чат телеграм бота
        String showActiveQuestion = String.copyValueOf(QuestionArr[RandomNumberArrPointer[activeQuestionNumber]]);
        sendbot.sendMessage(showActiveQuestion);
    }

    protected void IfTestEnds() {                                                              //метод определяющий условие завершения теста
        if ((typeCommand == 0 || activeQuestionNumber == QuestionNumber)&&!TestingEndFlag) {                    //цикл заканчивает работу либо при переборе всех строк из файла
            setTestingType(0);                                                                //либо при вводе finish
            EnglishHelperBot.setMenu(0);                                                      //меню бота возвращается к 1ому узлу
            TestingEndFlag = true;
            TestingSummery();                                                                  //запускается метод, выдающий результаты теста
        }
    }
}