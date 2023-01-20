import java.io.IOException;

public class InfinitiveGerund extends CoreTesting {
    public static String botInfAnswer = " ";
    static EnglishHelperBot sendbot;
    static int activeQuestionNumber = 0;
    boolean isQuestionRepeated = false;
    static int typeCommand = 1;														        //управляющая переменная


    static {
        try {
            sendbot = new EnglishHelperBot();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    InfinitiveGerund(String path) throws IOException {
        super(path);
    }

    void StartTest(){
        sendbot.sendMessage("В случае использования инфинитива введите to\n"+
                "В случае использования инфинитива введите ing");

        //FileToArray();
        //InfGerundTesting();
        //TestingSummery();
    }
    void showQuestion(){
        String showActiveQuestion = String.copyValueOf(QuestionArr[RandomNumberArrPointer[activeQuestionNumber]]);
        sendbot.sendMessage(showActiveQuestion);
    }

    int InfGerundTesting(){													                //основной метод для запуска теста{
        String buff;													//индекс вопросса/ответа в порядке их выведения
        String activeAnswer;																//строковый буфер для проверки правильности ответа
        String activeQuestion;																//строковый буфер для текущего вопроса

        												//переменная для повторяющегося неверного ответа

        //System.out.println(QuestionNumber);
       // for (;;) {

            if (typeCommand == 0|| activeQuestionNumber == QuestionNumber) {						//цикл заканчивает работу либо при переборе всех строк из файла
                TestingType = 0;
                EnglishHelperBot.menu = 0;
                TestingSummery();
                //break;																				//либо если методом проверки возвращен 0
            }

            activeAnswer = String.copyValueOf(AnswerArr[RandomNumberArrPointer[activeQuestionNumber]]);					//в буфер помещаются строки, соответствующие случайному
            activeQuestion = String.copyValueOf(QuestionArr[RandomNumberArrPointer[activeQuestionNumber]]);				//числу, лежащему в массиве случайных числел
            //showQuestion();
            System.out.println(activeQuestionNumber+" of "+QuestionNumber );
            System.out.println(activeQuestion);
            //sendbot.sendMessage(activeQuestion);
            System.out.println(botInfAnswer);
            //sendbot.sendMessage(botInfAnswer);

        typeCommand = checkAnswer(getAnswer(botInfAnswer), activeAnswer);									//проверка правильного ответа
            //при правильном ответе выводится новая строка
        if (typeCommand == 0) {						//цикл заканчивает работу либо при переборе всех строк из файла
            TestingType = 0;
            EnglishHelperBot.menu = 0;
            TestingSummery();
            //break;																				//либо если методом проверки возвращен 0
        }

        if (typeCommand == 1) {
                activeQuestionNumber++;
                showQuestion();
                isQuestionRepeated = false;
            }

            if (typeCommand == 2) {
                ErrorsNumber++;
                if (isQuestionRepeated == false) {													//если ошибочный ответ дан первый раз
                    WrongAnswer[WrongIndex] = RandomNumberArrPointer[activeQuestionNumber];			//в массив кладется номер текущего вопроса и ответа
                    WrongIndex++;
                    showQuestion();
                    isQuestionRepeated = true;
                }


            }

            if (typeCommand == 3) {																			//в случае пропуска вопроса - выводится правильный ответ
                buff = String.copyValueOf(AnswerArr[RandomNumberArrPointer[activeQuestionNumber]]);
                System.out.println(AnswerArr[RandomNumberArrPointer[activeQuestionNumber]]);				//и после выводится новая строка
                showQuestion();
                sendbot.sendMessage(buff);
                ErrorsNumber++;
                MissedQuestions[MissedIndex] = RandomNumberArrPointer[activeQuestionNumber];				//в масив кладется номер текущего вопроса и ответа
                MissedIndex++;
                activeQuestionNumber++;
                showQuestion();
                isQuestionRepeated = false;
            }
       // }

        return 0;
    }
}
