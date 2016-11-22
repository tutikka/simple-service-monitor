package com.tt.ssm.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

import com.tt.ssm.misc.Constants;
import com.tt.ssm.misc.Utils;
import com.tt.ssm.service.Service;
import com.tt.ssm.service.ServiceManager;
import com.tt.ssm.service.impl.JDBCService;
import com.tt.ssm.service.impl.TCPService;
import com.tt.ssm.service.impl.URLService;

@SuppressWarnings("serial")
public class SSMFrame extends JFrame implements ActionListener, MouseListener, URLServiceDialog.Callback, TCPServiceDialog.Callback, JDBCServiceDialog.Callback, ServiceManager.Callback {
	
	private CustomTableModel ctm = new CustomTableModel();
	
	private CustomTable table;
	
	private TableRowSorter<CustomTableModel> trs = new TableRowSorter<CustomTableModel>(ctm);
	
	public SSMFrame() {
		setTitle(Constants.TITLE);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT);
		Dimension dimension = Utils.getCurrentDisplayDimension(this);
		setLocation((int) (dimension.getWidth() / 2 - Constants.DEFAULT_WIDTH / 2), (int) (dimension.getHeight() / 2 - Constants.DEFAULT_HEIGHT / 2));
		setJMenuBar(createMenuBar());
		setLayout(new BorderLayout());
		add(createFilter(), BorderLayout.NORTH);
		add(createContent(), BorderLayout.CENTER);
		setVisible(true);
		ServiceManager.getInstance().registerCallback(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("jdbcService".equals(e.getActionCommand())) {
			new JDBCServiceDialog(this, this).setVisible(true);
		}
		if ("tcpService".equals(e.getActionCommand())) {
			new TCPServiceDialog(this, this).setVisible(true);
		}
		if ("urlService".equals(e.getActionCommand())) {
			new URLServiceDialog(this, this).setVisible(true);
		}
		if ("exit".equals(e.getActionCommand())) {
			int result = JOptionPane.showConfirmDialog(this, "Are you sure?", "Exit", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				ServiceManager.getInstance().unregisterCallback(this);
				ServiceManager.getInstance().close();
				dispose();
			}
		}
		if ("about".equals(e.getActionCommand())) {
			StringBuilder sb = new StringBuilder();
			sb.append(Constants.TITLE);
			sb.append("\n");
			sb.append("Version ");
			sb.append(Constants.VERSION);
			sb.append("\n");
			sb.append("Created by ");
			sb.append(Constants.AUTHOR);
			sb.append("\n");
			sb.append(Constants.DATE);
			JOptionPane.showMessageDialog(this, sb.toString(), "About", JOptionPane.INFORMATION_MESSAGE);
		}
		if ("open".equals(e.getActionCommand())) {
			int tableRow = table.getSelectedRow();
			if (tableRow == -1) {
				return;
			}
			int modelRow = table.convertRowIndexToModel(tableRow);
			if (modelRow == -1) {
				return;
			}
			Service service = ctm.getService(modelRow);
			ServiceDetailsDialog dialog = new ServiceDetailsDialog(this, service);
			ServiceManager.getInstance().registerCallback(dialog);
			dialog.setVisible(true);
			ServiceManager.getInstance().unregisterCallback(dialog);
		}
		if ("start".equals(e.getActionCommand())) {
			int tableRow = table.getSelectedRow();
			if (tableRow == -1) {
				return;
			}
			int modelRow = table.convertRowIndexToModel(tableRow);
			if (modelRow == -1) {
				return;
			}
			Service service = ctm.getService(modelRow);
			ServiceManager.getInstance().schedule(service);
		}
		if ("stop".equals(e.getActionCommand())) {
			int tableRow = table.getSelectedRow();
			if (tableRow == -1) {
				return;
			}
			int modelRow = table.convertRowIndexToModel(tableRow);
			if (modelRow == -1) {
				return;
			}
			Service service = ctm.getService(modelRow);
			ServiceManager.getInstance().cancel(service);
		}
		if ("delete".equals(e.getActionCommand())) {
			int tableRow = table.getSelectedRow();
			if (tableRow == -1) {
				return;
			}
			int modelRow = table.convertRowIndexToModel(tableRow);
			if (modelRow == -1) {
				return;
			}
			int result = JOptionPane.showConfirmDialog(this, "Are you sure?", "Delete", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				Service service = ctm.getService(modelRow);
				ServiceManager.getInstance().cancel(service);
				ctm.remove(service);
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
			int tableRow = table.rowAtPoint(e.getPoint());
			if (tableRow == -1) {
				return;
			}
			int modelRow = table.convertRowIndexToModel(tableRow);
			if (modelRow == -1) {
				return;
			}
			Service service = ctm.getService(modelRow);
			ServiceDetailsDialog dialog = new ServiceDetailsDialog(this, service);
			ServiceManager.getInstance().registerCallback(dialog);
			dialog.setVisible(true);
			ServiceManager.getInstance().unregisterCallback(dialog);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void onJDBCServiceAdded(JDBCService service) {
		ctm.update(service);
		ServiceManager.getInstance().schedule(service);
	}
	
	@Override
	public void onTCPServiceAdded(TCPService service) {
		ctm.update(service);
		ServiceManager.getInstance().schedule(service);
	}
	
	@Override
	public void onURLServiceAdded(URLService service) {
		ctm.update(service);
		ServiceManager.getInstance().schedule(service);
	}

	@Override
	public void onServiceResponded(Service service) {
		ctm.update(service);
	}
	
	private void applyFilter(String filter, int column) {
        RowFilter<CustomTableModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(filter, column);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        trs.setRowFilter(rf);
	}
	
	private JComponent createFilter() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
		panel.add(new JLabel("Filter:"));
		final JComboBox<String> column = new JComboBox<>(new String[]{"Name", "Group", "Type", "Destination", "Status", "Time", "Message", "Updated"});
		final JTextField filter = new JTextField("", 24);
		final JButton clear = new JButton("Clear");
		clear.setEnabled(false);
		column.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				applyFilter(filter.getText(), column.getSelectedIndex());
			}
		});
		panel.add(column);
		filter.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				applyFilter(filter.getText(), column.getSelectedIndex());
				clear.setEnabled(!filter.getText().isEmpty());
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				applyFilter(filter.getText(), column.getSelectedIndex());
				clear.setEnabled(!filter.getText().isEmpty());
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				applyFilter(filter.getText(), column.getSelectedIndex());
				clear.setEnabled(!filter.getText().isEmpty());
			}
		});
		panel.add(filter);
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				filter.setText("");
				applyFilter("", 0);
			}
		});
		panel.add(clear);
		filter.setPreferredSize(clear.getPreferredSize());
		return (panel);
	}
	
	private JComponent createContent() {
		table = new CustomTable(ctm);
		table.setBorder(BorderFactory.createEmptyBorder());
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setDefaultRenderer(String.class, new CustomTableCellRenderer());
		table.addMouseListener(this);
		table.setRowSorter(trs);
		table.setFillsViewportHeight(true);
	    JPopupMenu popupMenu = new JPopupMenu();
	    JMenuItem open = new JMenuItem("Open");
	    open.setActionCommand("open");
	    open.addActionListener(this);
	    JMenuItem start = new JMenuItem("Start");
	    start.setActionCommand("start");
	    start.addActionListener(this);
	    JMenuItem stop = new JMenuItem("Stop");
	    stop.setActionCommand("stop");
	    stop.addActionListener(this);
	    JMenuItem delete = new JMenuItem("Delete");
	    delete.setActionCommand("delete");
	    delete.addActionListener(this);
	    popupMenu.add(open);
	    popupMenu.addSeparator();
	    popupMenu.add(start);
	    popupMenu.add(stop);
	    popupMenu.addSeparator();
	    popupMenu.add(delete);
	    table.setComponentPopupMenu(popupMenu);
		JScrollPane jsp = new JScrollPane(table);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 1));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.add(jsp);
		return (panel);
	}
	
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem addService = new JMenu("Add service...");
		JMenuItem jdbcService = new JMenuItem("JDBC service");
		jdbcService.setActionCommand("jdbcService");
		jdbcService.addActionListener(this);
		addService.add(jdbcService);
		JMenuItem tcpService = new JMenuItem("TCP service");
		tcpService.setActionCommand("tcpService");
		tcpService.addActionListener(this);
		addService.add(tcpService);
		JMenuItem urlService = new JMenuItem("URL service");
		urlService.setActionCommand("urlService");
		urlService.addActionListener(this);
		addService.add(urlService);
		file.add(addService);
		file.addSeparator();
		JMenuItem exit = new JMenuItem("Exit");
		exit.setActionCommand("exit");
		exit.addActionListener(this);
		file.add(exit);
		menuBar.add(file);
		JMenu help = new JMenu("Help");
		JMenuItem about = new JMenuItem("About");
		about.setActionCommand("about");
		about.addActionListener(this);
		help.add(about);
		menuBar.add(help);
		return (menuBar);
	}
	
}