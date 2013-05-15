import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	static ServerSocket serverSocket;
	static Socket socket;
	static DataOutputStream out;
	static DataInputStream in;
	static Users[] user = new Users[10];

	public static void main(String[] args) throws IOException {
		System.out.println("Starting a server socket...");
		serverSocket = new ServerSocket(7778);
		System.out.println("Server socket created on port 7778");
		while (true) {
			socket = serverSocket.accept();

			for (int i = 0; i < 10; i++) {

				System.out.println("Accepting client from "
						+ socket.getInetAddress());
				out = new DataOutputStream(socket.getOutputStream());
				in = new DataInputStream(socket.getInputStream());
				if (user[i] == null) {

					user[i] = new Users(out, in, user);
					Thread thread = new Thread(user[i]);
					thread.start();
					break;
				}
				out.writeUTF("Connected to Server Socket");
				
				
			}
		}
	}

}

class Users implements Runnable {

	DataOutputStream out;
	DataInputStream in;
	Users[] user = new Users[10];

	String name;
	
	public Users(DataOutputStream out, DataInputStream in, Users[] user) {
		this.out = out;
		this.in = in;
		this.user = user;
		
	}
	boolean sendText;

	public void run() {
		
	
		String message = "";
		while (true) {
			message= " ";
			try {
				message = in.readUTF();
System.out.println(message);
				if (message.startsWith("/nick")){
					message = message.replace("/nick", "");
					String oldname = name;
					name = message;
					for (int i = 0; i < 10; i++) {
						if (user[i] != null) {
							if (oldname!=null){
							user[i].out.writeUTF(oldname+ " is know known as " + name);
							sendText = false;
							}else{
								user[i].out.writeUTF(name+" entered the chatroom");
								sendText = false;
							}
							
						}
						
					}
				}
				
				for (int i = 0; i < 10; i++) {
					if (user[i] != null&&sendText==true) {
						user[i].out.writeUTF(name+ " : " + message);
					}
					
				}
				sendText = true;

			} catch (IOException e) {
				this.out = null;
				this.in = null;
			}
		}
	}

}
