package observersActions;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
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

public class OnFirstTurnDecided extends OnObserverAction {
	
	private List<String> lettersObtained;
	private GamePlayers gamePlayers;
	private int numPlayers;
	private int currentTurn;
	private boolean gameInitiated;
	
	private GamePlayersBuilder gamePlayersBuilder;
	
	OnFirstTurnDecided() {
		
		super("first_turn_decided");

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
			
			JSONArray jsonArrayWords = data.getJSONArray("letters_obtained");
			
			this.lettersObtained = new ArrayList<String>();
			
			for(int i = 0; i < jsonArrayWords.length(); i++)
				this.lettersObtained.add(jsonArrayWords.getString(i));
			
			this.gamePlayers = this.gamePlayersBuilder.createGamePlayers(data.getJSONObject("game_players"));
			this.numPlayers = data.getInt("num_players");
			this.currentTurn = data.getInt("current_turn");
			this.gameInitiated = data.getBoolean("game_initiated");
			
			return this;
		}
		
		return null;
	}

	@Override
	public void executeAction(List<ScrabbleObserver> observers) {

		for(ScrabbleObserver o : observers)
			o.onFirstTurnDecided(lettersObtained, gamePlayers, numPlayers, currentTurn, gameInitiated);
	}
	

}
