package mereditor.interfaz.swt.listeners;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mereditor.interfaz.swt.figuras.Figura;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.swt.SWT;

public class SeleccionControlador extends MouseListener.Stub {
	private final static Set<IFigure> selected = new HashSet<>();
	private final static Map<IFigure, Figure> selectedBorders = new HashMap<>();

	private Figura<?> figura = null;

	public SeleccionControlador(Figura<?> figura) {
		this.figura = figura;
		this.figura.addMouseListener(this);
	}

	private boolean selectionModifiers(int state) {
		return (state & SWT.CTRL) != 0;
	}

	@Override
	public void mousePressed(MouseEvent me) {
		if (this.shouldDeselectAll(me.getState()))
			deselectAll(this.figura);

		if (!selected.contains(this.figura))
			select(this.figura);
		else if (this.selectionModifiers(me.getState()))
			deselect(this.figura);
	}

	private boolean shouldDeselectAll(int state) {
		boolean deselect = false;
		// Si CTRL NO está presionado y hay una sola figura selccionada.
		deselect = deselect || (!this.selectionModifiers(state) && selected.size() == 1);
		// Si CTRL NO está presionado, hay más de una figura seleccionada, pero esta figura no está seleccionada. 
		deselect = deselect || (!this.selectionModifiers(state) && selected.size() > 1 && !isSelected(this.figura));		
		return deselect;
	}

	@Override
	public void mouseReleased(MouseEvent me) {
	}

	@Override
	public void mouseDoubleClicked(MouseEvent me) {

	}

	/**
	 * Selecciona la figura.
	 * 
	 * @param figura
	 */
	private static void select(Figura<?> figura) {
		Figure seleccion = new Figure();
		seleccion.setOpaque(false);
		seleccion.setEnabled(false);
		seleccion.setBounds(figura.getBounds().getExpanded(2, 2));
		seleccion.setBorder(new LineBorder(Figura.defaultLineColor, 1,
				SWT.LINE_DASH));
		figura.getParent().add(seleccion);
		figura.addFiguraLoqueada(seleccion);

		selected.add(figura);
		selectedBorders.put(figura, seleccion);
	}

	/**
	 * Deselecciona la figura indicada y elimina el borde de la selección.
	 * 
	 * @param figura
	 */
	private static void deselect(Figura<?> figura) {
		if (figura != null && figura.getParent() != null) {
			IFigure parent = figura.getParent();

			if (parent == selectedBorders.get(figura).getParent())
				parent.remove(selectedBorders.get(figura));

			figura.removeFiguraLoqueada(selectedBorders.get(figura));
		}

		selected.remove(figura);
		selectedBorders.remove(figura);
	}

	/**
	 * Indica si una figura está seleccionada.
	 * 
	 * @param figura
	 * @return
	 */
	public static boolean isSelected(Figure figura) {
		return selected.contains(figura);
	}

	/**
	 * Deselecciona todas las figuras.
	 */
	public static void deselectAll(Figura<?> figuraExcepcion) {
		Set<IFigure> figuras = new HashSet<>(selected);

		for (IFigure figura : figuras)
			if (figuraExcepcion == null || figuraExcepcion != figura)
				deselect((Figura<?>) figura);
	}

	/**
	 * Devuelve todas las figuras seleccionadas.
	 * 
	 * @return
	 */
	public static Set<IFigure> getSelected() {
		return Collections.unmodifiableSet(selected);
	}
}
