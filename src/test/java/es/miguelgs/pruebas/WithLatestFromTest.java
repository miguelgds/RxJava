package es.miguelgs.pruebas;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static rx.Observable.interval;
import rx.Observable;

public class WithLatestFromTest {

	public static void main(String[] args) throws InterruptedException {
		Observable<String> fast = interval(10, MILLISECONDS)
									.map(x -> "F" + x)
									.delay(100, MILLISECONDS)
									.startWith("FX"); // EVITA QUE SE  PIERDAN VALORES DEL slow, YA QUE NO RECIBE EVENTOS HASTA PASADOS 100MS
		Observable<String> slow = interval(17, MILLISECONDS)
									.map(x -> "S" + x);
		slow.withLatestFrom(fast, (s, f) -> s + ":" + f)
			.forEach(System.out::println);

		SECONDS.sleep(10);
	}
}
