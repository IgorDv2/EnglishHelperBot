public class InfinitiveGerund extends CoreTesting {                                                             //класс для теста на использование инфинитивов или герундия
    public static void setBotInfAnswer(String botInfAnswer) {
        InfinitiveGerund.botInfAnswer = botInfAnswer;
    }

    public static String getBotInfAnswer() {
        return botInfAnswer;
    }

    private static String botInfAnswer = " ";                                                                   //строка содержащая ответ на вопрос, заданный боту
    private boolean isQuestionRepeated = false;                                                                 //флаг для проверки повторения вопроса
    public static boolean getIsArrayDone() {
        return IsArrayDone;
    }

    public static void setIsArrayDone(boolean isArrayDone) {
        InfinitiveGerund.IsArrayDone = isArrayDone;
    }

    private static boolean IsArrayDone = false;                                                                 //флаг, обозначающий, что файл с вопросами считан

    InfinitiveGerund(String path) {
        super(path);
        typeCommand = 1;
    }

    void StartTest(){                                                                             //метод, выдающий напоминалку после запуска теста
        sendbot.sendMessage("В случае использования инфинитива введите to\n"+
                "В случае использования герундия введите ing\n"+
                "Для пропуска вопроса введите /next\n"+
                "Для завершения теста введите /finish");
    }

    int InfGerundTesting(){											//Метод выполняющий одну итерацию тестирования
        String stringBuffer;									    //Буффер для строки
        String activeAnswer;                                        //Буфер строки ответа

        activeAnswer = AnswerArr.get(RandomNumberArrPointer[activeQuestionNumber]);					//в буфер помещаются строки, соответствующие случайному символу
        typeCommand = checkAnswer(getBotInfAnswer(), activeAnswer);	                                //сверка введенного ответа с правильным ответом

        if(typeCommand == 0){
            TestEnd();                                                                              //конец тестирования
            return 0;
        }

        if (typeCommand == 1) {                                                                         //Если ответ дан правильно
            activeQuestionNumber++;                                                                     //следующий вопрос
            if(activeQuestionNumber == getFixedNumberOfQuestions()) {                                   //если номер текущего вопроса равен максимальному для этого теста
                TestEnd();                                                                              //то тест окончен
                return 0;
            }
            isQuestionRepeated = false;
            showQuestion();                                                                             //бот отображает следующий вопрос
        }

        if (typeCommand == 2) {                                                                         //Если ответ да неверно
            ErrorsNumber++;                                                                             //плюс к количеству ошибок
            showQuestion();
            if (!isQuestionRepeated) {													                //если ошибочный ответ дан первый раз
                    WrongAnswer.add(WrongIndex, RandomNumberArrPointer[activeQuestionNumber]);			//в список кладется номер текущего вопроса и ответа
                    WrongIndex++;
                    isQuestionRepeated = true;
                }
        }

        if (typeCommand == 3) {																			//в случае пропуска вопроса:
            stringBuffer = AnswerArr.get(RandomNumberArrPointer[activeQuestionNumber]);
            //System.out.println(AnswerArr.get(RandomNumberArrPointer[activeQuestionNumber]));

            showQuestion();
            sendbot.sendMessage(stringBuffer);                                                          //выводится правилный ответ
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
        if(typeCommand == 4) {                                                                          //при команде /help выводится напоминалка и текущий вопрос
            StartTest();
            showQuestion();
        }

        return 0;
    }


   /* private void ConsoleAnswerCheck(){
        String activeQuestion;
        activeQuestion = String.copyValueOf(QuestionArr[RandomNumberArrPointer[activeQuestionNumber]]);				//числу, лежащему в массиве случайных числел
        System.out.println(activeQuestionNumber+" of "+QuestionNumber );
        System.out.println(activeQuestion);
        System.out.println(botInfAnswer);
    }*/
}