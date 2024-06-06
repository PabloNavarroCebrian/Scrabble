package observersActions;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.json.JSONObject;

import client.Client;
import containers.Board;
import containers.BoardBuilder;
import containers.GamePlayers;
import containers.GamePlayersBuilder;
import simulatedObjects.BoxBuilder;
import simulatedObjects.PlayerBuilder;
import simulatedObjects.TileBuilder;
import strategies.EasyStrategyBuilder;
import strategies.HardStrategyBuilder;
import strategies.HumanStrategyBuilder;
import strategies.MediumStrategyBuilder;
import strategies.StrategyBuilder;
import view.ScrabbleObserver;

public class OnRegister {
	
	private GamePlayersBuilder gamePlayersBuilder;
	private BoardBuilder boardBuilder;
	
	public OnRegister() {

		TileBuilder tileBuilder = new TileBuilder();
		
		List<StrategyBuilder> strategyBuilders = new ArrayList<StrategyBuilder>();
		
		strategyBuilders.add(new EasyStrategyBuilder());
		strategyBuilders.add(new MediumStrategyBuilder());
		strategyBuilders.add(new HardStrategyBuilder());
		strategyBuilders.add(new HumanStrategyBuilder());
		
		this.gamePlayersBuilder = new GamePlayersBuilder(new PlayerBuilder(tileBuilder, strategyBuilders));
		
		this.boardBuilder = new BoardBuilder(new BoxBuilder(tileBuilder));
	}


	public void register(JSONObject jo, Client client, List<ScrabbleObserver> observers, boolean alreadyRegistered) {
		
		JSONObject data = jo.getJSONObject("data");
		
		Board board = data.has("game_board") ? this.boardBuilder.createBoard(data.getJSONObject("game_board")) : null;
		int numPlayers = data.getInt("num_players");
		GamePlayers gamePlayers = data.has("game_players") ? this.gamePlayersBuilder.createGamePlayers(data.getJSONObject("game_players")) : null;
		int currentTurn = data.getInt("current_turn");
		boolean gameInitiated = data.getBoolean("game_initiated");
		
		if(!alreadyRegistered)
			client.initGUI(numPlayers - 1);
		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				for(int i = 0; i < observers.size(); ++i)
					observers.get(i).onRegister(board, numPlayers, gamePlayers, currentTurn, gameInitiated);
			}
			
		});
		}
}
