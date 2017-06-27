package es.miguelgs.pruebas;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import es.miguelgs.pruebas.utils.Printer;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Believe it or not, concurrency in RxJava can be described by two operators: the afor‐
 * mentioned subscribeOn() and observeOn()
 * 
 * subscribeOn() allows choosing which Scheduler will be used to invoke OnSubscribe
 * 				 (lambda expression inside create()). Therefore, any code inside create() is pushed
 * 				 to a different thread for example, to avoid blocking the main thread.
 * observeOn() controls which Scheduler is used to invoke downstream Subscribers
 * 			   occurring after observeOn()
 * 
 * subscribeOn() and observeOn() work really well together when you want to physi‐
 * cally decouple producer (Observable.create()) and consumer (Subscriber). By
 * default, there is no such decoupling, and RxJava simply uses the same thread
 * 
 * @author Miguel
 *
 */
public class ConcurrencyTest {

	public static void main(String[] args) throws InterruptedException {
		Printer.printMessage("Inicio main");
		Observable//.defer(() -> Observable.from(heavyProcess()))
				  .<SomeDTO>create(subscriber -> {
					  heavyProcess().stream().forEach(subscriber::onNext);
					  subscriber.onCompleted();
				  })
				  //.from(heavyProcess()) SI SE HACE ASI, SE EJECUTA EL heavyProcess ANTES DE LA SUBSCRIPCION, POR LO QUE DEJA DE SER LAZY
				  .observeOn(Schedulers.io())
				  .doOnNext(val -> Printer.printMessage("Paso 1(%s)", val))
				  .map(dto -> String.format("Id : %d | Nombre : %s", dto.getId(), dto.getName()))
				  .observeOn(Schedulers.io())
				  .doOnNext(val -> Printer.printMessage("Paso 2(%s)", val))
				  .doOnSubscribe(() -> Printer.printMessage("Observable.onSubscribe"))
				  .subscribeOn(Schedulers.io())
				  //.toBlocking() // ESTO HACE QUE EL Subscriber.onNext se ejecute en el main Thread
				  .subscribe(val -> Printer.printMessage("Subscriber.onNext(%s)", val));		
		Printer.printMessage("Fin main");
		
		TimeUnit.SECONDS.sleep(10);
	}
	
	private static List<SomeDTO> heavyProcess(){
		Printer.printMessage("Inicio heavyProcess");
		List<SomeDTO> result = IntStream.range(0, 10)
					    .boxed()
						.peek(val -> {
							try {
								TimeUnit.MILLISECONDS.sleep(val * 100);
							} catch (Exception e) {
								e.printStackTrace();
							}
						})
						.map(index -> new SomeDTO(index, "Name " + index))
						.peek(dto -> Printer.printMessage(dto.toString()))
						.collect(Collectors.toList());
		Printer.printMessage("Fin heavyProcess");
		return result;
	}
	
	public static class SomeDTO{
		private Integer id;
		private String name;
						
		public SomeDTO(Integer id, String name) {
			super();
			this.id = id;
			this.name = name;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		@Override
		public String toString() {
			return "SomeDTO [id=" + id + ", name=" + name + "]";
		}				
	}
}
