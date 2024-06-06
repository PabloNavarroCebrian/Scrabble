package strategies;

import org.json.JSONObject;

public class EasyStrategyBuilder extends StrategyBuilder {
	
	public EasyStrategyBuilder() {
		super("easy_strategy");
	}

	@Override
	protected Strategy createTheStrategy(JSONObject data) {
		
		if(data.getString("strategy_type").equalsIgnoreCase(_type))
			return new EasyStrategy();
		
		return null;
	}

}
