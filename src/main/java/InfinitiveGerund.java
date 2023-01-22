import java.io.IOException;

public class InfinitiveGerund extends CoreTesting {
    public static void setBotInfAnswer(String botInfAnswer) {
        InfinitiveGerund.botInfAnswer = botInfAnswer;
    }

    private static String botInfAnswer = " ";
    private static EnglishHelperBot sendbot;                //Экземпляр бота для отправки ботом сообщений
    private static int activeQuestionNumber = 0;            //Номер текущего вопроса
    private boolean isQuestionRepeated = false;             //флаг для проверки повторения вопроса
    private static int typeCommand = 1;		                //управляющая переменная

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
                "В случае использования герундия введите ing\n"+
                "Для пропуска вопроса введите next");
    }
    void showQuestion(){                                            //Метод, выводящий вопрос в чат телеграм бота
        String showActiveQuestion = String.copyValueOf(QuestionArr[RandomNumberArrPointer[activeQuestionNumber]]);
        sendbot.sendMessage(showActiveQuestion);
    }

    int InfGerundTesting(){											//Метод выполняющий одну итерацию тестирования
        String stringBuffer;									    //Буффер для строки
        String activeAnswer;                                        //Буфер строки ответа

        IfTestEnds();

        activeAnswer = String.copyValueOf(AnswerArr[RandomNumberArrPointer[activeQuestionNumber]]);					//в буфер помещаются строки, соответствующие случайному

        //ConsoleAnswerCheck();

        typeCommand = checkAnswer(getAnswer(botInfAnswer), activeAnswer);	                            //сверка введенного ответа с правильным ответом

        IfTestEnds();

        if (typeCommand == 1) {                                                                         //Если ответ дан правильно
            activeQuestionNumber++;                                                                     //следующий вопрос
            showQuestion();                                                                             //следующий вопрос отображает бот
            isQuestionRepeated = false;
        }

        if (typeCommand == 2) {                                                                         //Если ответ да неверно
            ErrorsNumber++;                                                                             //плюс к количеству ошибок
                if (isQuestionRepeated == false) {													//если ошибочный ответ дан первый раз
                    WrongAnswer[WrongIndex] = RandomNumberArrPointer[activeQuestionNumber];			//в массив кладется номер текущего вопроса и ответа
                    WrongIndex++;
                    showQuestion();
                    isQuestionRepeated = true;
                }
        }

        if (typeCommand == 3) {																			//в случае пропуска вопроса:
            stringBuffer = String.copyValueOf(AnswerArr[RandomNumberArrPointer[activeQuestionNumber]]);
            System.out.println(AnswerArr[RandomNumberArrPointer[activeQuestionNumber]]);

            showQuestion();
            sendbot.sendMessage(stringBuffer);                                                          //выводится правилный ответ
            ErrorsNumber++;
            MissedQuestions[MissedIndex] = RandomNumberArrPointer[activeQuestionNumber];				//в масив кладется номер текущего вопроса и ответа
            MissedIndex++;
            activeQuestionNumber++;                                                                     //следующий вопрос
            showQuestion();
            isQuestionRepeated = false;
        }

        IfTestEnds();

        return 0;
    }


    private void ConsoleAnswerCheck(){
        String activeQuestion;
        activeQuestion = String.copyValueOf(QuestionArr[RandomNumberArrPointer[activeQuestionNumber]]);				//числу, лежащему в массиве случайных числел
        System.out.println(activeQuestionNumber+" of "+QuestionNumber );
        System.out.println(activeQuestion);
        System.out.println(botInfAnswer);
    }

    private void IfTestEnds(){                                                              //метод определяющий условие завершения теста
        if (typeCommand == 0|| activeQuestionNumber == QuestionNumber) {				    //цикл заканчивает работу либо при переборе всех строк из файла
            setTestingType(0);                                                                //либо при вводе finish
            EnglishHelperBot.setMenu(0);                                                      //меню бота возвращается к 1ому узлу
            TestingSummery();                                                               //запускается метод, выдающий результаты теста
        }
    }
}