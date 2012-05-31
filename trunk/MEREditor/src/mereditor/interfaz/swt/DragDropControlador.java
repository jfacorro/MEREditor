package mereditor.interfaz.swt;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

public class DragDropControlador extends MouseMotionListener.Stub implements MouseListener  {
	private Point startPoint;

	public DragDropControlador() {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.startPoint = e.getLocation();
		e.consume();
	}
	
	@Override
	public void mouseDragged(MouseEvent evt) {
		this.actualizarPosicion((Figure)evt.getSource(), this.startPoint, evt.getLocation());
		this.startPoint = new Point(evt.getLocation());
	}

	
	public void actualizarPosicion(Figure figure, Point start, Point current) {
		if (start != null && !start.equals(current)) {
			Dimension delta = start.getDifference(current);
			figure.setBounds(figure.getBounds().getTranslated(-delta.width, -delta.height));
		}		
	}

	@Override
	public void mouseDoubleClicked(MouseEvent arg0) {		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
