package es.miguelgs.pruebas;

import es.miguelgs.pruebas.utils.Printer;
import rx.Observable;

public class StreamSubsetOperatorsTest {

	public static void main(String[] args) {
		Observable<Integer> serieNumerica = Observable.range(1, 10);

		Printer.printMessage("take y skip");
		serieNumerica.skip(2)
				  	 .take(2)
				  	 .subscribe(System.out::println);
		
		Printer.printMessage("takeLast y skipLast");
		serieNumerica.skipLast(2)
					 .takeLast(2)
					 .subscribe(System.out::println);
		
		Printer.printMessage("first y last");
		serieNumerica.first()
					 .subscribe(System.out::println);
		
		Printer.printMessage("takeFirst, takeUntil y takeWhile");
		serieNumerica.takeUntil(num -> num > 5)
					 .subscribe(System.out::println);
		
		Printer.printMessage("elementAt y elementAtOrDefault");
		serieNumerica.elementAtOrDefault(10, -1)
		 			 .subscribe(System.out::println);
		
		Printer.printMessage("count");
		serieNumerica.count()
		 			 .subscribe(System.out::println);
		
		Printer.printMessage("all, exists y contains");
		serieNumerica.all(x -> x < 10)
		 			 .subscribe(System.out::println);
	}
}
