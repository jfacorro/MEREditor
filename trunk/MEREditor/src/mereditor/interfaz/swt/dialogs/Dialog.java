package mereditor.interfaz.swt.dialogs;

import mereditor.interfaz.swt.Principal;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public abstract class Dialog extends org.eclipse.jface.dialogs.Dialog {
	protected Principal principal = Principal.getInstance();
	protected String titulo = "";

	/**
	 * @wbp.parser.constructor
	 */
	protected Dialog(Shell shell) {
		super(shell);
		setShellStyle(SWT.CLOSE);
	}

	public Dialog() {
		super(Principal.getInstance().getShell());
	}
	
	@Override
	protected Control createContents(Composite parent) {
		Control control = super.createContents(parent);
		
		Button btnOK = this.getButton(IDialogConstants.OK_ID);
		Button btnCancel = this.getButton(IDialogConstants.CANCEL_ID);
		btnOK.setText("Aceptar");
		btnOK.addSelectionListener(this.aceptar);
		btnCancel.setText("Cancelar");
		btnCancel.addSelectionListener(this.cancelar);
		
		return control;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(this.titulo);
	}

	/**
	 * Acciones a realizar al presionar el boton aceptar
	 */
	protected abstract void aceptar();

	/**
	 * Acciones a realizar al presionar el boton cancelar
	 */
	protected void cancelar() {
		this.close();
	}

	private SelectionAdapter aceptar = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			aceptar();
		};
	};

	private SelectionAdapter cancelar = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			cancelar();
		};
	};
}
