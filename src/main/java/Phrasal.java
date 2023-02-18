import org.apache.commons.lang3.StringUtils;

public class Phrasal extends CoreTesting{                           //класс для проведения тестов на знание устойчивых выражений
    public static void setBotPhraseAnswer(String botInfAnswer) {
        Phrasal.botPhraseAnswer = botInfAnswer;
    }
    public static String getBotPhraseAnswer() {
        return botPhraseAnswer;
    }
    private static String botPhraseAnswer = " ";                    //строка содержащая ответ на вопрос, заданный боту
    private boolean isQuestionRepeated = false;                     //флаг для проверки повторения вопроса

    public static boolean getIsArrayDone() {
        return IsArrayDone;
    }
    public static void setIsArrayDone(boolean isArrayDone) {
        Phrasal.IsArrayDone = isArrayDone;
    }

    private static boolean IsArrayDone = false;                     //флаг, обозначающий, что файл с вопросами считан

    Phrasal(String path) {
        super(path);
        typeCommand = -1;
    }

    void StartTest(){                                               //метод, выдающий напоминалку после запуска теста
        sendbot.sendMessage("Для пропуска вопроса введите /next\n"+
                "Для завершения теста введите /finish");
    }
    int PhrasalTesting() {													                //Метод выполняющий одну итерацию тестирования

        String stringBuffer;									                            //Буфер для строки
        String activeAnswer;
        String activeAnswer1;                                                               //Буфер строки ответа
        String activeAnswer2;                                                               //буфер для возможного альтернативного ответа
        String[] result;                                                                    //массив строк для альтернативных ответов

        activeAnswer = AnswerArr.get(RandomNumberArrPointer[activeQuestionNumber]);			//в буфер помещаются строки, соответствующие случайному
        if (activeAnswer.contains("/")) {                                                   //если активная строка содержит символ "/"
                result = StringUtils.split(activeAnswer, "/");                  //то символы до нее составляют один из альтернативных ответов
                activeAnswer1 = result[0];                                                  //а символы после - другой
                activeAnswer2 = result[1];                                                  //в целом этот блок - главное и единственное отличие от InfGerundTesting() - ТРЕБУЕТСЯ РЕФАКТОРИНГ!
            typeCommand = checkAnswer(getBotPhraseAnswer(), activeAnswer1, activeAnswer2);  //в этом случае проверка идет с любым из ответов
        }
        else
            typeCommand = checkAnswer(getBotPhraseAnswer(), activeAnswer);	                 //сверка введенного ответа с правильным ответом

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
            if (!isQuestionRepeated) {													        //если ошибочный ответ дан первый раз
                WrongAnswer.add(WrongIndex, RandomNumberArrPointer[activeQuestionNumber]);			        //в массив кладется номер текущего вопроса и ответа
                WrongIndex++;
                isQuestionRepeated = true;
            }
        }

        if (typeCommand == 3) {																			//в случае пропуска вопроса:
            stringBuffer = AnswerArr.get(RandomNumberArrPointer[activeQuestionNumber]);
            //System.out.println(AnswerArr.get(RandomNumberArrPointer[activeQuestionNumber]));

            showQuestion();
            sendbot.sendMessage(stringBuffer);                                                          //выводится правильный ответ
            ErrorsNumber++;
            MissedQuestions.add(MissedIndex, RandomNumberArrPointer[activeQuestionNumber]);				//в список кладется номер текущего вопроса и ответа
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
        String showActiveQuestion = QuestionArr.get(RandomNumberArrPointer[activeQuestionNumber]); //вроде ничего не делает, разобраться в дальнейшем
        sendbot.sendMessage(showActiveQuestion);
    }


    /*private void ConsoleAnswerCheck() {
        String activeQuestion;
        activeQuestion = String.copyValueOf(QuestionArr[RandomNumberArrPointer[activeQuestionNumber]]);                //числу, лежащему в массиве случайных числел
        System.out.println(activeQuestionNumber + " of " + QuestionNumber);
        System.out.println(activeQuestion);
        System.out.println(botPhraseAnswer);
    }*/
}
