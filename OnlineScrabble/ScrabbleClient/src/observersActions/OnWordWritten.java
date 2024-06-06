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

public class OnWordWritten extends OnObserverAction {

	private String word;
	private int posX;
	private int posY;
	private String direction;
	private int points;
	private int extraPoints;
	private int numPlayers;
	private GamePlayers gamePlayers;
	private int currentTurn;
	private Board board;
	private boolean gameInitiated;
	
	private GamePlayersBuilder gamePlayersBuilder;
	private BoardBuilder boardBuilder;
	
	OnWordWritten() {
		
		super("word_written");
		
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
			
			this.word = data.getString("word");
			this.posX = data.getInt("pos_X");
			this.posY = data.getInt("pos_Y");
			this.direction = data.getString("direction");
			this.points = data.getInt("points");
			this.extraPoints = data.getInt("extra_points");
			this.numPlayers = data.getInt("num_players");
			this.gamePlayers = this.gamePlayersBuilder.createGamePlayers(data.getJSONObject("game_players"));
			this.currentTurn = data.getInt("current_turn");
			this.board = this.boardBuilder.createBoard(data.getJSONObject("game_board"));
			this.gameInitiated = data.getBoolean("game_initiated");
			
			return this;
		}
		
		return null;
	}

	@Override
	public void executeAction(List<ScrabbleObserver> observers) {

		for(ScrabbleObserver o : observers)
			o.onWordWritten(word, posX, posY, direction, points, extraPoints, numPlayers, gamePlayers, currentTurn, board, gameInitiated);
	}
}
