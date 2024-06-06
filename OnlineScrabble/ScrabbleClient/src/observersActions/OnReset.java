package observersActions;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

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

public class OnReset extends OnObserverAction {
	
	private Board board;
	private int numPlayers;
	private String currentPlayerName;
	private int remainingTiles;
	private GamePlayers gamePlayers;
	private int currentTurn;
	private boolean gameInitiated;
	
	private BoardBuilder boardBuilder;
	private GamePlayersBuilder gamePlayersBuilder;
	
	OnReset() {
		
		super("reset");

		TileBuilder tileBuilder = new TileBuilder();
		
		List<StrategyBuilder> strategyBuilders = new ArrayList<StrategyBuilder>();
		
		strategyBuilders.add(new EasyStrategyBuilder());
		strategyBuilders.add(new MediumStrategyBuilder());
		strategyBuilders.add(new HardStrategyBuilder());
		strategyBuilders.add(new HumanStrategyBuilder());
		
		this.gamePlayersBuilder = new GamePlayersBuilder(new PlayerBuilder(tileBuilder, strategyBuilders));
		
		this.boardBuilder = new BoardBuilder(new BoxBuilder(tileBuilder));
	}
	
	@Override
	OnObserverAction interpret(JSONObject jo) {

		if(this.type.equals(jo.getString("type"))) {
			
			JSONObject data = jo.getJSONObject("data");
			
			this.board = this.boardBuilder.createBoard(data.getJSONObject("game_board"));
			this.numPlayers = data.getInt("num_players");
			this.currentPlayerName = data.has("current_player_name") ? data.getString("current_player_name") : null;
			this.remainingTiles = data.getInt("remaining_tiles");
			this.gamePlayers = data.has("game_players") ? this.gamePlayersBuilder.createGamePlayers(data.getJSONObject("game_players")) : null;
			this.currentTurn = data.getInt("current_turn");
			this.gameInitiated = data.getBoolean("game_initiated");
			
			return this;
		}
		
		return null;
	}

	@Override
	public void executeAction(List<ScrabbleObserver> observers) {
		
		for(int i = 0; i < observers.size(); ++i)
			observers.get(i).onReset(board, numPlayers, currentPlayerName, remainingTiles, gamePlayers, currentTurn, gameInitiated);
	}
}
