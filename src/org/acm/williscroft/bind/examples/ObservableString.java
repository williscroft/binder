package org.acm.williscroft.bind.examples;

import java.util.Observable;;

/**
 * A tiny POJO
 * (Plain-Old-Java-Object)
 * that's Observable.
 * That's all it is.
 * 
 * @author tim
 *
 */
public class ObservableString extends Observable{

	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		setChanged();
		notifyObservers();
	}
}
