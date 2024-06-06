package strategies;

import org.json.JSONObject;

// Ver apuntes de la clase padre StrategyBuilder.
public class MediumStrategyBuilder extends StrategyBuilder {
	
	public MediumStrategyBuilder() {
		super("medium_strategy");
	}

	@Override
	protected Strategy createTheStrategy(JSONObject data) {
		
		if(data.getString("strategy_type").equalsIgnoreCase(_type))
			return new MediumStrategy();
		
		return null;
	}

}
