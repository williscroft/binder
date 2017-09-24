package org.acm.williscroft.bind;

import java.util.Observable;
import java.util.Observer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Bind an observable to a consumer
 * The consumer will consume <T> from the model.
 * which is applied whenever the {@link Observable} updates.
 * Oh, and in the initial case if the model is non-null.
 * @author tim
 *
 */
public class Binder<T> implements Observer {

	private Consumer<T> applicator;
	private Supplier<T> modelReader;
	
	/**
	 * Bind a model to a view.
	 * @param modelReader a getter on the observable for the field, so that the binder can set the target to start with. 
	 * @param applicator code to run when the observable changes. something like (a)->view.setText(a) 
	 * @param o the observable to observe.
	 */
	public Binder(Supplier<T> modelReader,Consumer<T> applicator,Observable o) {
		this.applicator=applicator;
		this.modelReader = modelReader;
		o.addObserver(this);
		if( modelReader.get()!=null){
			applicator.accept(modelReader.get());
		}
		
	}
	
	@Override
	public void update(Observable o, Object arg) {
		applicator.accept(modelReader.get());
	}

}
