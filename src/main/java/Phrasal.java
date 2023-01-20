import java.io.IOException;

public class Phrasal extends CoreTesting{
    static String botPhraseAnswer;
    Phrasal(String path) throws IOException {
        super(path);
    }

    void Start() throws IOException {
        FileToArray();
        PhrasalTesting();
        TestingSummery();
    }

    int PhrasalTesting()
    {													                                    //основной метод для запуска теста{
        int activeQuestionNumber = 0;														//индекс вопросса/ответа в порядке их выведения
        String activeAnswer;																//строковый буфер для проверки правильности ответа
        String activeQuestion;																//строковый буфер для текущего вопроса
        int typeCommand = 1;														        //управляющая переменная
        boolean isQuestionRepeated = false;													//переменная для повторяющегося неверного ответа


        for (;;) {
            if (typeCommand == 0|| activeQuestionNumber == QuestionNumber) {						//цикл заканчивает работу либо при переборе всех строк из файла
                TestingType = 0;
                break;																				//либо если методом проверки возвращен 0
            }

            activeAnswer = String.copyValueOf(AnswerArr[RandomNumberArrPointer[activeQuestionNumber]]);					//в буфер помещаются строки, соответствующие случайному
            activeQuestion = String.copyValueOf(QuestionArr[RandomNumberArrPointer[activeQuestionNumber]]);				//числу, лежащему в массиве случайных числел

            System.out.println(activeQuestion+"___");
            typeCommand = checkAnswer(getAnswer(botPhraseAnswer), activeAnswer);									//проверка правильного ответа

                if (typeCommand == 1) {                                                             //при правильном ответе выводится новая строка
                activeQuestionNumber++;
                isQuestionRepeated = false;
            }

            if (typeCommand == 2) {
                ErrorsNumber++;
                if (isQuestionRepeated == false) {													//если ошибочный ответ дан первый раз
                    WrongAnswer[WrongIndex] = RandomNumberArrPointer[activeQuestionNumber];			//в массив кладется номер текущего вопроса и ответа
                    WrongIndex++;
                    isQuestionRepeated = true;
                }


            }

            if (typeCommand == 3) {																			//в случае пропуска вопроса - выводится правильный ответ
                System.out.println(AnswerArr[RandomNumberArrPointer[activeQuestionNumber]]);				//и после выводится новая строка
                ErrorsNumber++;
                MissedQuestions[MissedIndex] = RandomNumberArrPointer[activeQuestionNumber];				//в масив кладется номер текущего вопроса и ответа
                MissedIndex++;
                activeQuestionNumber++;
                isQuestionRepeated = false;
            }
        }

        return 0;
    }
}
