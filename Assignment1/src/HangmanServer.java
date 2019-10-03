import java.net.*;
/*
The HangmanServer class essentially handles any incoming connection requests from clients
and calls the Connection constructors if the request is valid

@author Darron Singh
@date modified October 2 2019
 */

public class HangmanServer {

    public static void main(String[] args) throws Exception{

        //if statement checks to make sure that the correct number of arguments were given
        if (args.length !=1){
            System.err.println("Usage: java HangmanServer <port number>");
            System.exit(1);
        }

        int portNum = Integer.parseInt(args[0]);
        HangmanServer connection = new HangmanServer();
        connection.run(portNum);
    }

    /*
    The run method gets called to create the socket to connect to the connection class

    @param the port number that was entered
    @return nothing
     */

    public void run(int portNum){
        try{
            ServerSocket serverSocket = new ServerSocket(portNum);
            while(true){
                Socket HangmanClient = serverSocket.accept();
                Connection cc = new Connection(HangmanClient);
            }
        } catch(Exception e){
            System.out.println("Exception" + e);
        }
    }

}
