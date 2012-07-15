package mereditor.modelo.validacion;

import mereditor.modelo.base.Componente;

public interface Validacion {
	public static final int MAX_COMPONENTES = 7;
	public static final int MAX_ATRIBUTOS = 7;
	public static final int MAX_DESVIACION_COMPONENTES = 2;
	public static final int MAX_DESVIACION_ATRIBUTOS = 2;
	
	public String validar(Componente componente); 
}
