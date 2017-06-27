package es.miguelgs.pruebas;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;
import es.miguelgs.pruebas.utils.Printer;

/**
 * <p>Comparación de las operaciones de concat y merge.</p>
 * <p><strong>CONCAT:</strong> Se ejecuta de manera secuencial los dos observables. Si se termina antes de 
 * llegar al fin del primer observable, ya no se subscribe al segundo.</p>
 * <p><strong>MERGE:</strong> Se mezclan los dos observables, emitiendo valores segun van llegando.</p>
 * 
 * @author Miguel
 */
public class CombiningStreamsTest {

	public static void main(String[] args) throws InterruptedException {
		Observable<Integer> primerosDiezNumeros = Observable.range(1, 10)
															.observeOn(Schedulers.io())
															.doOnSubscribe(() -> Printer.printMessage("primerosDiez.onSubscribe"));
															
		Observable<Integer> segundosDiezNumeros = Observable.range(11, 20)															
															.observeOn(Schedulers.io())
															.doOnSubscribe(() -> Printer.printMessage("segundosDiez.onSubscribe"));
				
		Observable.concat(primerosDiezNumeros, segundosDiezNumeros)
				  .take(10) 
				  .subscribe(val -> Printer.printMessage("concat(%s)", val));
		
		TimeUnit.SECONDS.sleep(2);
		
		Observable.merge(primerosDiezNumeros, segundosDiezNumeros)
				  .take(10)
				  .subscribe(val -> Printer.printMessage("merge(%s)", val));
		
		TimeUnit.SECONDS.sleep(2);
	}
}
