package mereditor.interfaz.swt.figuras;

import mereditor.modelo.Atributo;
import mereditor.modelo.Entidad;
import mereditor.modelo.Relacion;
import mereditor.representacion.PList;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolygonShape;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;

public class RelacionFigure extends Figura<Relacion> {
	private PolygonShape rombo;

	public RelacionFigure(Relacion relacion, Dimension dim) {
		super(relacion, dim);
	}

	public RelacionFigure(Relacion relacion) {
		super(relacion);
	}

	@Override
	protected void init() {
		super.init();
		this.setBackgroundColor(null);
		this.setBorder(null);
		this.setOpaque(false);

		// Quitar todos los controles hijos
		this.removeAll();

		this.rombo = new PolygonShape();
		this.rombo.setAntialias(SWT.ON);
		this.rombo.setLayoutManager(new BorderLayout());
		this.rombo.setBackgroundColor(this.getBackColor());
		this.rombo.setOpaque(false);
		this.rombo.add(this.lblName, BorderLayout.CENTER);
		this.add(this.rombo, BorderLayout.CENTER);
		this.generarPuntos();

		this.lblName.setText(this.componente.getNombre());
	}

	/**
	 * Calcula y genera los puntos para formar el rombo
	 */
	private void generarPuntos() {
		Dimension dim = this.getSize();
		this.rombo.removeAllPoints();
		this.rombo.setSize(dim);
		int w = dim.width - 2;
		int h = dim.height - 2;
		this.rombo.addPoint(new Point(w / 2, 0));
		this.rombo.addPoint(new Point(w, h / 2));
		this.rombo.addPoint(new Point(w / 2, h));
		this.rombo.addPoint(new Point(0, h / 2));
	}

	@Override
	public void setRepresentacion(PList representacion) {
		super.setRepresentacion(representacion);
		// Regenerar los puntos del rombo
		this.generarPuntos();
	}

	public void conectarEntidad(Figura<Entidad> figura, String label) {
		Connection conexion = Figura.conectar(this, figura);

		// Agregad cardinalidad y rol
		Label lblCardinalidad = new Label(label);
		conexion.add(lblCardinalidad, new MidpointLocator(conexion, 0));

		this.getParent().add(conexion);
		
		this.conexiones.put(figura.getComponente().getId(), conexion);
	}

	public void conectarAtributo(Figura<Atributo> figura) {
		Connection conexion = Figura.conectar(this, figura);
		this.getParent().add(conexion);
		
		this.conexiones.put(figura.getComponente().getId(), conexion);
	}
	
	@Override
	public void actualizar() {
		this.lblName.setText(this.componente.getNombre());		
	}
}