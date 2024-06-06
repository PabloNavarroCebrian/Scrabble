package strategies;

import org.json.JSONObject;

// Ver apuntes de la clase padre StrategyBuilder.
public class HumanStrategyBuilder extends StrategyBuilder {

	public HumanStrategyBuilder() {
		super("human_strategy");
	}

	@Override
	protected Strategy createTheStrategy(JSONObject data) {
		
		if(data.getString("strategy_type").equalsIgnoreCase(_type))
			return new HumanStrategy();
		
		return null;
	}
}
