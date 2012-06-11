package mereditor.xml;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mereditor.control.Control;
import mereditor.control.Proyecto;
import mereditor.modelo.Diagrama;
import mereditor.modelo.base.Componente;
import mereditor.representacion.PList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

class RepresentacionParserXml extends ParserXml {

	public RepresentacionParserXml(Proyecto proyecto, String path) throws Exception {
		super();
		File source = new File(path);
		this.root = docBuilder.parse(source).getDocumentElement();
		this.proyecto = proyecto;
	}

	public RepresentacionParserXml(Proyecto proyecto) throws Exception {
		this.proyecto = proyecto;
	}

	/**
	 * Recorre la coleccion de componentes parseados y busca sus
	 * representaciones para cada diagrama en el que est�n presentes.
	 */
	public void parsearRepresentacion() {
		// Para cada componente del proyecto
		for (Componente componente : this.proyecto.getComponentes()) {
			// Buscar las representaciones en cada diagrama
			Map<String, PList> representaciones = this.obtenerRepresentaciones(componente.getId());
			// Asignarselas a las figura correspondiente de cada diagrama
			Control<?> control = (Control<?>) componente;
			for (String idDiagrama : representaciones.keySet())
				control.getFigura(idDiagrama).setRepresentacion(representaciones.get(idDiagrama));
		}
	}

	/**
	 * Devuelve un mapa con las representaciones del componente por cada
	 * diagrama en el que se encuentra.
	 * 
	 * @param id
	 *            Id del componente
	 * @return Mapa con los ids de los diagramas como clave y las
	 *         representaciones como valor.
	 */
	public Map<String, PList> obtenerRepresentaciones(String id) {
		HashMap<String, PList> representaciones = new HashMap<>();

		// Buscar todas las representaciones para el id
		String query = String.format(Constants.REPRESENTACION_ID_QUERY, id);
		List<Element> representacionesXml = XmlHelper.query(this.root, query);

		for (Element representacionXml : representacionesXml) {
			Element diagramaXml = XmlHelper.querySingle(representacionXml, Constants.DIAGRAMA_PADRE_QUERY);

			String idDiagrama = this.obtenerId(diagramaXml);
			representaciones.put(idDiagrama, this.obtenerRepresentacion(representacionXml));
		}

		return representaciones;
	}

	/**
	 * Parsea un elemento de representaci�n b�sico con posici�n y dimensi�n.
	 * 
	 * @param elemento
	 * @return
	 */
	protected PList obtenerRepresentacion(Element elemento) {
		PList representacion = new PList();
		for (Element element : XmlHelper.query(elemento, "./*")) {
			PList hijo = this.obtenerRepresentacion(element);
			representacion.set(element.getNodeName(), hijo);
		}
		for (String nombre : XmlHelper.attributeNames(elemento)) {
			representacion.set(nombre, elemento.getAttribute(nombre));
		}
		return representacion;
	}

	public Document generarXml() {
		Document doc = this.docBuilder.newDocument();
		this.root = doc.createElement(Constants.PROYECTO_TAG);
		doc.appendChild(this.root);

		this.generarDiagramaXml(this.root, this.proyecto.getRaiz());

		return doc;
	}

	protected void generarDiagramaXml(Element elemento, Diagrama diagrama) {
		Element diagramaElem = this.agregarElemento(elemento, Constants.DIAGRAMA_TAG);
		this.agregarAtributo(diagramaElem, Constants.ID_ATTR, diagrama.getId());

		for (Componente componente : diagrama.getComponentes()) {
			Control<?> control = (Control<?>) componente;
			PList plist = control.getFigura(diagrama.getId()).getRepresentacion();
			if (plist != null) {
				Element reprElement = this.agregarElemento(diagramaElem, Constants.REPRESENTACION_TAG);
				this.agregarAtributo(reprElement, Constants.ID_ATTR, componente.getId());

				this.agregarRepresentacion(reprElement, plist);
			}
		}

		for (Diagrama diagramaHijo : diagrama.getDiagramas()) {
			this.generarDiagramaXml(elemento, diagramaHijo);
		}
	}

	private void agregarRepresentacion(Element elemento, PList repr) {
		for (String nombre : repr.getNames()) {
			Object valor = repr.get(nombre);

			if (valor instanceof PList) {
				Element elemHijo = this.agregarElemento(elemento, nombre);
				this.agregarRepresentacion(elemHijo, (PList) valor);
			} else {
				this.agregarAtributo(elemento, nombre, valor.toString());
			}
		}
	}
}