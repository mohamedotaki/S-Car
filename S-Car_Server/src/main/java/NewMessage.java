import java.util.Scanner;

public class NewMessage implements Runnable {
    Subject subject;

    public NewMessage(Subject subject) {
        this.subject = subject;
    }

    public void run(){
        Commentary cObject = ((Commentary)subject);
        while(true){
            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();
            cObject.setDesc(message);

        }
    }





}
