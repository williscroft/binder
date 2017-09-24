package org.acm.williscroft.bind;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

/**
 * Bind an observable to a consumer
 * The consumer will consume <T> from the model.
 * which is applied whenever the {@link Observable} updates.
 * Oh, and in the initial case if the model is non-null.
 * @author tim
 *
 */
public class Binder<T> implements Observer,ActionListener,PropertyChangeListener {

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
	
	/**
	 * Bind a model to a Swing Document
	 * @param modelReader a getter on document, so that the binder can set the target to start with. 
	 * @param applicator code to run when the document changes. something like pojo.setText(doc.getText()) 
	 * @param doc the document to listen to changes on
	 */
	public Binder(Supplier<T> modelReader,Consumer<T> applicator,Document doc) {
		this(modelReader,applicator);
		doc.addDocumentListener(new DocumentListener() {			
			@Override
			public void removeUpdate(DocumentEvent e) {
				update(null,e);
			}			
			@Override
			public void insertUpdate(DocumentEvent e) {
				update(null,e);
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				update(null,e);
			}
		});
		
	}
	
	/**
	 * Bind a model to a Swing Action
	 * @param modelReader a getter on document, so that the binder can set the target to start with. 
	 * @param applicator code to run when the document changes. something like pojo.setText(doc.getText())
	 * 
	 *  add the Binder as an actionListener to the component.
	 *  
	 *  jButton1.addActionListener(new Binder(...,...));
	 */
	public Binder(Supplier<T> modelReader,Consumer<T> applicator) {
		this.applicator=applicator;
		this.modelReader = modelReader;
		
		if( modelReader.get()!=null){
			applicator.accept(modelReader.get());
		}
		
	}
	
	/**
	 * Bind a model to a property change event.
	 * Due to java beans not defining a PropertyChangeProvider interface, this is fallible. 
	 * @param modelReader a getter on document, so that the binder can set the target to start with. 
	 * @param applicator code to run when the document changes. something like pojo.setText(doc.getText())
	 * @param pcp the object that is a Property Change Provider
	 * @param propertyName the name of the property to look for ( null if not required)
	 * @throws Exception if something goes wrong.
	 */
	public Binder(Supplier<T> modelReader,Consumer<T> applicator,Object pcp,String propertyName) throws Exception {
		this(modelReader,applicator);
		if( propertyName==null){
			Method m;
			try {
				m = pcp.getClass().getMethod("addPropertyChangeListener", PropertyChangeListener.class);
				m.invoke(pcp, this);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new Exception("Binder error",e);
			}
		} else {
			Method m;
			try {
				m = pcp.getClass().getMethod("addPropertyChangeListener", PropertyChangeListener.class,String.class);
				m.invoke(pcp, this,propertyName);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new Exception("Binder error",e);
			}
		}
	}
	
	
	
	@Override
	public void update(Observable o, Object arg) {
		applicator.accept(modelReader.get());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		applicator.accept(modelReader.get());		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		applicator.accept(modelReader.get());
	}

}
