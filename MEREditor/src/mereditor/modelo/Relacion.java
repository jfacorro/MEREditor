package mereditor.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mereditor.modelo.base.Componente;
import mereditor.modelo.base.ComponenteNombre;

public class Relacion extends ComponenteNombre {
	public enum TipoRelacion {
		ASOCIACION, COMPOSICION
	}

	protected TipoRelacion tipo = TipoRelacion.ASOCIACION;
	protected Set<Atributo> atributos = new HashSet<>();
	protected Set<EntidadRelacion> participantes = new HashSet<>();

	public Relacion() {
		super();
	}

	public Relacion(String nombre) {
		super(nombre);
	}

	public Relacion(String nombre, String id, TipoRelacion tipo) {
		super(nombre, id);
		this.tipo = tipo;
	}

	public void addAtributo(Componente atributo) {
		atributos.add((Atributo) atributo);
	}

	public void addParticipante(EntidadRelacion participante) {
		participantes.add(participante);
	}

	/*
	 * Getter y setters
	 */
	public TipoRelacion getTipo() {
		return tipo;
	}

	public void setTipo(TipoRelacion tipo) {
		this.tipo = tipo;
	}

	public Set<EntidadRelacion> getParticipantes() {
		return Collections.unmodifiableSet(participantes);
	}
	
	public List<Entidad> getEntidadesParticipantes() {
		List<Entidad> entidades = new ArrayList<>();

		for(EntidadRelacion entidadRelacion : this.participantes)
			entidades.add(entidadRelacion.getEntidad());

		return entidades;
	}

	public Set<Atributo> getAtributos() {
		return Collections.unmodifiableSet(atributos);
	}

	@Override
	public boolean contiene(Componente componente) {
		boolean contiene = this.atributos.contains(componente);
		
		if (contiene)
			return contiene;
		
		// Verificar los hijos de los atributos
		for (Componente hijo : this.atributos) {
			contiene = hijo.contiene(componente);
			if (contiene)
				return contiene;
		}
		
		return super.contiene(componente);
	}

	/*
	 * Contiene la entidad que pertence a la relacion y su informacion asociada
	 * a la misma.
	 */
	public class EntidadRelacion {
		protected Entidad entidad;
		protected String rol;
		protected String cardinalidadMinima = "1";
		protected String cardinalidadMaxima = "1";
		protected Relacion relacion; 

		public EntidadRelacion(Relacion relacion, Entidad entidad, String rol, String cardinalidadMinima, String cardinalidadMaxima) {
			this.relacion = relacion;
			this.entidad = entidad;
			this.rol = rol;
			this.cardinalidadMinima = cardinalidadMinima;
			this.cardinalidadMaxima = cardinalidadMaxima;
		}

		public Entidad getEntidad() {
			return entidad;
		}

		public void setEntidad(Entidad entidad) {
			this.entidad = entidad;
		}

		public String getRol() {
			return rol;
		}

		public void setRol(String rol) {
			this.rol = rol;
		}

		public String getCardinalidadMinima() {
			return cardinalidadMinima;
		}

		public String getCardinalidadMaxima() {
			return cardinalidadMaxima;
		}

		public void setCardinalidadMinima(String cardinalidadMinima) {
			this.cardinalidadMinima = cardinalidadMinima;
		}

		public void setCardinalidadMaxima(String cardinalidadMaxima) {
			this.cardinalidadMaxima = cardinalidadMaxima;
		}

		@Override
		public String toString() {
			String label = "";
			if (!this.cardinalidadMinima.equals("1") || !this.cardinalidadMaxima.equals("1"))
				label = "(" + this.cardinalidadMinima + ", " + this.cardinalidadMaxima + ")";

			if (this.rol != null)
				label += " " + this.rol;

			return label;
		}

		public Relacion getRelacion() {
			return this.relacion;
		}
	}
}
