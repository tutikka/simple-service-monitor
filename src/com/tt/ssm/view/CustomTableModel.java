package com.tt.ssm.view;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

import com.tt.ssm.service.Response;
import com.tt.ssm.service.Service;

@SuppressWarnings("serial")
public class CustomTableModel extends AbstractTableModel {

	private static final String[] columns = new String[]{"Name", "Group", "Type", "Destination", "Status", "Time (ms)", "Message", "Updated"};

	private List<ServiceRow> rows = new ArrayList<ServiceRow>();
	
	public void updateFromService(Service service) {
		for (ServiceRow row : rows) {
			if (service.getId().equals(row.getId())) {
				row.setStatus(service.getResponse().getStatus());
				row.setTime(service.getResponse().getTime());
				row.setMessage(service.getResponse().getMessage());
				row.setUpdated(service.getResponse().getUpdated());
				fireTableDataChanged();
				return;
			}
		}
		ServiceRow row = new ServiceRow(service.getId());
		row.setName(service.getName());
		row.setGroup(service.getGroup());
		row.setType(service.getType());
		row.setDestination(service.getDestination());
		rows.add(row);
		fireTableDataChanged();
	}
	
	public ServiceRow getServiceRow(int row) {
		if (row < 0 || row > rows.size() - 1) {
			return (null);
		} else {
			return (rows.get(row));
		}
	}
	
	@Override
	public int getRowCount() {
		return (rows.size());
	}

	@Override
	public int getColumnCount() {
		return (columns.length);
	}

	@Override
	public String getColumnName(int column) {
		return (columns[column]);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0 : return (String.class);
		case 1 : return (String.class);
		case 2 : return (String.class);
		case 3 : return (String.class);
		case 4 : return (String.class);
		case 5 : return (Long.class);
		case 6 : return (String.class);
		case 7 : return (String.class);
		default : return (null);
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ServiceRow row = rows.get(rowIndex);
		if (row != null) {
			switch (columnIndex) {
			case 0 : return (row.getName());
			case 1 : return (row.getGroup());
			case 2 : return (row.getType());
			case 3 : return (row.getDestination());
			case 4 : return (Response.formatStatus(row.getStatus()));
			case 5 : return (row.getTime());
			case 6 : return (row.getMessage());
			case 7 : return (Response.formatUpdated(row.getUpdated()));
			default : return (null);
			}
		} else {
			return (null);
		}
	}
	
}