package observersActions;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import containers.GamePlayers;
import containers.GamePlayersBuilder;
import simulatedObjects.PlayerBuilder;
import simulatedObjects.TileBuilder;
import strategies.EasyStrategyBuilder;
import strategies.HardStrategyBuilder;
import strategies.HumanStrategyBuilder;
import strategies.MediumStrategyBuilder;
import strategies.StrategyBuilder;
import view.ScrabbleObserver;

public class OnUpdate extends OnObserverAction {

	private boolean gameFinished;
	private int numPlayers;
	private int remainingTiles;
	private String currentPlayerName;
	private GamePlayers gamePlayers;
	private int currentTurn;
	private boolean gameInitiated;
	
	private GamePlayersBuilder gamePlayersBuilder;
	
	OnUpdate() {
		
		super("update");

		List<StrategyBuilder> strategyBuilders = new ArrayList<StrategyBuilder>();
		
		strategyBuilders.add(new EasyStrategyBuilder());
		strategyBuilders.add(new MediumStrategyBuilder());
		strategyBuilders.add(new HardStrategyBuilder());
		strategyBuilders.add(new HumanStrategyBuilder());
		
		this.gamePlayersBuilder = new GamePlayersBuilder(new PlayerBuilder(new TileBuilder(), strategyBuilders));
	}

	@Override
	OnObserverAction interpret(JSONObject jo) {
		
		if(this.type.equals(jo.getString("type"))) {
			
			JSONObject data = jo.getJSONObject("data");
			
			this.gameFinished = data.getBoolean("game_finished");
			this.numPlayers = data.getInt("num_players");
			this.remainingTiles = data.getInt("remaining_tiles");
			this.currentPlayerName = data.getString("current_player_name");
			this.gamePlayers = this.gamePlayersBuilder.createGamePlayers(data.getJSONObject("game_players"));
			this.currentTurn = data.getInt("current_turn");
			this.gameInitiated = data.getBoolean("game_initiated");
			
			return this;
		}
		
		return null;
	}

	@Override
	public void executeAction(List<ScrabbleObserver> observers) {

		for(ScrabbleObserver o : observers)
			o.onUpdate(gameFinished, numPlayers, remainingTiles, currentPlayerName, gamePlayers, currentTurn, gameInitiated);
		
	}
}
