package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.json.JSONObject;

import gameActions.GameAction;
import logic.Game;
import logic.GameSerializer;
import storage.GameLoader;

public class Server extends Thread {

	private Game game;

	private ServerSocket serverSocket;

	private int port;

	private List<ClientThread> clients;

	private int numHumanPlayers;
	private int numAutomaticPlayers;

	public Server(int numHumanPlayers, int numAutomaticPlayers, String strategy1, String strategy2, int port) {
		this.game = new Game(this);
		this.port = port;
		this.clients = new ArrayList<ClientThread>();
		this.numHumanPlayers = numHumanPlayers;
		this.numAutomaticPlayers = numAutomaticPlayers;
		
		try {
			GameLoader.newGame(this.game);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		if(strategy1 != null)
			this.game.addAutomaticPlayer(strategy1);
		if(strategy2 != null)
			this.game.addAutomaticPlayer(strategy2);
	}

	public void doGameAction(JSONObject jo) {
		
		GameAction gameAction = GameAction.getAction(jo);

		try {
			gameAction.executeAction(game);
		} catch (FileNotFoundException e) {
			this.sendViewAction(GameSerializer.serializeError(e.getMessage(), game.getCurrentTurn()));
		}
	}

	public void sendViewAction(JSONObject jo) {
		for(ClientThread ct : this.clients)
			ct.sendViewAction(jo);
	}

	@Override
	public void run() {

		try {
			this.serverSocket = new ServerSocket(this.port);
			
			while (this.clients.size() < this.numHumanPlayers) {
				
				Socket socket;

				socket = serverSocket.accept();

				ClientThread client = new ClientThread(socket, this);
				this.clients.add(client);
				client.start();
			}
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					game.decideFirstTurn();
				}
			});
			
			
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public int getNumHumanPlayers() {
		return this.numHumanPlayers;
	}
	
	public int getNumAutomaticPlayers() {
		return this.numAutomaticPlayers;
	}
}
