package es.miguelgs.pruebas;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import rx.Observable;
import rx.schedulers.Schedulers;
import es.miguelgs.pruebas.utils.Printer;

/**
 * • Observable without any Scheduler works like a single-threaded program with
 *	 blocking method calls passing data between one another.
 * • Observable with a single subscribeOn() is like starting a big task in the back‐
 *	 ground Thread. The program within that Thread is still sequential, but at least it
 *	 runs in the background.
 * • Observable using flatMap() where each internal Observable has subscribeOn() 
 *	 works like ForkJoinPool from java.util.concurrent, where each substream 
 *	 is a fork of execution and flatMap() is a safe join stage.
 * @author Miguel
 *
 */
public class ImperativeConcurrencyTest {

	public static void main(String[] args) {
		Long timeInit = System.currentTimeMillis();
		int elements = 10;
		Observable<Integer> numbers = deferredGetNumbersSecuenced(elements) 
											.subscribeOn(Schedulers.io()) //  ESTO HACE QUE SE EJECUTEN EN PARALELO EN UN THREAD DISTINTO
											;
		Observable<String> words = deferredGetWordsSecuenced(elements)
											.subscribeOn(Schedulers.io()) //  ESTO HACE QUE SE EJECUTEN EN PARALELO EN UN THREAD DISTINTO
											;
		numbers.zipWith(words, (num, word) -> word + " -> " + num)
			   .toBlocking()
			   .subscribe(System.out::println);
		
		Printer.printMessage("Tiempo de ejecución %s seg", (System.currentTimeMillis() - timeInit) / (float) 1000);
	}
	
	/**
	 * El defer permite una evaluación lazy del Observable, necesario si se quiere ejecutar en paralelo
	 * @param limit
	 * @return
	 */
	private static Observable<Integer> deferredGetNumbersSecuenced(int limit){
		return Observable.defer(() -> getNumbersSecuenced(limit));
	}
	
	private static Observable<Integer> getNumbersSecuenced(int limit){
		System.out.println("Inicio getNumberSecuenced");
		Observable<Integer> obs = Observable.from(IntStream.rangeClosed(1, limit)										
											.boxed()
											.peek(el -> {
												try {
													Thread.sleep(200);
													Printer.printMessage("Se procesa el número: " + el);
												} catch (Exception e) {
													e.printStackTrace();
												}
											})
											.collect(Collectors.toList()));
		System.out.println("Fin getNumberSecuenced");
		return obs;
	}
	
	/**
	 * El defer permite una evaluación lazy del Observable, necesario si se quiere ejecutar en paralelo
	 * @param limit
	 * @return
	 */
	private static Observable<String> deferredGetWordsSecuenced(int limit){
		return Observable.defer(() -> getWordsSecuenced(limit));
	}
	
	private static Observable<String> getWordsSecuenced(int limit){
		System.out.println("Inicio getWordsSecuenced");
		Observable<String> obs = Observable.from(IntStream.rangeClosed(65, 65 + limit - 1)
														  .boxed()
														  .map(el -> String.valueOf((char)el.intValue()))
														  .peek(el -> {
															  try {
																  Thread.sleep(200);
																  Printer.printMessage("Se procesa la letra: " + el);
															  } catch (Exception e) {
																  e.printStackTrace();
															  }
														  })
														  .collect(Collectors.toList()));
		System.out.println("Fin getWordsSecuenced");
		return obs;
	}
}
