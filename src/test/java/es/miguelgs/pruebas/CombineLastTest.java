package es.miguelgs.pruebas;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static rx.Observable.combineLatest;
import static rx.Observable.interval;

/**
 * Cada evento que llega a un observable, se emite el par compuesto por ese valor 
 * y el último valor recibido del otro observable. No para de emitir hasta que terminan
 * los dos observables.
 * 
 * @author Miguel
 */
public class CombineLastTest {

	public static void main(String[] args) throws InterruptedException {
		combineLatest(
				interval(17, MILLISECONDS).map(x -> "S" + x),
				interval(10, MILLISECONDS).map(x -> "F" + x),
				(s, f) -> f + ":" + s
		).forEach(System.out::println);
		
		SECONDS.sleep(5);
	}
}
