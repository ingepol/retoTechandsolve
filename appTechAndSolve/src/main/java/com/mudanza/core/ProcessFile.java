package com.mudanza.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mudanza.configuration.StorageProperties;
import com.mudanza.exception.PreconditionException;
import com.mudanza.utils.Constants;
import com.mudanza.utils.Utils;

/**
 * Componente que contiene la lógica y reglas del negocio.
 * 
 * Validar el número de días a trabjar 1 ≤​ T​ ≤ 500 
 * Validar cantidad de elementos a transportar por día 1 ≤ ​N​ ≤ 100 
 * Validar el peso de cada elemento a transportar 1 ≤ ​Wi​ ≤ 100  
 * 
 * Determinar el máximo de viajes que puede realizar un trabajador,
 * teniendo encuenta que como mínimo debe transportar en cada viaje
 * un total de 50 libras.
 * 
 * @author Paul Andrés Arenas Cardona
 * @version 1.0 
 * 
 * Fecha de creación 2018-05-07
 *
 */

@Component
public class ProcessFile {
	
	private final String rootLocation;

	@Autowired
	public ProcessFile(StorageProperties properties) {
		this.rootLocation = properties.getFilePathOut();

	}
	
	public boolean generateResponse(File file)  {
		return calculateTripsByEachDay(Utils.listOfIntegersToProcess(file));
	}

	/**
	 * Calcular la cantidad de viajes a realizar cada día, validando 
	 * la regla de negocio correspondiente a los días de trabajos
	 * 
	 * @param list Lista con los datos de entrada a procesar
	 * @return Verdadero si el proceso fue exitoso
	 * @exception PreconditionException si la lista es nula o vacía.  
	 * 				También si los días de trabajo no cumple la regla 
	 * 				de negocio 1 ≤​ T​ ≤ 500.
	 */
	public boolean calculateTripsByEachDay(List<Integer> list) {
		if (list != null && !list.isEmpty()) {
			int daysToWork = list.get(0);

			if (1 <= daysToWork && daysToWork <= 500) {
				list.remove(0);
				
				List<Integer> listResult = splitWeightsByDay(list);

				if (listResult.isEmpty()) {
					return false;
				}
				
				Utils.linesToWrite(this.rootLocation, listResult);
			} else {
				throw new PreconditionException(Constants.WORKEDAYSNOTVALID);
			}
			return true;
		} else {
			throw new PreconditionException(Constants.LISTNULLOREMPTY);
		}
	}

	/**
	 * Procesa la cantidad del elemento de cada día, validando la regla de negocio
	 * correspondiente a transportar la cantidad elementos permitida.
	 * 
	 * @param weightsComplete Lista que contiene la cantidad de elementos a cargar 
	 * con el respectivo peso para cada elemento
	 * 			
	 * @return Lista de enteros indicando la cantidad máxima de viajes 
	 * que realizó cada día
	 * @exception PreconditionException si la cantidad de elementos a transportar
	 * 				no cumple con la regla de negocio 1 ≤ ​N​ ≤ 100.
	 */
	public List<Integer> splitWeightsByDay(List<Integer> weightsComplete) {
		List<Integer> listOfResults = new ArrayList<>();
		int index = 0;
		while( index < weightsComplete.size() && weightsComplete.size()>1) {
			if (1 <= weightsComplete.get(index) && weightsComplete.get(index) <= 100) {
				List<Integer> weightsIteration = validateWeightsForIteration(weightsComplete, index);
				listOfResults.add(trips(weightsIteration));
				index = weightsComplete.get(index) + 1 + index;
			} else {
				throw new PreconditionException(Constants.ELEMENTSOUTOFPARAMETERS);
			}
		}
		return listOfResults;
	}
	
	/**
	 * Extrae de la lista la cantidad de elementos a transportar, indicando la
	 * posición inicial (index + 1) y final (Cantidad de elementos a transportar 
	 * + posición inicial).
	 * 
	 * También valida la regla de negocio correspondiente a los pesos del elemento
	 * transportado.
	 * 
	 * @param weightsComplete Lista que contiene la cantidad de elementos a cargar 
	 * con el respectivo peso para cada elemento
	 * @param index Indice que tiene la posición del día e indica la cantidad de elemento 
	 * 				a transportar
	 * @return Lista con los elementos a transportar indicando su respectivo peso.
	 * @exception PreconditionException si el peso del elemento a transportar
	 * 				no cumple con la regla de negocio 1 ≤ ​Wi​ ≤ 100.
	 */
	private List<Integer> validateWeightsForIteration(List<Integer> weightsComplete, int index) {
		List<Integer> weightsIteration = weightsComplete.subList(index + 1, weightsComplete.get(index) + index + 1);
		for (Integer weightCurrent : weightsIteration) {
			if (1 > weightCurrent || 100 < weightCurrent) {
				throw new PreconditionException(Constants.WEIGHTSOUTOFPARAMETERS);
			}
		}
		return weightsIteration;
	}

	
	/**
	 * Calcula la cantidad máxima de viajes a realizar teniendo en cuenta que
	 * si un elemento pesa más de 50 libras suma un viaje, de lo contrario
	 * se deberá ir acumulando la cantidad de elementos necesario al viaje con 
	 * el próposito que por lo menos pese 50 libras. 
	 * 
	 * @param weights Lista que indica el peso de cada elemento a transportar.
	 * @return totalTrips Cantidad máxima de viajes realizados.
	 */
	private int trips(List<Integer> weights) {
		int elementsInPackage = 0;
		int totalElements = weights.size();
		int totalTrips = 0;
		Collections.sort(weights,Collections.reverseOrder() );
		int index = 0;		
		while(totalElements > 0){
			elementsInPackage = elementsInPackageOfCurrentTrip(weights.get(index), totalElements);
			totalElements -= elementsInPackage;
			totalTrips ++;
			index += elementsInPackage;
		}
		return totalTrips;
	}
	
	/**
	 * Cuando el primer elemento tiene un peso menor a 50 libras se valida si es posible
	 * adicionar un elemento más al viaje sin ir a sobrepasar las 50 libras
	 * 
	 * Consideración - Se asume que cada elemento pesa al menos tanto como el elemento 
	 * 					que está en la parte superior.
	 * 
	 * @param weightOfTheFirstElement  Peso del primer elemento, el cual tendrá un peso
	 *  							mayor o igual a los demás que hagan parte del viaje 
	 * @param totalElements  Total de elementos a transportar en un día.
	 * @return elements Total de elementos transportados en un viaje.
	 */
	private int elementsInPackageOfCurrentTrip(int weightOfTheFirstElement, int totalElements) {
		int elements = 1;
		do{
			if ((weightOfTheFirstElement * elements) >= 50 || totalElements == 1) {
				break;
			} else {
				elements++;
			}
		}while (elements < totalElements); 
		return elements;
	}

}
