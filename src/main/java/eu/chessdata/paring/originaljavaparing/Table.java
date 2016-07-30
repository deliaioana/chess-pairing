package eu.chessdata.paring.originaljavaparing;

import eu.chessdata.paring.originaljavaparing.stripedobjects.TableModel;

public class Table implements TableModel {

	public int getRowCount() {
		throw new UnsupportedOperationException("Please implement this");
	}

	public int getColumnCount() {
		throw new UnsupportedOperationException("Please implement this");
	}

	public String getColumnName(int columnIndex) {
		throw new UnsupportedOperationException("Please implement this");
	}

	public Class<?> getColumnClass(int columnIndex) {
		throw new UnsupportedOperationException("Please implement this");
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		throw new UnsupportedOperationException("Please implement this");
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		throw new UnsupportedOperationException("Please implement this");
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		throw new UnsupportedOperationException("Please implement this");

	}

}
