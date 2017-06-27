package es.miguelgs.pruebas;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;
import es.miguelgs.pruebas.utils.Printer;

public class CombiningStreamsTest {

	public static void main(String[] args) throws InterruptedException {
		Observable<Integer> primerosDiezNumeros = Observable.range(1, 10)
															.observeOn(Schedulers.io())
															.doOnSubscribe(() -> Printer.printMessage("primerosDiez.onSubscribe"));
															
		Observable<Integer> segundosDiezNumeros = Observable.range(11, 20)															
															.observeOn(Schedulers.io())
															.doOnSubscribe(() -> Printer.printMessage("segundosDiez.onSubscribe"));
				
		Observable.concat(primerosDiezNumeros, segundosDiezNumeros)
				  .take(10) // SI SE TERMINA ESTE OBSERVABLE ANTES DE LLEGAR AL FIN DEL PRIMERO, NO SE LLEGA A SUBSCRIBIR AL SEGUNDO
				  .subscribe(val -> Printer.printMessage("concat(%s)", val));
		
		TimeUnit.SECONDS.sleep(2);
		
		Observable.merge(primerosDiezNumeros, segundosDiezNumeros)
				  .take(10)
				  .subscribe(val -> Printer.printMessage("merge(%s)", val));
		
		TimeUnit.SECONDS.sleep(2);
	}
}
