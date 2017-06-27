package es.miguelgs.pruebas;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import es.miguelgs.pruebas.utils.Printer;

public class MergeTest {

	private static Long tiempoInicio = System.currentTimeMillis();
	
	/**
	 * <p> Si se utiliza el método <strong>toBlocking()</strong> el merge() se ejecuta en el <strong>Thread del main</strong> </p>
	 * <p> Si se utiliza un <strong>sleep()</strong> para esperar por la ejecución, el merge() se ejecuta en el <strong>Thread del Observable que le provee cada evento</strong> </p>
	 */
	public static void main(String[] args) throws InterruptedException {
		
		Printer.printMessage("Inicio Main");
		
		Observable<String> interval = Observable.interval(2, TimeUnit.SECONDS)											 
											  .take(15, TimeUnit.SECONDS)
											  .doOnNext(v -> Printer.printMessage("Interval.doOnNext(%s)", v))
											  .map(v -> Printer.getMessage("Interval.map(%s)", v));
		
		Observable<String> random = Observable.interval(700, TimeUnit.MILLISECONDS)
											   .take(20, TimeUnit.SECONDS)
											   .map(v -> Math.random() * 100)
											   .doOnNext(v -> Printer.printMessage("Random.doOnNext(%s)", v))
											   .map(v -> Printer.getMessage("Random.map(%s)", v));
		
		Observable.merge(interval, random)
				  //.toBlocking()
				  .subscribe(val -> Printer.printMessage("%s : Merge.subscribe(%s)", getTimeElapsed(), val));	
		
		TimeUnit.SECONDS.sleep(20);
		
		Printer.printMessage("Fin Main");
	}
	
	private static Float getTimeElapsed(){
		return (System.currentTimeMillis() - tiempoInicio) / (float)1000;
	}
}
