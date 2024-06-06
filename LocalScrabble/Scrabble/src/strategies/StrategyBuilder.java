package strategies;

import org.json.JSONObject;

/* APUNTES GENERALES
  
   Esta clase reune la funcionalidad común de cualquier Builder de strategias.
 */
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