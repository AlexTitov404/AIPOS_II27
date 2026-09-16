import java.io.*;
import java.net.*;
import java.util.ArrayList;

class Pair {
    int number;
    char letter;

    Pair(char letter, int number) {
        
        this.letter = letter;
        this.number = number;
    }
}

public class Server {
// Выбираем номер порта за пределами 1-1024:
public static final int PORT = 8080;
public static void main(String[] args)
throws IOException {
ServerSocket s = new ServerSocket(PORT);
System.out.println( "Started: " + s);
try {
Socket socket = s.accept();
try {
System.out.println( "Connection accepted: " + socket);
BufferedReader in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter( socket.getOutputStream())), true);

final int BUFFER_SIZE = 64;
char[] buffer = new char[BUFFER_SIZE];
int totalRead = 0;

ArrayList<Pair> p = new ArrayList<>();

while (true) {
    int count = in.read(buffer, totalRead, BUFFER_SIZE - totalRead);
    if (count == -1) break;
    totalRead += count;

    if (totalRead == BUFFER_SIZE) {
        // Подсчёт вхождений символов
        for (char c : buffer) {
            boolean isExist = false;
            for (int i = 0; i < p.size(); i++) {
                if (p.get(i).letter == c) {
                    p.get(i).number++;
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                Pair temp = new Pair(c, 1);
                p.add(temp);
            }
        }

        // Вывод ответа
        System.out.println("Echoing the answer:");
        if(p.size() <= 3){
            System.out.println('\n' + "not enough characters");
            System.out.println( "closing..." );
            out.println('\n' + "not enough characters");
            out.println("closing...");
            socket.close();
            s.close();
            return;
        }
        else{
        for (int i = 0; i < p.size(); i++) {
            String line = "Character " + p.get(i).letter + ": " + p.get(i).number;
            System.out.println(line);
            out.println(line);
        }
        out.flush();
        totalRead = 0;
        p.clear();
    }
    }
}
}
finally
{
System.out.println( "closing..." );
socket.close();
}
}
finally
{
s.close();
}
}
}

