package mereditor.editores;

import java.util.ArrayList;

import mereditor.interfaz.swt.Principal;
import mereditor.modelo.Atributo;
import mereditor.modelo.Entidad;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class EntidadEditor extends ApplicationWindow {

	private Shell shell;
	private Entidad entidad;
	private ArrayList<Atributo> atributos;

	// TODO: completar
	public static final String NOMBRE = "Nombre";
	public static final String TIPO = "Tipo";
	public static final String[] PROPS = { NOMBRE, TIPO };

	public EntidadEditor(Principal principal, Entidad entidad) {
		super(principal.getShell());

		this.shell = principal.getShell();
		this.entidad = entidad;

		this.atributos = new ArrayList<>();
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(entidad.getNombre() + " Editor");
		shell.setSize(400, 400);
	}

	protected Control createContents(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		Button btnNuevoAtributo = new Button(composite, SWT.PUSH);
		btnNuevoAtributo.setText("Crear un nuevo atributo");

		// TableViewer
		final TableViewer tv = new TableViewer(composite, SWT.FULL_SELECTION);
		tv.setContentProvider(new AtributoProvider());
		tv.setLabelProvider(new AtributoLabelProvider());
		tv.setInput(this.atributos);

		// Configurar tabla
		Table table = tv.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));

		new TableColumn(table, SWT.CENTER).setText(NOMBRE);
		new TableColumn(table, SWT.CENTER).setText(TIPO);

		for (int i = 0, n = table.getColumnCount(); i < n; i++) {
			table.getColumn(i).pack();
		}

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// agrega atributos existentes
		for (Atributo attr : this.entidad.getAtributos()) {
			atributos.add(attr);
		}
		tv.refresh();

		// Agregar un nuevo atributo cuando se hace click sobre el botón
		btnNuevoAtributo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Atributo atr = new Atributo();
				atr.setNombre("Nombre");
				// TODO: completar el resto de los valores iniciales del
				// atributo

				atributos.add(atr);
				tv.refresh();

				// TODO: impactar sobre el modelo
			}
		});

		// Crear editores de celda
		CellEditor[] editors = new CellEditor[2];
		editors[0] = new TextCellEditor(table);
		editors[1] = new CheckboxCellEditor(table);

		//
		tv.setColumnProperties(PROPS);
		tv.setCellModifier(new AtributoCellModifier(tv));
		tv.setCellEditors(editors);

		return composite;
	}

}