package mereditor.control;

import java.util.HashMap;
import java.util.Map;

import mereditor.interfaz.swt.figuras.AtributoFigure;
import mereditor.interfaz.swt.figuras.Figura;
import mereditor.modelo.Atributo;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.MouseEvent;

public class AtributoControl extends Atributo implements Control<Atributo> {
	protected Map<String, AtributoFigure> figures = new HashMap<>();

	@Override
	public Figura<Atributo> getFigura(String idDiagrama) {
		if (!this.figures.containsKey(idDiagrama)) {
			AtributoFigure figura = new AtributoFigure(this);
			this.figures.put(id, figura);
			// Agregar este controlador como listener para mouse clicks
			figura.addMouseListener(this);
		}

		return this.figures.get(idDiagrama);
	}

	@Override
	public void dibujar(Figure contenedor, String idDiagrama) {
		AtributoFigure figura = (AtributoFigure) this.getFigura(idDiagrama);
		contenedor.add(figura);

		for (Atributo atributo : this.atributos) {
			AtributoControl atributoControl = (AtributoControl) atributo;
			figura.conectarAtributo(atributoControl.getFigura(idDiagrama));
			atributoControl.dibujar(contenedor, idDiagrama);

			figura.agregarFiguraLoqueada(atributoControl.getFigura(idDiagrama));
		}
	}
	
	public Map<String, AtributoFigure> getFiguras() {
		return this.figures;
	}

	@Override
	public void mouseDoubleClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}