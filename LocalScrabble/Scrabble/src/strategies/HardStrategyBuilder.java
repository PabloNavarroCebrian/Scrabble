package strategies;

import org.json.JSONObject;

// Ver apuntes de la clase padre StrategyBuilder.
public class HardStrategyBuilder extends StrategyBuilder {
	
	public HardStrategyBuilder() {
		super("hard_strategy");
	}

	@Override
	protected Strategy createTheStrategy(JSONObject data) {
		
		if(data.getString("strategy_type").equalsIgnoreCase(_type))
			return new HardStrategy();
		
		return null;
	}

}
