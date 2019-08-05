/*
 * BeoZip : a simple archiving application for the Java(tm) Swing platform previously written in C++.
 *
 * Copyright(c) 2001-2019, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.beozip;

import com.beowurks.BeoCommon.BaseButton;
import com.beowurks.BeoCommon.BaseProperties;
import com.beowurks.BeoCommon.BaseTabbedPane;
import com.beowurks.BeoCommon.GridBagLayoutHelper;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class OptionsDialog1 extends BeoZipBaseDialog implements ActionListener
{
  private final String fcPropertyPrefix = this.getClass().getName();

  private final BaseTabbedPane tabPane1 = new BaseTabbedPane();
  private final JPanel pnlArchiving1 = new JPanel();
  private final JPanel pnlQuickZip1 = new JPanel();

  private final JComboBox cboCompression1 = new JComboBox();
  private final JCheckBox chkIncludeHiddenDirectories1 = new JCheckBox();
  private final JCheckBox chkIncludeHiddenFiles1 = new JCheckBox();

  private final JComboBox cboCompression2 = new JComboBox();
  private final JCheckBox chkIncludeHiddenDirectories2 = new JCheckBox();
  private final JCheckBox chkIncludeHiddenFiles2 = new JCheckBox();

  private final JLabel lblCompression1 = new JLabel();
  private final JLabel lblCompression2 = new JLabel();
  private final JLabel lblSampleArchiveName1 = new JLabel();

  private final BaseButton btnOK1 = new BaseButton();
  private final BaseButton btnCancel1 = new BaseButton();

  private final ButtonGroup foButtonGroup1 = new ButtonGroup();
  private JPanel pnlFileNameFormat1 = null;

  private final Box boxButtons1 = Box.createHorizontalBox();

  private final String[] faCompression = new String[]{"No Compression", "Level 1, Least Compression & Fastest",
      "Level 2", "Level 3", "Level 4", "Level 5, Okay Compression & Speed", "Level 6 (Default)", "Level 7", "Level 8",
      "Level 9, Best Compression & Slowest"};

  private ArchiveFileNameParameters[] faArchiveParameters = null;

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public OptionsDialog1(final MainFrame toMainFrame, final String tcTitle)
  {
    super(toMainFrame, tcTitle);

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

    this.setupPanels();
    this.setupOptionGroups();
    this.setupTabPanes();
    this.setupButtons();
    this.setupComboBoxes();
    this.setupLabels();
    this.setupCheckBoxes();
    this.setupListeners();
    this.getMainFormVariables();
    this.setupLayouts();

    this.getProperties();
  }

  // ---------------------------------------------------------------------------
  private void setupLayouts()
  {
    this.boxButtons1.add(this.btnOK1, null);
    this.boxButtons1.add(this.btnCancel1, null);

    GridBagLayoutHelper loGrid;
    // --------------------
    loGrid = new GridBagLayoutHelper();
    this.pnlArchiving1.setLayout(loGrid);
    loGrid.setInsetDefaults();

    this.pnlArchiving1.add(this.lblCompression1, loGrid.getConstraint(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE));
    this.pnlArchiving1.add(this.cboCompression1, loGrid.getConstraint(1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE));
    this.pnlArchiving1.add(this.chkIncludeHiddenDirectories1, loGrid.getConstraint(1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));
    this.pnlArchiving1.add(this.chkIncludeHiddenFiles1, loGrid.getConstraint(1, 2, GridBagConstraints.WEST, GridBagConstraints.NONE));
    this.pnlArchiving1.add(this.pnlFileNameFormat1, loGrid.getConstraint(1, 3, GridBagConstraints.WEST, GridBagConstraints.NONE));
    this.pnlArchiving1.add(this.lblSampleArchiveName1, loGrid.getConstraint(1, 4, GridBagConstraints.WEST, GridBagConstraints.NONE));

    // --------------------
    loGrid = new GridBagLayoutHelper();

    this.pnlQuickZip1.setLayout(loGrid);
    loGrid.setInsetDefaults();

    this.pnlQuickZip1.add(this.lblCompression2, loGrid.getConstraint(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE));
    this.pnlQuickZip1.add(this.cboCompression2, loGrid.getConstraint(1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE));
    this.pnlQuickZip1.add(this.chkIncludeHiddenDirectories2, loGrid.getConstraint(1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));
    this.pnlQuickZip1.add(this.chkIncludeHiddenFiles2, loGrid.getConstraint(1, 2, GridBagConstraints.WEST, GridBagConstraints.NONE));

    // --------------------
    loGrid = new GridBagLayoutHelper();

    this.getContentPane().setLayout(loGrid);

    loGrid.setInsets(10, 4, 10, 4);

    this.getContentPane().add(this.tabPane1, loGrid.getConstraint(0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE));
    this.getContentPane().add(this.boxButtons1, loGrid.getConstraint(0, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE));
  }

  // ---------------------------------------------------------------------------
  private void setupOptionGroups() throws Exception
  {
    if (this.pnlFileNameFormat1 == null)
    {
      throw new Exception("The panels have not been initialized in the routine setupPanels in OptionsDialog1.");
    }

    this.faArchiveParameters = new ArchiveFileNameParameters[3];
    this.faArchiveParameters[0] = new ArchiveFileNameParameters();
    this.faArchiveParameters[0].fcLabel = "Date Format for Archive File";
    this.faArchiveParameters[0].fnValue = AppProperties.ARCHIVENAME_DATEONLY;
    this.faArchiveParameters[1] = new ArchiveFileNameParameters();
    this.faArchiveParameters[1].fcLabel = "Date-Time Format for Archive File";
    this.faArchiveParameters[1].fnValue = AppProperties.ARCHIVENAME_DATETIME;
    this.faArchiveParameters[2] = new ArchiveFileNameParameters();
    this.faArchiveParameters[2].fcLabel = "Date-Time Millisecond Format for Archive File";
    this.faArchiveParameters[2].fnValue = AppProperties.ARCHIVENAME_DATETIMEMILLISECONDS;

    JRadioButton loRadio;

    final int lnValue = this.getMainFrame().foAppProperties.getOptionArchiveNameTypeBZ();
    for (final ArchiveFileNameParameters laArchiveParameter : this.faArchiveParameters)
    {
      loRadio = new JRadioButton(laArchiveParameter.fcLabel);
      loRadio.setCursor(new Cursor(Cursor.HAND_CURSOR));
      loRadio.setSelected(lnValue == laArchiveParameter.fnValue);
      loRadio.setActionCommand(loRadio.getText());

      this.foButtonGroup1.add(loRadio);
      this.pnlFileNameFormat1.add(loRadio);
    }
  }

  // ---------------------------------------------------------------------------
  private void setupPanels()
  {
    final Border loBorder = new CompoundBorder(new SoftBevelBorder(BevelBorder.LOWERED), new EmptyBorder(5, 5, 5, 5));

    this.pnlFileNameFormat1 = new JPanel();
    this.pnlFileNameFormat1.setLayout(new BoxLayout(this.pnlFileNameFormat1, BoxLayout.Y_AXIS));
    this.pnlFileNameFormat1.setAlignmentY(Component.TOP_ALIGNMENT);
    this.pnlFileNameFormat1.setAlignmentX(Component.LEFT_ALIGNMENT);
    this.pnlFileNameFormat1.setBorder(loBorder);
  }

  // ---------------------------------------------------------------------------
  private void setupListeners()
  {
    this.btnOK1.addActionListener(this);
    this.btnCancel1.addActionListener(this);

    final Enumeration<AbstractButton> loE = this.foButtonGroup1.getElements();
    while (loE.hasMoreElements())
    {
      final AbstractButton loAbstractButton = loE.nextElement();
      if (loAbstractButton instanceof JRadioButton)
      {
        loAbstractButton.addActionListener(this);
      }
    }
  }

  // ---------------------------------------------------------------------------
  private void setupLabels()
  {
    this.lblCompression1.setText("Compression");
    this.lblCompression1.setHorizontalAlignment(SwingConstants.RIGHT);

    this.lblCompression2.setText("Compression");
    this.lblCompression2.setHorizontalAlignment(SwingConstants.RIGHT);

    this.lblSampleArchiveName1.setText(this.getMainFrame().buildArchiveFileName(this.getMainFrame().foAppProperties.getOptionArchiveNameTypeBZ()));
    this.lblSampleArchiveName1.setFont(this.lblSampleArchiveName1.getFont().deriveFont(Font.ITALIC));
  }

  // ---------------------------------------------------------------------------
  private void setupCheckBoxes()
  {
    this.chkIncludeHiddenDirectories1.setText("Include Hidden Directories (NOT Recommended)");
    this.chkIncludeHiddenFiles1.setText("Include Hidden Files");
    this.chkIncludeHiddenDirectories1.setCursor(new Cursor(Cursor.HAND_CURSOR));
    this.chkIncludeHiddenFiles1.setCursor(new Cursor(Cursor.HAND_CURSOR));

    this.chkIncludeHiddenDirectories2.setText("Include Hidden Directories (NOT Recommended)");
    this.chkIncludeHiddenFiles2.setText("Include Hidden Files");
    this.chkIncludeHiddenDirectories2.setCursor(new Cursor(Cursor.HAND_CURSOR));
    this.chkIncludeHiddenFiles2.setCursor(new Cursor(Cursor.HAND_CURSOR));
  }

  // ---------------------------------------------------------------------------
  private void setupComboBoxes()
  {
    final Dimension ldSize = new Dimension(300, Global.TEXT_HEIGHT);

    this.cboCompression1.setMinimumSize(ldSize);
    this.cboCompression1.setPreferredSize(ldSize);
    this.cboCompression1.setCursor(new Cursor(Cursor.HAND_CURSOR));

    this.cboCompression2.setMinimumSize(ldSize);
    this.cboCompression2.setPreferredSize(ldSize);
    this.cboCompression2.setCursor(new Cursor(Cursor.HAND_CURSOR));

    for (final String lcCompression : this.faCompression)
    {
      this.cboCompression1.addItem(lcCompression);
      this.cboCompression2.addItem(lcCompression);
    }
  }

  // ---------------------------------------------------------------------------
  private void setupButtons()
  {
    this.btnOK1.setText("OK");
    this.btnOK1.setMnemonic('O');
    this.btnOK1.setToolTipText("Save any changes made to options");

    this.btnCancel1.setText("Cancel");
    this.btnCancel1.setMnemonic('C');
    this.btnCancel1.setToolTipText("Cancel any changes made to options");
  }

  // ---------------------------------------------------------------------------
  private void setupTabPanes()
  {
    this.tabPane1.add("Archiving", this.pnlArchiving1);
    this.tabPane1.add("QuickZip", this.pnlQuickZip1);
  }

  // ---------------------------------------------------------------------------
  private void getMainFormVariables() throws Exception
  {
    if (this.cboCompression1.getItemCount() <= 0)
    {
      throw new Exception("cboCompression1 has not yet been initialized.");
    }

    if (this.cboCompression2.getItemCount() <= 0)
    {
      throw new Exception("cboCompression2 has not yet been initialized.");
    }

    final MainFrame loMain = this.getMainFrame();
    int lnIndex = loMain.foAppProperties.getOptionCompressionLevelBZ();
    if ((lnIndex < 0) || (lnIndex >= this.cboCompression1.getItemCount()))
    {
      lnIndex = 5;
    }
    this.cboCompression1.setSelectedIndex(lnIndex);
    this.chkIncludeHiddenDirectories1.setSelected(loMain.foAppProperties.getOptionIncludeHiddenDirectoriesBZ());
    this.chkIncludeHiddenFiles1.setSelected(loMain.foAppProperties.getOptionIncludeHiddenFilesBZ());

    lnIndex = loMain.foAppProperties.getOptionCompressionLevelQZ();
    if ((lnIndex < 0) || (lnIndex >= this.cboCompression2.getItemCount()))
    {
      lnIndex = 5;
    }
    this.cboCompression2.setSelectedIndex(lnIndex);
    this.chkIncludeHiddenDirectories2.setSelected(loMain.foAppProperties.getOptionIncludeHiddenDirectoriesQZ());
    this.chkIncludeHiddenFiles2.setSelected(loMain.foAppProperties.getOptionIncludeHiddenFilesQZ());
  }

  // ---------------------------------------------------------------------------
  // These variables are saved in the setProperties of MainFrame.
  private void setMainFormVariables()
  {
    final MainFrame loMain = this.getMainFrame();

    loMain.foAppProperties.setOptionCompressionLevelBZ(this.cboCompression1.getSelectedIndex());
    loMain.foAppProperties.setOptionIncludeHiddenDirectoriesBZ(this.chkIncludeHiddenDirectories1.isSelected());
    loMain.foAppProperties.setOptionIncludeHiddenFilesBZ(this.chkIncludeHiddenFiles1.isSelected());

    final String lcAction = this.foButtonGroup1.getSelection().getActionCommand();

    for (final ArchiveFileNameParameters loArchiveParameter : this.faArchiveParameters)
    {
      if (loArchiveParameter.fcLabel.compareToIgnoreCase(lcAction) == 0)
      {
        loMain.foAppProperties.setOptionArchiveNameTypeBZ(loArchiveParameter.fnValue);
        break;
      }
    }

    loMain.foAppProperties.setOptionCompressionLevelQZ(this.cboCompression2.getSelectedIndex());
    loMain.foAppProperties.setOptionIncludeHiddenDirectoriesQZ(this.chkIncludeHiddenDirectories2.isSelected());
    loMain.foAppProperties.setOptionIncludeHiddenFilesQZ(this.chkIncludeHiddenFiles2.isSelected());
  }

  // ---------------------------------------------------------------------------
  private void getProperties()
  {
    final BaseProperties loProp = this.getMainFrame().foAppProperties;

    this.tabPane1.setSelectedIndex(loProp.getProperty(this.fcPropertyPrefix + " tabPane1", 0));
  }

  // ---------------------------------------------------------------------------
  private void setProperties()
  {
    final BaseProperties loProp = this.getMainFrame().foAppProperties;

    loProp.setProperty(this.fcPropertyPrefix + " tabPane1", this.tabPane1.getSelectedIndex());
  }

  // ---------------------------------------------------------------------------
  @Override
  public void removeListeners()
  {
    this.btnOK1.removeActionListener(this);
    this.btnCancel1.removeActionListener(this);

    final Enumeration<AbstractButton> loE = this.foButtonGroup1.getElements();
    while (loE.hasMoreElements())
    {
      final AbstractButton loAbstractButton = loE.nextElement();
      if (loAbstractButton instanceof JRadioButton)
      {
        loAbstractButton.removeActionListener(this);
      }
    }
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
        this.setMainFormVariables();
        this.setProperties();
        this.closeWindow();
      }
      else if (loSource == this.btnCancel1)
      {
        this.closeWindow();
      }
    }
    else if (loSource instanceof JRadioButton)
    {
      final JRadioButton loRadio = (JRadioButton) loSource;
      for (final ArchiveFileNameParameters loArchiveParameter : this.faArchiveParameters)
      {
        if (loRadio.getText().compareToIgnoreCase(loArchiveParameter.fcLabel) == 0)
        {
          this.lblSampleArchiveName1.setText(this.getMainFrame().buildArchiveFileName(loArchiveParameter.fnValue));
          break;
        }
      }
    }

  }
  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
}

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
class ArchiveFileNameParameters
{
  protected String fcLabel;
  protected int fnValue;
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
