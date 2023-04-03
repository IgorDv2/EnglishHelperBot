import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;

public class CoreTesting {
    protected ArrayList<String> QuestionArr = new ArrayList<>();                    //список для хранения строк, содержащих вопросы
    protected ArrayList<String> AnswerArr = new ArrayList<>();                      //список для хранения строк, содержащих ответы
    protected ArrayList<Integer> WrongAnswer= new ArrayList<>();                    //список номеров вопросов/ответов, где была ошибка
    protected ArrayList<Integer> MissedQuestions= new ArrayList<>();                //список номеров вопросов, которые были пропущены
    protected int QuestionNumber = 0;                                               //общее число вопросов/ответов
    protected int[] RandomNumberArrPointer;                                         //Указатель на массив со случайными числами
    protected int ErrorsNumber = 0;                                                 //общее количество ошибок

    protected int WrongIndex = 0;                                                    //индекс массива вопросов/ответов c ошибками
    protected int MissedIndex = 0;                                                   //индекс массива пропущенных впросов

    protected int activeQuestionNumber = 0;                                          //Номер текущего вопроса

    public static int getFixedNumberOfQuestions() {
        return fixedNumberOfQuestions;
    }

    public static void setFixedNumberOfQuestions(int fixedNumberOfQuestions) {
        CoreTesting.fixedNumberOfQuestions = fixedNumberOfQuestions;
    }

    private static int fixedNumberOfQuestions = 50;                                    //переменная, содержащая максимальное количество вопросов для конкретного теста

    protected static EnglishHelperBot sendbot;                                          //Экземпляр бота для отправки ботом сообщений

    static {
        try {
            sendbot = new EnglishHelperBot();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected int typeCommand = 1;                                                      //переменная обозначающая состояние тестирования
    protected String DataFilePath;                                                      //Строка для хранения пути к текстовому файлу с вопросами

    public static String getTestingType() {
        return TestingType;
    }

    public static void setTestingType(String testingType) {
        TestingType = testingType;
    }

    protected static String TestingType;

    CoreTesting(String path) {                                                          //Конструктор принимает строку с путем к файлу
        DataFilePath = path;
    }

    public int FileToArray() throws IOException {

        String bufferString;                                    //буферная строка

        File DataFile = new File(DataFilePath);
        FileReader fr = new FileReader(DataFile);
        BufferedReader reader = new BufferedReader(fr);

        bufferString = reader.readLine();                        //Считывается первая строка из файла

        while (bufferString != null) {                           //пока не закончатся строки в файле

            if (bufferString.length() == 0)                      //если строка пустая, то она пропускается
                bufferString = reader.readLine();                //повторный вызов метода переходит к следующей строке в файле

            QuestionArr.add(QuestionNumber, bufferString);       //кладем строку из буфера в список вопросов
            QuestionAndAnswerWriting(bufferString.length());     //вызываем основную функцию передавая длину текущей строки

            if (QuestionArr.get(QuestionNumber).charAt(0) != '\n' && QuestionArr.get(QuestionNumber).charAt(0) != 0 && QuestionArr.get(QuestionNumber).charAt(0) != '*')    //это условие позволяет пропускать пустые строки и строки, начинающиеся с *
                QuestionNumber++;                                                                                                                                           //только при соблюдении этого условия, идет запись следующей строки в список

            bufferString = reader.readLine();                     //Считывается следующая строка из файла

        }
        System.out.println(QuestionNumber);
        return 0;
    }

    public int Randomize() {                                                                //метод для созданиия последовательности случайных неповторяющихся чисел
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
                nonReccurentCheckArr[RandBuff] = -2;                                    //делаем отметку в буферном массиве, что это число использованно
                x++;
            }

            if (x == QuestionNumber - 1) break;

        }
        return 0;
    }

    int checkAnswer(String answer, String rightAnswer) {
//Принимает результат ввода и возвращает 1 при правильном ответе,
//2 при неправильном
//3 при введении команды "next" - для пропуска вопроса и выведения правильного ответа
//и 0 при команде finish
// 4 при команде /help

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
        } else if (answer.equals(commandHelp)) {
            return 4;
        } else {
            sendbot.sendMessage("INCORRECT");
            System.out.println("INCORRECT\n");
            return 2;
        }

    }

    int checkAnswer(String answer, String rightAnswer1, String rightAnswer2) {
        //перегруженный метод для сравнения с двумя вариантами правильного ответа
//Принимает результат ввода и возвращает 1 при правильном ответе,
//2 при неправильном
//3 при введении команды "next" - для пропуска вопроса и выведения правильного ответа
//и 0 при команде finish
// 4 при команде /help

        String commandFinish = "/finish";
        String commandNext = "/next";
        String commandHelp = "/help";

        if (answer.equals(rightAnswer1) || answer.equals(rightAnswer2)) {
            sendbot.sendMessage("Correct");
            System.out.println("Correct\n");
            return 1;
        } else if (answer.equals(commandFinish)) {
            return 0;
        } else if (answer.equals(commandNext)) {
            return 3;
        } else if (answer.equals(commandHelp)) {
            return 4;
        } else {
            sendbot.sendMessage("INCORRECT");
            System.out.println("INCORRECT\n");
            return 2;
        }

    }

    public int TestingSummery() {
        String buff1, buff2;                                               //буферные строки

        System.out.println("Всего " + ErrorsNumber + " ошибок  в " + getFixedNumberOfQuestions() + " вопросах");
        System.out.println("*Ошибки:\n");
        sendbot.sendMessage("***\nВсего " + ErrorsNumber + " ошибок  в " + getFixedNumberOfQuestions() + " вопросах\n" +
                "Ошибки:\n***");

        for (int i = 0; i < WrongIndex; i++) {                              //выдает на экран все правильные фразы в которых была допущена ошибка

            buff1 = QuestionArr.get(WrongAnswer.get(i));
            buff2 = AnswerArr.get(WrongAnswer.get(i));

            System.out.print(QuestionArr.get(WrongAnswer.get(i)));
            System.out.print(" ");
            System.out.println(AnswerArr.get(WrongAnswer.get(i)));

            sendbot.sendMessage(buff1 + " " + buff2);
        }


        System.out.println("\n*Пропущенные вопросы:\n");
        sendbot.sendMessage("***\nПропущенные вопросы:\n***");

        for (int x = 0; x < MissedIndex; x++) {                            //выдает на экран все фразы, которые были пропущены
            buff1 = QuestionArr.get(MissedQuestions.get(x));
            buff2 = AnswerArr.get(MissedQuestions.get(x));

            System.out.print(QuestionArr.get(MissedQuestions.get(x)));
            System.out.println(AnswerArr.get(MissedQuestions.get(x)));

            sendbot.sendMessage(buff1 + " " + buff2);
        }

        return 0;
    }

    void showQuestion() {                                            //Метод, выводящий вопрос в чат телеграм бота
        String showActiveQuestion = (activeQuestionNumber+1) + ".   " + QuestionArr.get(RandomNumberArrPointer[activeQuestionNumber]);
        sendbot.sendMessage(showActiveQuestion);
    }

    protected void TestEnd() {                                          //метод, который обнуляет индексы в конце конкретного тестирования
        TestingSummery();                                               //вызывает метод показывающий результаты тестирования
        EnglishHelperBot.setMenu(0);                                    //переключает меню выбора команд в начало
        WrongIndex = 0;
        MissedIndex = 0;
        ErrorsNumber = 0;
        activeQuestionNumber = 0;
        typeCommand = -1;
        setFixedNumberOfQuestions(50);

    }

    void QuestionAndAnswerWriting(int characterNumber) {                //единый метод для записи вопросов и ответов в соответствующие списки, принимает длину текущей строки
        int activeCharacterIndex = 0;                                   //все откоменченные выводы в консоль помогают понять как это все работает, пока не удалять!!!
        String QuestionBuff = "";
        String AnswerBuff = "";
        boolean isAnswer = false;
        boolean inBetweenAnswer = false;

        //System.out.print(QuestionArr.get(QuestionNumber));
        //System.out.println(" " + QuestionArr.get(QuestionNumber).length());

        for (; activeCharacterIndex < characterNumber; activeCharacterIndex++) {                   //для каждого символа строки
            if ((QuestionArr.get(QuestionNumber).charAt(activeCharacterIndex) == '\t')) {          //если обнаружен знак табуляции(который указывает на то, что ответ идет прямо за ним)
                QuestionArr.get(QuestionNumber).replace('\t', ' ');                 //заменяем табуляцию на пробел
                activeCharacterIndex++;

                for (; activeCharacterIndex < characterNumber; activeCharacterIndex++) {            //перебираем дальше строку посимвольно до следующего пробела
                    if (QuestionArr.get(QuestionNumber).charAt(activeCharacterIndex) == ' ') {      //если пробел найден до конца строки выходим из цикла
                        isAnswer = true;                                                            //флаг найденного ответа
                        inBetweenAnswer = true;                                                     //флаг ответа в середине вопроса
                        break;
                    }
                    AnswerBuff += Character.toString(QuestionArr.get(QuestionNumber).charAt(activeCharacterIndex));     //конкатенируем посимвольно символы ответа в буферную строку(ДА это костылесипед, НЕТ пока я не буду его распутывать)
                    //System.out.println(AnswerBuff + " " + activeCharacterIndex + " (" + characterNumber + ")");
                }

                AnswerArr.add(QuestionNumber, AnswerBuff);                  //добавляем в список ответов строку из буфера
                //System.out.print(AnswerBuff);
                //System.out.println(" " + AnswerBuff.length());
                continue;                                                   //и переходим к следующему символу
            }

            if (isAnswer == true) {                                         //при флаге ответа
                //System.out.println("isAnswer");
                QuestionBuff += " ___ ";                                    //обозначаем места, где он ожидается
                isAnswer = false;
            }
            QuestionBuff += Character.toString(QuestionArr.get(QuestionNumber).charAt(activeCharacterIndex));           //конкатенируем посимвольно символы вопроса в буферную строку(ДА, это еще один костылесипед)
            //System.out.println(QuestionBuff);
        }
        if (inBetweenAnswer == false && getTestingType().equals("Phrasal")) {       //если тип тестирования - устойчивые фразы и ответ лежит внутри фразы
                QuestionBuff += " ___ ";                                            //конкатенируем обозначение этого места в конец вопроса
        }
        QuestionArr.add(QuestionNumber , QuestionBuff);                       //записываем текущий вопрос в список вопросов.
    }

}

