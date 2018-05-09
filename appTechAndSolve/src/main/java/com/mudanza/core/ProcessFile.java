package com.mudanza.core;

import java.io.File;
import java.util.ArrayList;
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
	 * 
	 * @param weightsComplete Lista que contiene la cantidad de elementos a cargar 
	 * con el respectivo peso para cada elemento
	 * @param i Indice que indica la posición de la cantidad de elemento a procesar
	 * @return
	 */
	private List<Integer> validateWeightsForIteration(List<Integer> weightsComplete, int i) {
		List<Integer> weightsIteration = weightsComplete.subList(i + 1, weightsComplete.get(i) + i + 1);
		for (Integer weightCurrent : weightsIteration) {
			if (1 > weightCurrent || 100 < weightCurrent) {
				throw new PreconditionException(Constants.WEIGHTSOUTOFPARAMETERS);
			}
		}
		return weightsIteration;
	}

	

	private int trips(List<Integer> weights) {
		List<Integer> descendingWeights = Utils.descendingOrder(weights);
		int elementsInPackage = 0;
		int totalElements = weights.size();
		int totalTrips = 0;
		for (int i = 0; i <= descendingWeights.size(); i++) {

			if (descendingWeights.get(i) >= 50) {
				totalTrips ++;
				totalElements--;
			} else {
				if (elementsInPackage <= totalElements) {
					elementsInPackage = elementsInPackageOfCurrentTrip(descendingWeights.get(i), totalElements);
					totalElements = totalElements - elementsInPackage;
					totalTrips ++;
				} else {
					break;
				}
			}
		}
		return totalTrips;
	}
	
	private int elementsInPackageOfCurrentTrip(int weightOfTheFirstElement, int totalElements) {
		int elements = 2;
		while (elements <= totalElements) {
			if ((weightOfTheFirstElement * elements) >= 50) {
				break;
			} else {
				elements++;
			}
		}
		return elements;
	}

}
