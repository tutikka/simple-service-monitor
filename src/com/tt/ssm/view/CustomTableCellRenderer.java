package com.tt.ssm.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class CustomTableCellRenderer extends DefaultTableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	    Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	    if (!isSelected && column == 4) {
	    	String status = (String) value;
	    	// OK
	    	if ("OK".equals(status)) {
	    		component.setBackground(Color.GREEN);
	    	}
	    	// WARNING
	    	if ("WARNING".equals(status)) {
	    		component.setBackground(Color.YELLOW);
	    	}
	    	// ERROR
	    	if ("ERROR".equals(status)) {
	    		component.setBackground(Color.RED);
	    	}
	    }
	    return (component);
	}

}
