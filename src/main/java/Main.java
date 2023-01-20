import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        StartTesting();

    }

     static int StartTesting() throws IOException {
        int testingType;
        String pathBuff;
        Scanner read = new Scanner(System.in);
        System.out.println("Для проведения теста по Phrasal verbs, Prepositional phrases, Word patterns \nВведите '1'\n");
        System.out.println("Для проведения теста - Инфинитив или Герундий\nВведите '2'");
        System.out.println("Для завершения теста введите finish");
        System.out.println("Для пропуска вопроса введите next");
        testingType = read.nextInt();


        switch(testingType){ //Вызывается метод для определения типа теста
            case 1:
                pathBuff = "C:\\Users\\Odd\\IdeaProjects\\EnglishHelperJ\\EnglishHelperJ\\PhrasalV.txt";
                Phrasal case1 = new Phrasal(pathBuff);
                case1.Start();
                break;

            case 2:
                pathBuff = "C:\\Users\\Odd\\IdeaProjects\\EnglishHelperJ\\EnglishHelperJ\\InfinitiveOrGerund.txt";
                InfinitiveGerund case2 = new InfinitiveGerund(pathBuff);
                case2.Start();
                break;
        }
         return 0;
    }
}