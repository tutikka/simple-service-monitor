package com.tt.ssm.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.tt.ssm.service.Response;
import com.tt.ssm.service.Service;

@SuppressWarnings("serial")
public class CustomTableModel extends AbstractTableModel {

	private static final String[] columns = new String[]{"Name", "Group", "Type", "Destination", "Status", "Time (ms)", "Message", "Updated"};

	private List<Service> services = new ArrayList<Service>();
	
	public void add(Service service) {
		services.add(service);
		fireTableRowsInserted(services.size() - 1, services.size() - 1);
	}
	
	public void update(Service service) {
		int row = services.indexOf(service);
		if (row < 0 || row > services.size() - 1) {
		} else {
			Service existing = services.get(row);
			existing.setResponse(service.getResponse());
			fireTableRowsUpdated(row, row);
		}
	}
	
	public void remove(Service service) {
		if (services.contains(service)) {
			int index = services.indexOf(service);
			services.remove(index);
			fireTableRowsDeleted(index, index);
		}
	}
	
	public Service getService(int row) {
		if (row < 0 || row > services.size() - 1) {
			return (null);
		} else {
			return (services.get(row));
		}
	}
	
	@Override
	public int getRowCount() {
		return (services.size());
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
		Service service = services.get(rowIndex);
		if (service != null) {
			switch (columnIndex) {
			case 0 : return (service.getName());
			case 1 : return (service.getGroup());
			case 2 : return (service.getType());
			case 3 : return (service.getDestination());
			case 4 : return (service.getResponse() == null ? null : Response.formatStatus(service.getResponse().getStatus()));
			case 5 : return (service.getResponse() == null ? null : service.getResponse().getTime());
			case 6 : return (service.getResponse() == null ? null : service.getResponse().getMessage());
			case 7 : return (service.getResponse() == null ? null : Response.formatUpdated(service.getResponse().getUpdated()));
			default : return (null);
			}
		} else {
			return (null);
		}
	}
	
}