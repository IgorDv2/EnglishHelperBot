import org.apache.commons.lang3.StringUtils;

public class Articles extends CoreTesting{
    public static void setBotArtAnswer(String botArtAnswer) {
        Articles.botArtAnswer = botArtAnswer;
    }

    public static String getBotArtAnswer() {
        return botArtAnswer;
    }

    private static String botArtAnswer = " ";                                                                   //строка содержащая ответ на вопрос, заданный боту
    private boolean isQuestionRepeated = false;                                                                 //флаг для проверки повторения вопроса
    public static boolean getIsArrayDone() {
        return IsArrayDone;
    }

    public static void setIsArrayDone(boolean isArrayDone) {
        Articles.IsArrayDone = isArrayDone;
    }

    private static boolean IsArrayDone = false;                                                                 //флаг, обозначающий, что файл с вопросами считан

    Articles(String path) {
        super(path);
        typeCommand = 1;
    }

    void StartTest(){                                                                             //метод, выдающий напоминалку после запуска теста
        sendbot.sendMessage("для всех вопросов в приоритете имеется ввиду исчислимое существительное в единственном числе, если это возможно и не противоречит условию"+
                "В случае использования неопределенного артикля введите 'the'\n"+
                "В случае использования определенного артикля введите 'a' (или 'an' если это необходимо) \n"+
                "В случае использования нулевого артикля введите 'none'\n"+
                "Для пропуска вопроса введите /next\n"+
                "Для завершения теста введите /finish");
    }

    int ArticlesTesting(){											//Метод выполняющий одну итерацию тестирования
        String stringBuffer;									    //Буффер для строки
        String activeAnswer;                                        //Буфер строки ответа
        String activeAnswer1;                                                               //Буфер строки ответа
        String activeAnswer2;                                                               //буфер для возможного альтернативного ответа
        String[] result;                                                                    //массив строк для альтернативных ответов

        activeAnswer = AnswerArr.get(RandomNumberArrPointer[activeQuestionNumber]);					//в буфер помещаются строки, соответствующие случайному символу
        if (activeAnswer.contains("/")) {                                                   //если активная строка содержит символ "/"
            result = StringUtils.split(activeAnswer, "/");                  //то символы до нее составляют один из альтернативных ответов
            activeAnswer1 = result[0];                                                  //а символы после - другой
            activeAnswer2 = result[1];                                                  //в целом этот блок - главное и единственное отличие от InfGerundTesting() - ТРЕБУЕТСЯ РЕФАКТОРИНГ!
            typeCommand = checkAnswer(getBotArtAnswer(), activeAnswer1, activeAnswer2);  //в этом случае проверка идет с любым из ответов
        }
        else


            typeCommand = checkAnswer(getBotArtAnswer(), activeAnswer);	                                //сверка введенного ответа с правильным ответом

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

}

