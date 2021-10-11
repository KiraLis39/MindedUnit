package core.Events;

import core.interfaces.Collable.CollableType;
import unit.Unit;


public class ColEvent {
	CollableType coType;
	Object source;
	
	public ColEvent(Object source) {
		this.coType = ((Unit) source).getType();
		this.source = source;
	}

	public CollableType collisionType() {return coType;}

	public Object getColladeObject() {return source;}
}
