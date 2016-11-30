package com.tt.ssm.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
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
import javax.swing.JFileChooser;
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
import com.tt.ssm.service.impl.ICMPService;
import com.tt.ssm.service.impl.JDBCService;
import com.tt.ssm.service.impl.TCPService;
import com.tt.ssm.service.impl.URLService;

@SuppressWarnings("serial")
public class SSMFrame extends JFrame implements ActionListener, MouseListener, URLServiceDialog.Callback, TCPServiceDialog.Callback, JDBCServiceDialog.Callback, ICMPServiceDialog.Callback, ServiceManager.Callback {
	
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
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					new JDBCServiceDialog(SSMFrame.this, SSMFrame.this).setVisible(true);
				}
			});
			
		}
		if ("tcpService".equals(e.getActionCommand())) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					new TCPServiceDialog(SSMFrame.this, SSMFrame.this).setVisible(true);
				}
			});
			
		}
		if ("urlService".equals(e.getActionCommand())) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					new URLServiceDialog(SSMFrame.this, SSMFrame.this).setVisible(true);
				}
			});
			
		}
		if ("icmpService".equals(e.getActionCommand())) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					new ICMPServiceDialog(SSMFrame.this, SSMFrame.this).setVisible(true);
				}
			});
			
		}
		if ("save".equals(e.getActionCommand())) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					handleSave();
				}
			});
		}
		if ("open".equals(e.getActionCommand())) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					handleOpen();
				}
			});
		}
		if ("exit".equals(e.getActionCommand())) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					handleExit();
				}
			});

		}
		if ("about".equals(e.getActionCommand())) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
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
					JOptionPane.showMessageDialog(SSMFrame.this, sb.toString(), "About", JOptionPane.INFORMATION_MESSAGE);
				}
			});

		}
		if ("details".equals(e.getActionCommand())) {
			final int tableRow = table.getSelectedRow();
			if (tableRow == -1) {
				return;
			}
			final int modelRow = table.convertRowIndexToModel(tableRow);
			if (modelRow == -1) {
				return;
			}
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					Service service = ctm.getService(modelRow);
					ServiceDetailsDialog dialog = new ServiceDetailsDialog(SSMFrame.this, service);
					ServiceManager.getInstance().registerCallback(dialog);
					dialog.setVisible(true);
					ServiceManager.getInstance().unregisterCallback(dialog);
				}
			});

		}
		if ("cancel".equals(e.getActionCommand())) {
			final int tableRow = table.getSelectedRow();
			if (tableRow == -1) {
				return;
			}
			final int modelRow = table.convertRowIndexToModel(tableRow);
			if (modelRow == -1) {
				return;
			}
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					int result = JOptionPane.showConfirmDialog(SSMFrame.this, "The service will be cancelled and removed from the list. Are you sure?", "Cancel", JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.YES_OPTION) {
						Service service = ctm.getService(modelRow);
						ServiceManager.getInstance().cancel(service);
					}
				}
			});

		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
			final int tableRow = table.rowAtPoint(e.getPoint());
			if (tableRow == -1) {
				return;
			}
			final int modelRow = table.convertRowIndexToModel(tableRow);
			if (modelRow == -1) {
				return;
			}
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					Service service = ctm.getService(modelRow);
					ServiceDetailsDialog dialog = new ServiceDetailsDialog(SSMFrame.this, service);
					ServiceManager.getInstance().registerCallback(dialog);
					dialog.setVisible(true);
					ServiceManager.getInstance().unregisterCallback(dialog);
				}
			});

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
	public void onJDBCServiceAdded(final JDBCService service) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ServiceManager.getInstance().schedule(service);
			}
		});

	}
	
	@Override
	public void onTCPServiceAdded(final TCPService service) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ServiceManager.getInstance().schedule(service);
			}
		});

	}
	
	@Override
	public void onURLServiceAdded(final URLService service) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ServiceManager.getInstance().schedule(service);
			}
		});

	}

	@Override
	public void onICMPServiceAdded(final ICMPService service) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ServiceManager.getInstance().schedule(service);
			}
		});

	}
	
	@Override
	public void onServiceResponded(final Service service) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ctm.update(service);
			}
		});
	}
	
	@Override
	public void onServiceScheduled(final Service service) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ctm.add(service);
			}
		});
	}

	@Override
	public void onServiceCancelled(final Service service) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ctm.remove(service);
			}
		});
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
	
	/* ********** private ********** */
	
	private void handleSave() {
		JFileChooser chooser = new JFileChooser();
		int result = chooser.showSaveDialog(SSMFrame.this);
		if (result == JFileChooser.APPROVE_OPTION) {
			if (chooser.getSelectedFile().exists()) {
				result = JOptionPane.showConfirmDialog(SSMFrame.this, "The selected file already exists and will be overwritten. Are you sure?", "Save", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					ServiceManager.getInstance().save(chooser.getSelectedFile());
				}
			} else {
				ServiceManager.getInstance().save(chooser.getSelectedFile());
			}
		}
	}
	
	private void handleOpen() {
		if (ServiceManager.getInstance().list().size() > 0) {
			int result = JOptionPane.showConfirmDialog(SSMFrame.this, "All current services will be cancelled. Are you sure?", "Open", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				JFileChooser chooser = new JFileChooser();
				result = chooser.showOpenDialog(SSMFrame.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					ServiceManager.getInstance().open(chooser.getSelectedFile());
				}
			}
		} else {
			JFileChooser chooser = new JFileChooser();
			int result = chooser.showOpenDialog(SSMFrame.this);
			if (result == JFileChooser.APPROVE_OPTION) {
				ServiceManager.getInstance().open(chooser.getSelectedFile());
			}
		}
	}
	
	private void handleExit() {
		if (ServiceManager.getInstance().list().size() > 0) {
			int result = JOptionPane.showConfirmDialog(SSMFrame.this, "All current services will be cancelled. Are you sure?", "Exit", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				ServiceManager.getInstance().unregisterCallback(SSMFrame.this);
				ServiceManager.getInstance().close();
				dispose();
			}
		} else {
			ServiceManager.getInstance().unregisterCallback(SSMFrame.this);
			ServiceManager.getInstance().close();
			dispose();
		}
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
	    JMenuItem details = new JMenuItem("Details");
	    details.setActionCommand("details");
	    details.addActionListener(this);
	    JMenuItem cancel = new JMenuItem("Cancel");
	    cancel.setActionCommand("cancel");
	    cancel.addActionListener(this);
	    popupMenu.add(details);
	    popupMenu.addSeparator();
	    popupMenu.add(cancel);
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
		JMenuItem addService = new JMenu("Add service");
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
		JMenuItem icmpService = new JMenuItem("ICMP service");
		icmpService.setActionCommand("icmpService");
		icmpService.addActionListener(this);
		addService.add(icmpService);
		file.add(addService);
		file.addSeparator();
		JMenuItem open = new JMenuItem("Open...");
		open.setActionCommand("open");
		open.addActionListener(this);
		file.add(open);
		JMenuItem save = new JMenuItem("Save...");
		save.setActionCommand("save");
		save.addActionListener(this);
		file.add(save);
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