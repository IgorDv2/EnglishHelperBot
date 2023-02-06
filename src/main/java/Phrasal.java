import org.apache.commons.lang3.StringUtils;

public class Phrasal extends CoreTesting{

    public static void setBotPhraseAnswer(String botInfAnswer) {
        Phrasal.botPhraseAnswer = botInfAnswer;
    }
    private static String botPhraseAnswer = " ";
    private boolean isQuestionRepeated = false;             //флаг для проверки повторения вопроса

    public static boolean getIsArrayDone() {
        return IsArrayDone;
    }

    public static void setIsArrayDone(boolean isArrayDone) {
        Phrasal.IsArrayDone = isArrayDone;
    }

    private static boolean IsArrayDone = false;

    Phrasal(String path) {
        super(path);
        typeCommand = -1;
        if(getFixedNumberOfQuestions()>QuestionNumber)
            setFixedNumberOfQuestions(QuestionNumber);
    }

    void StartTest(){
        sendbot.sendMessage("Для пропуска вопроса введите /next\n"+
                "Для завершения теста введите /finish");
    }
    int PhrasalTesting()
    {													                                    //основной метод для запуска теста{
        String stringBuffer;									                            //Буффер для строки
        String activeAnswer;                                                                //Буфер строки ответа
        String activeAnswer1;
        String activeAnswer2;
        String[] result;

        activeAnswer = String.copyValueOf(AnswerArr[RandomNumberArrPointer[activeQuestionNumber]]);					//в буфер помещаются строки, соответствующие случайному
        if (activeAnswer.contains("/")) {
                result = StringUtils.split(activeAnswer, "/");
                activeAnswer1 = result[0];
                activeAnswer2 = result[1];
            typeCommand = checkAnswer(botPhraseAnswer, activeAnswer1, activeAnswer2);
        }
        else
            typeCommand = checkAnswer(botPhraseAnswer, activeAnswer);	                                                //сверка введенного ответа с правильным ответом

        if(typeCommand == 0){
            TestEnd();
            return 0;
        }
        if (typeCommand == 1) {                                                                         //Если ответ дан правильно
            activeQuestionNumber++;
            isQuestionRepeated = false;                                                                  //следующий вопрос
            if(activeQuestionNumber == getFixedNumberOfQuestions()) {
                TestEnd();
                return 0;
            }
            showQuestion();                                                                             //следующий вопрос отображает бот
        }

        if (typeCommand == 2) {                                                                         //Если ответ да неверно
            ErrorsNumber++;                                                                             //плюс к количеству ошибок
            showQuestion();
            if (isQuestionRepeated == false) {													        //если ошибочный ответ дан первый раз
                WrongAnswer[WrongIndex] = RandomNumberArrPointer[activeQuestionNumber];			        //в массив кладется номер текущего вопроса и ответа
                WrongIndex++;
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
            isQuestionRepeated = false;
            if(activeQuestionNumber == getFixedNumberOfQuestions()) {
                TestEnd();
                return 0;
            }
            showQuestion();
        }
        if(typeCommand == 4) {
            StartTest();
            showQuestion();
        }
        return 0;
    }
    @Override
    void showQuestion() {                                            //Метод, выводящий вопрос в чат телеграм бота
        String showActiveQuestion = String.copyValueOf(QuestionArr[RandomNumberArrPointer[activeQuestionNumber]]);
        sendbot.sendMessage(showActiveQuestion);
    }


    private void ConsoleAnswerCheck() {
        String activeQuestion;
        activeQuestion = String.copyValueOf(QuestionArr[RandomNumberArrPointer[activeQuestionNumber]]);                //числу, лежащему в массиве случайных числел
        System.out.println(activeQuestionNumber + " of " + QuestionNumber);
        System.out.println(activeQuestion);
        System.out.println(botPhraseAnswer);
    }
}
