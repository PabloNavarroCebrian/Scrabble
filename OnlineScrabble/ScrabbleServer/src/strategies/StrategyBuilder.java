package strategies;

import org.json.JSONObject;

public abstract class StrategyBuilder {
	
	protected String _type;
	
	StrategyBuilder(String type) {
		if (type == null)
			throw new IllegalArgumentException("Invalid type: " + type);
		
		else _type = type;
	}

	public Strategy createStrategy(JSONObject data) {
		return createTheStrategy(data);
	}
	
	protected abstract Strategy createTheStrategy(JSONObject data);
}