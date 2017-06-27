package es.miguelgs.pruebas;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.Subject;
import es.miguelgs.pruebas.utils.Printer;

/**
 * The Subject class is quite interesting because it extends Observable and implements
 * Observer at the same time. What that means is that you can treat it as Observable on
 * the client side (subscribing to upstream events) and as Observer on the provider side
 * (pushing events downstream on demand by calling onNext() on it). Typically, what
 * you do is keep a reference to Subject internally so that you can push events from any
 * source you like but externally expose this Subject as Observable
 * @author Miguel
 *
 */
public class SubjectTest {

	private static Subject<String, String> observable = //ReplaySubject.create();
														//PublishSubject.create();
														//AsyncSubject.create();
														BehaviorSubject.create();
	
	public static void main(String[] args) {		
		Observable<String> obs = observable.doOnSubscribe(() -> System.out.println("onSubscribe"));
		
		obs.subscribe(System.out::println);
		observable.onNext("Hello");
		observable.onNext("World");
		
		obs.subscribe(val -> Printer.printMessage("Texto %s", val));				
		observable.onNext("Mike");
		
		observable.onCompleted();
	}
}
