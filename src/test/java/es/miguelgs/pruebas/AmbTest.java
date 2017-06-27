package es.miguelgs.pruebas;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import rx.Observable;
import es.miguelgs.pruebas.utils.Printer;

/**
 * Se subscribe a los dos Observables, y copia al que primero emita un evento. 
 * En el momento que llega el primer elemento se de-subscribe del otro.
 * 
 * @author Miguel
 */
public class AmbTest {

	public static void main(String[] args) throws InterruptedException {

		Observable.amb(        
				stream(100, 17, "S"),        
				stream(200, 10, "F") 
		).subscribe(System.out::println);
		
		SECONDS.sleep(1);
	}

	private static Observable<String> stream(int initialDelay, int interval, String name) {
		return Observable.interval(initialDelay, interval, MILLISECONDS)
						 .map(x -> name + x)
						 .doOnSubscribe(() -> Printer.printMessage("Subscribe to %s", name))
						 .doOnUnsubscribe(() -> Printer.printMessage("Unsubscribe from %s", name));
	}
}
