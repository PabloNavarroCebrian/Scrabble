package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.json.JSONObject;
import org.json.JSONTokener;

import control.Controller;
import control.ControllerSerializer;
import observersActions.OnObserverAction;
import observersActions.OnRegister;
import view.GUIView;
import view.ScrabbleObserver;

public class Client extends Thread {

	private List<ScrabbleObserver> observers;

	private int puerto;
	private String iP;

	private String name;

	private Socket socket;

	private DataOutputStream dataOutputStream;
	private DataInputStream dataInputStream;

	private OnRegister onRegister;
	private boolean alreadyRegistered;

	private boolean listening;

	public Client(String name, String IP, int puerto) {
		this.observers = new ArrayList<ScrabbleObserver>();
   		this.puerto = puerto;
   		this.iP = IP;
   		this.name = name;
   		this.listening = true;
   		this.onRegister = new OnRegister();
   		this.alreadyRegistered = false;
   	}

	public void addObserver(ScrabbleObserver o) {
		if (!observers.contains(o)) {
			this.observers.add(o);
		}
	}

	public void removeObserver(ScrabbleObserver o) {
		this.observers.remove(o);
	}

	public void sendGameAction(JSONObject jo) {
		try {
			this.dataOutputStream.writeUTF(jo.toString());
			this.dataOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void doViewAction(JSONObject jo) {
		
		if(jo.getString("type").contentEquals("register"))
			this.onRegister.register(jo, this, observers, this.alreadyRegistered);
		else {
			
			OnObserverAction observerAction = OnObserverAction.getAction(jo);
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					observerAction.executeAction(observers);
				}
			});
		}	
	}

	public void initGUI(int clientNumPlayer) {
		
		this.alreadyRegistered = true;

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new GUIView(new Controller(Client.this), clientNumPlayer);
			}
		});
	}

	@Override
	public void run() {

		try {
   			this.socket = new Socket(this.iP, this.puerto);
   			this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
   			this.dataOutputStream.flush();
   			this.dataInputStream = new DataInputStream(this.socket.getInputStream());
   			this.sendRegistrationRequest();
   			this.listen();
   		} catch (IOException e) {
   			e.printStackTrace();
   			System.exit(0);
   		}
   	}

	private void sendRegistrationRequest() {
		try {
			this.dataOutputStream.writeUTF(ControllerSerializer.serializeRegister(name).toString());
			this.dataOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void listen() {

		try {
			while (listening) {
				String gameAction;
				gameAction = this.dataInputStream.readUTF();
				if (gameAction != null) {
					JSONObject jo = new JSONObject(new JSONTokener(gameAction));
					this.doViewAction(jo);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
