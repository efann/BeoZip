/*
 * BeoZip : a simple archiving application for the Java(tm) Swing platform previously written in C++.
 *
 * Copyright(c) 2001-2018, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.BeoZip;

import com.beowurks.BeoCommon.BaseButton;
import com.beowurks.BeoCommon.BaseProperties;
import com.beowurks.BeoCommon.GridBagLayoutHelper;
import com.beowurks.BeoCommon.Util;
import com.beowurks.BeoTable.SortingTable;
import com.beowurks.BeoTable.SortingTableModel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class ArchiveFoldersDialog1 extends BeoZipBaseDialog implements ActionListener
{
  private final String fcPropertyPrefix = this.getClass().getName();

  private final JPanel pnlArchive1 = new JPanel();

  private final Box boxButtonsOKCancel1 = Box.createHorizontalBox();
  private final Box boxButtonsCommands1 = Box.createVerticalBox();

  private final BaseButton btnAddFolder1 = new BaseButton();
  private final BaseButton btnRemoveFolder1 = new BaseButton(BaseButton.BASE_WIDTH, (int) (BaseButton.BASE_HEIGHT * 1.6));

  private final BaseButton btnOK1 = new BaseButton();
  private final BaseButton btnCancel1 = new BaseButton();

  private final JLabel lblFolders1 = new JLabel();

  private final SortingTable grdFoldersToArchive1 = new SortingTable();
  private final JScrollPane scrFoldersToArchive1 = new JScrollPane();

  private final StringBuilder fcAddFolderLocation = new StringBuilder(256);

  private String[] faFolders;

  private boolean flOkay = false;

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public ArchiveFoldersDialog1(final MainFrame toMainFrame, final String tcTitle, final String[] taFolders)
  {
    super(toMainFrame, tcTitle);

    this.faFolders = taFolders;

    try
    {
      this.jbInit();
      this.makeVisible(true);
    }
    catch (final Exception e)
    {
      e.printStackTrace();
    }
  }

  // ---------------------------------------------------------------------------
  // Component initialization
  @Override
  protected void jbInit() throws Exception
  {
    super.jbInit();

    this.setResizable(false);

    this.setupButtons();
    this.setupLabels();
    this.setupTables();
    this.setupListeners();
    this.setupLayouts();

    this.getProperties();
  }

  // ---------------------------------------------------------------------------
  private void setupLayouts()
  {
    this.boxButtonsOKCancel1.add(this.btnOK1, null);
    this.boxButtonsOKCancel1.add(this.btnCancel1, null);

    this.boxButtonsCommands1.add(this.btnAddFolder1, null);
    this.boxButtonsCommands1.add(this.btnRemoveFolder1, null);

    this.scrFoldersToArchive1.getViewport().add(this.grdFoldersToArchive1, null);

    GridBagLayoutHelper loGrid;
    // --------------------
    loGrid = new GridBagLayoutHelper();

    this.pnlArchive1.setLayout(loGrid);
    loGrid.setInsetDefaults();

    this.pnlArchive1.add(this.lblFolders1, loGrid.getConstraint(0, 2, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE));
    this.pnlArchive1.add(this.scrFoldersToArchive1, loGrid.getConstraint(1, 2, GridBagConstraints.WEST, GridBagConstraints.BOTH));
    this.pnlArchive1.add(this.boxButtonsCommands1, loGrid.getConstraint(2, 2, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE));

    // --------------------
    loGrid = new GridBagLayoutHelper();

    this.getContentPane().setLayout(loGrid);

    loGrid.setInsets(10, 4, 10, 4);

    this.getContentPane().add(this.pnlArchive1, loGrid.getConstraint(0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE));
    this.getContentPane().add(this.boxButtonsOKCancel1, loGrid.getConstraint(0, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE));
  }

  // ---------------------------------------------------------------------------
  private void setupTables()
  {
    this.scrFoldersToArchive1.setPreferredSize(new Dimension((int) (MainFrame.ARCHIVE_MEMO_WIDTH * 0.75), MainFrame.ARCHIVE_MEMO_HEIGHT));

    this.grdFoldersToArchive1.setTableHeader(null);
    this.grdFoldersToArchive1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    this.grdFoldersToArchive1.setupColumns(new Object[][]{{"Folders", ""}});
    this.grdFoldersToArchive1.setupHeaderRenderer();

    final SortingTableModel loModel = this.grdFoldersToArchive1.getSortModel();
    loModel.clearAll();

    if (this.faFolders != null)
    {
      for (final String lcFolder : this.faFolders)
      {
        loModel.addRow(new Object[]{lcFolder});
      }
    }

    loModel.sortColumn(0, true);

    this.grdFoldersToArchive1.updateUI();
  }

  // ---------------------------------------------------------------------------
  private void setupListeners()
  {
    this.btnOK1.addActionListener(this);
    this.btnCancel1.addActionListener(this);

    this.btnAddFolder1.addActionListener(this);
    this.btnRemoveFolder1.addActionListener(this);
  }

  // ---------------------------------------------------------------------------
  private void setupLabels()
  {
    this.lblFolders1.setHorizontalAlignment(SwingConstants.RIGHT);
    this.lblFolders1.setText("Folders To Archive");
  }

  // ---------------------------------------------------------------------------
  private void setupButtons()
  {
    this.btnAddFolder1.setText("Add Folder...");
    this.btnAddFolder1.setMnemonic('A');
    this.btnAddFolder1.setToolTipText("Add a folder to the 'Folders To Archive'");

    this.btnRemoveFolder1.setText("<html><div align=\"center\"><font face=\"Arial\"><u>R</u>emove Folder</div></font></html>");
    this.btnRemoveFolder1.setMnemonic('R');
    this.btnRemoveFolder1.setToolTipText("Remove the currently selected folder in the 'Folders To Archive'");

    this.btnOK1.setText("OK");
    this.btnOK1.setMnemonic('O');
    this.btnOK1.setToolTipText("Save any changes made to the folder selection");

    this.btnCancel1.setText("Cancel");
    this.btnCancel1.setMnemonic('C');
    this.btnCancel1.setToolTipText("Cancel any changes made to the folder selection");
  }

  // ---------------------------------------------------------------------------
  private void addToFoldersToArchive()
  {
    final SelectFileReturnValue loValue = this.foMainFrame.foSelectFilesChooser.selectFileDialog(this.fcAddFolderLocation.toString(),
            JFileChooser.DIRECTORIES_ONLY, false, this.foMainFrame.foAppProperties.getOptionIncludeHiddenDirectoriesBZ(), false, false,
            "Add Folder To Be Archived", "Add Folder", SelectFilesChooser.ACCESSORY_NONE);

    if (loValue != null)
    {
      final String lcFolder = loValue.SelectedFile.getPath();

      Util.clearStringBuilder(this.fcAddFolderLocation);
      this.fcAddFolderLocation.append(lcFolder);

      final SortingTableModel loModel = this.grdFoldersToArchive1.getSortModel();
      loModel.addRow(new Object[]{lcFolder});
      loModel.sortColumn(0, true);
    }
  }

  // ---------------------------------------------------------------------------
  private void removeFoldersFromArchive()
  {
    final int lnRow = this.grdFoldersToArchive1.getSelectedRow();
    if (lnRow != -1)
    {
      try
      {
        final SortingTableModel loModel = this.grdFoldersToArchive1.getSortModel();
        loModel.removeRow(lnRow);
        loModel.sortColumn(0, true);
      }
      catch (final ArrayIndexOutOfBoundsException ignored)
      {
      }
    }
    else
    {
      Util.errorMessage(this, "You must first select a folder to remove.");
    }
  }

  // ---------------------------------------------------------------------------
  private void getProperties()
  {
    final BaseProperties loProp = this.getMainFrame().foAppProperties;

    Util.clearStringBuilder(this.fcAddFolderLocation);
    this.fcAddFolderLocation.append(loProp.getProperty(this.fcPropertyPrefix + " fcAddFolderLocation", System.getProperty("user.home")));
  }

  // ---------------------------------------------------------------------------
  private void setProperties()
  {
    final BaseProperties loProp = this.getMainFrame().foAppProperties;

    loProp.setProperty(this.fcPropertyPrefix + " fcAddFolderLocation", this.fcAddFolderLocation.toString());
  }

  // ---------------------------------------------------------------------------
  public boolean isOkay()
  {
    return (this.flOkay);
  }

  // ---------------------------------------------------------------------------
  public String[] getFolders()
  {
    return (this.faFolders);
  }

  // ---------------------------------------------------------------------------
  @Override
  public void removeListeners()
  {
    this.btnOK1.removeActionListener(this);
    this.btnCancel1.removeActionListener(this);

    this.btnAddFolder1.removeActionListener(this);
    this.btnRemoveFolder1.removeActionListener(this);
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface ActionListener
  // ---------------------------------------------------------------------------
  @Override
  public void actionPerformed(final ActionEvent e)
  {
    final Object loSource = e.getSource();

    if (loSource instanceof JButton)
    {
      if (loSource == this.btnOK1)
      {
        this.flOkay = true;
        final int lnFolders = this.grdFoldersToArchive1.getRowCount();

        this.faFolders = (lnFolders > 0) ? new String[lnFolders] : null;
        if (this.faFolders != null)
        {
          final SortingTableModel loModel = this.grdFoldersToArchive1.getSortModel();
          for (int i = 0; i < lnFolders; ++i)
          {
            this.faFolders[i] = loModel.getValueAt(i, 0).toString();
          }
        }

        this.setProperties();
        this.closeWindow();
      }
      else if (loSource == this.btnCancel1)
      {
        this.closeWindow();
      }
      else if (loSource == this.btnAddFolder1)
      {
        this.addToFoldersToArchive();
      }
      else if (loSource == this.btnRemoveFolder1)
      {
        this.removeFoldersFromArchive();
      }
    }

  }
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
