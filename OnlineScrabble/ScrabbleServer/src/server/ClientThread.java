package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.json.JSONObject;
import org.json.JSONTokener;

public class ClientThread extends Thread {

	private Socket socket;

	private DataOutputStream dataOutputStream;
	private DataInputStream dataInputStream;

	private Server server;

	private boolean listening;

	public ClientThread(Socket socket, Server server) {

		this.server = server;
		this.socket = socket;
		try {
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			this.dataOutputStream.flush();
			dataInputStream = new DataInputStream(socket.getInputStream());
		} catch (IOException ex) {
			System.err.println("Error en la inicialización del ObjectOutputStream y el ObjectInputStream");
			System.exit(0);
		}
	}

	public void disconnect() {

		try {
			socket.close();
			listening = false;
		} catch (IOException ex) {
			System.err.println("Error al cerrar el socket de comunicación con el cliente.");
			System.exit(0);
		}
	}

	public void run() {
		try {
			listen();
		} catch (Exception ex) {
			System.err.println("Error al llamar al método readLine del hilo del cliente.");
			System.exit(0);
		}
		disconnect();
	}

	public void listen() {

		this.listening = true;

		try {
			
			while (listening) {
				
				String gameAction = this.dataInputStream.readUTF();

				if (gameAction != null) {
					JSONObject jo = new JSONObject(new JSONTokener(gameAction));
					this.server.doGameAction(jo);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void sendViewAction(JSONObject jo) {
		try {
			this.dataOutputStream.writeUTF(jo.toString());
			this.dataOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
