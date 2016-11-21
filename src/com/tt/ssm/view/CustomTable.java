package com.tt.ssm.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class CustomTable extends JTable {

	public CustomTable(CustomTableModel tableModel) {
		super(tableModel);
	}
	
	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
	    Component component = super.prepareRenderer( renderer, row, column );
	    if (!isRowSelected(row) && column != 4) {
	    	component.setBackground(row % 2 == 0 ? getBackground() : new Color(238, 238, 255));
	    }
	    return (component);
	}

}
