public class InfinitiveGerund extends CoreTesting {
    public static void setBotInfAnswer(String botInfAnswer) {
        InfinitiveGerund.botInfAnswer = botInfAnswer;
    }
    private static String botInfAnswer = " ";
    private boolean isQuestionRepeated = false;             //флаг для проверки повторения вопроса

    InfinitiveGerund(String path) {
        super(path);
        typeCommand = 1;
    }

    void StartTest(){
        sendbot.sendMessage("В случае использования инфинитива введите to\n"+
                "В случае использования герундия введите ing\n"+
                "Для пропуска вопроса введите /next\n"+
                "Для завершения теста введите /finish");
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
            showQuestion();
            if (isQuestionRepeated == false) {													//если ошибочный ответ дан первый раз
                    WrongAnswer[WrongIndex] = RandomNumberArrPointer[activeQuestionNumber];			//в массив кладется номер текущего вопроса и ответа
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
            showQuestion();
            isQuestionRepeated = false;
        }
        if(typeCommand == 4) {
            StartTest();
            showQuestion();
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
}