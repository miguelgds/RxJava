package es.miguelgs.pruebas;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static rx.Observable.combineLatest;
import static rx.Observable.interval;

public class CombineLastTest {

	public static void main(String[] args) throws InterruptedException {
		combineLatest(
				interval(17, MILLISECONDS).map(x -> "S" + x),
				interval(10, MILLISECONDS).map(x -> "F" + x),
				(s, f) -> f + ":" + s
		).forEach(System.out::println);
		
		SECONDS.sleep(30);
	}
}
