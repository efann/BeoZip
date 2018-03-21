/*
 * BeoZip : a simple archiving application for the Java(tm) Swing platform previously written in C++.
 *
 * Copyright(c) 2001-2018, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.BeoZip;

import com.beowurks.BeoCommon.BaseButton;
import com.beowurks.BeoCommon.BaseFrame;
import com.beowurks.BeoCommon.CancelDialog;
import com.beowurks.BeoCommon.Dialogs.About.AboutAdapter;
import com.beowurks.BeoCommon.Dialogs.About.DialogAbout;
import com.beowurks.BeoCommon.Dialogs.About.IAbout;
import com.beowurks.BeoCommon.Dialogs.Credits.CreditAdapter;
import com.beowurks.BeoCommon.Dialogs.Credits.DialogCredits;
import com.beowurks.BeoCommon.Dialogs.Credits.ICredit;
import com.beowurks.BeoCommon.GridBagLayoutHelper;
import com.beowurks.BeoCommon.Util;
import com.beowurks.BeoLookFeel.LFDialog;
import com.beowurks.BeoTable.SortingTable;
import com.beowurks.BeoTable.SortingTableModel;
import com.beowurks.BeoZippin.IZipProgressComponents;
import com.beowurks.BeoZippin.ThreadCompile;
import com.beowurks.BeoZippin.ThreadDelete;
import com.beowurks.BeoZippin.ThreadExtract;
import com.beowurks.BeoZippin.ThreadPopulateZipTable;
import com.beowurks.BeoZippin.ThreadTest;
import com.beowurks.BeoZippin.ZipComment;
import com.beowurks.BeoZippin.ZipTable;
import com.beowurks.apple.eawt.IOSXAdapter;
import com.beowurks.apple.eawt.OSXAdapterHelper;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumnModel;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class MainFrame extends BeoZipBaseFrame implements ActionListener, ChangeListener, MouseMotionListener, KeyListener, MouseListener,
        IZipProgressComponents, IOSXAdapter
{
  protected static final int TARGET_BEOZIP = 0;
  protected static final int TARGET_QUICKZIP = 1;

  protected static final int ARCHIVE_MEMO_WIDTH = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.55);
  protected static final int ARCHIVE_MEMO_WIDTH_THIRD = (int) (MainFrame.ARCHIVE_MEMO_WIDTH / 3.0);
  protected static final int ARCHIVE_MEMO_HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.35);

  protected final StringBuilder fcQuickZipFileName = new StringBuilder(256);
  protected final StringBuilder fcBeoZipRestoreFileName = new StringBuilder(256);
  protected final JLabel barStatus1 = new JLabel();

  protected final AppProperties foAppProperties;

  protected final SelectFilesChooser foSelectFilesChooser = new SelectFilesChooser(this);

  private final JMenuBar menuBar1 = new JMenuBar();
  private final JMenu menuFile1 = new JMenu();
  private final JMenu menuActions1 = new JMenu();
  private final JMenu menuSelections1 = new JMenu();
  private final JMenu menuView1 = new JMenu();
  private final JMenu menuTools1 = new JMenu();
  private final JMenu menuHelp1 = new JMenu();

  private final JMenuItem menuNew1 = new JMenuItem();
  private final JMenuItem menuOpen1 = new JMenuItem();
  private final JMenuItem menuExit1 = new JMenuItem();

  private final JMenuItem menuArchive1 = new JMenuItem();
  private final JMenuItem menuRestore1 = new JMenuItem();
  private final JMenuItem menuArchivesLocation1 = new JMenuItem();
  private final JMenuItem menuFoldersToArchive1 = new JMenuItem();
  private final JMenuItem menuRescue1 = new JMenuItem();

  private final JMenuItem menuAdd1 = new JMenuItem();
  private final JMenuItem menuDelete1 = new JMenuItem();
  private final JMenuItem menuExtract1 = new JMenuItem();
  private final JMenuItem menuTest1 = new JMenuItem();

  private final JMenuItem menuSelectAll1 = new JMenuItem();
  private final JMenuItem menuClearSelect1 = new JMenuItem();
  private final JMenuItem menuSelectDeselect1 = new JMenuItem();

  private final JCheckBoxMenuItem menuModified1 = new JCheckBoxMenuItem();
  private final JCheckBoxMenuItem menuSize1 = new JCheckBoxMenuItem();
  private final JCheckBoxMenuItem menuRatio1 = new JCheckBoxMenuItem();
  private final JCheckBoxMenuItem menuPacked1 = new JCheckBoxMenuItem();
  private final JCheckBoxMenuItem menuCRC321 = new JCheckBoxMenuItem();
  private final JCheckBoxMenuItem menuMethod1 = new JCheckBoxMenuItem();
  private final JCheckBoxMenuItem menuPath1 = new JCheckBoxMenuItem();

  private final JMenuItem menuLookFeel1 = new JMenuItem();
  private final JMenuItem menuOptions1 = new JMenuItem();
  private final JMenuItem menuWinXPZip1 = new JMenuItem();
  private final JMenuItem menuJavaManager1 = new JMenuItem();

  private final JMenuItem menuCredits1 = new JMenuItem();
  private final JMenuItem menuDocumentation1 = new JMenuItem();
  private final JMenuItem menuAbout1 = new JMenuItem();

  private final TabbedPaneImage tabMain1 = new TabbedPaneImage();

  private final JSplitPane splSplitPaneBeoZip1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
  private final JPanel pnlSplit1a = new JPanel();
  private final JPanel pnlSplit1b = new JPanel();
  private final JPanel pnlBeoZip1 = new JPanel();
  private final JPanel pnlQuickZip1 = new JPanel();
  private final JPanel pnlActionProgress1 = new JPanel();

  private final StringBuilder fcNewArchiveFileName = new StringBuilder(256);

  private final JTextField txtArchiveSearchPath1 = new JTextField();
  private final JTextField txtRestoreFileNameBZ1 = new JTextField();
  private final JTextField txtArchiveFileNameQZ1 = new JTextField();

  private final SortingTable grdListOfArchives1 = new SortingTable();
  private final JScrollPane scrListOfArchives1 = new JScrollPane();

  private final ZipTable grdFilesWithinArchiveBZ1 = new ZipTable();
  private final JScrollPane scrFilesWithinArchiveBZ1 = new JScrollPane();

  private final ZipTable grdFilesWithinArchiveQZ1 = new ZipTable();
  private final JScrollPane scrFilesWithinArchiveQZ1 = new JScrollPane();

  private final JToolBar tlbBeozipOptions = new JToolBar("BeoZip Options");
  private final JToolBar tlbQuickZipOptions = new JToolBar("QuickZip Options");

  // Buttons for Archive toolbar
  private final BaseButton btnArchive1 = new BaseButton(72, 48);
  private final BaseButton btnRestore1 = new BaseButton(72, 48);
  private final BaseButton btnArchivesLocation1 = new BaseButton(72, 48);
  private final BaseButton btnFoldersToArchive1 = new BaseButton(72, 48);
  private final BaseButton btnRescue1 = new BaseButton(72, 48);

  // Buttons for QuickZip toolbar
  private final BaseButton btnNew1 = new BaseButton(72, 48);
  private final BaseButton btnOpen1 = new BaseButton(72, 48);
  private final BaseButton btnAdd1 = new BaseButton(72, 48);
  private final BaseButton btnDelete1 = new BaseButton(72, 48);
  private final BaseButton btnExtract1 = new BaseButton(72, 48);

  private final JLabel lblActionDescription1 = new JLabel();
  private final JTextField txtActionCurrentFile1 = new JTextField();
  private final JLabel lblActionCurrentFile1 = new JLabel();
  private final JLabel lblActionCurrentOperation1 = new JLabel();
  private final JProgressBar barActionCurrentFile1 = new JProgressBar(0, 100);
  private final JProgressBar barActionCurrentOperation1 = new JProgressBar(0, 100);

  private SplashScreenWindow foSplash = null;

  private final CancelDialog foCancelDialog = new CancelDialog(this, "Creating archive. . . .");

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public MainFrame(final Application toApplication)
  {
    this.foAppProperties = toApplication.foAppProperties;
    this.foSplash = toApplication.foSplash;

    this.foMainFrame = this;
    this.flPackFrame = true;

    try
    {
      this.foSplash.setProgressBar(0, "Main Frame...");

      this.jbInit();
      this.makeVisible(true);

      this.foSplash.setProgressBar(160, "Completed...");

      this.foSplash.dispose();
    }
    catch (final Exception loErr)
    {
      Util.showStackTraceInMessage(this, loErr, "Exception");
    }
  }

  // ---------------------------------------------------------------------------
  // Component initialization
  @Override
  protected void jbInit() throws Exception
  {
    // setupMenu must go before super.jbInit as super.jbInit creates the duplicate
    // menu.
    this.setupMenu(10);
    super.jbInit();

    this.setupLabels(30);
    this.setupButtons(40);
    this.setupToolBars(50);
    this.setupTextBoxes(60);
    this.setupTables(70);
    this.setupTabPanes(80);
    this.setupProgressBars(90);
    this.setupStatusBars(100);
    this.setupSplitPanes(110);
    this.setupLayouts(120);
    this.setupListeners(130);

    this.foSplash.setProgressBar(140, "Properties");

    this.setTitle(Util.getTitle());

    this.getProperties();
    this.updateAllComponents(true);

    this.foSplash.setProgressBar(150, "Initializing completed...");

    // Only sets up if running on a Mac.
    OSXAdapterHelper.setupOSXAdapter(this);
  }

  // ---------------------------------------------------------------------------
  private void setupLayouts(final int tnProgress)
  {
    this.foSplash.setProgressBar(tnProgress, "Layouts...");

    GridBagLayoutHelper loGrid;
    Dimension ldSize;

    this.scrFilesWithinArchiveBZ1.getViewport().add(this.grdFilesWithinArchiveBZ1, null);
    this.scrListOfArchives1.getViewport().add(this.grdListOfArchives1, null);
    this.scrFilesWithinArchiveQZ1.getViewport().add(this.grdFilesWithinArchiveQZ1, null);

    // --------------------------------------
    // The minimum size is being set for the SplitPane
    loGrid = new GridBagLayoutHelper();

    this.pnlSplit1a.setLayout(loGrid);
    loGrid.setInsetDefaults();

    this.pnlSplit1a.add(this.txtArchiveSearchPath1, loGrid.getConstraint(0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL));
    this.pnlSplit1a.add(this.scrListOfArchives1, loGrid.getConstraint(0, 1, GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH));

    ldSize = new Dimension(150, 150);
    this.pnlSplit1a.setMinimumSize(ldSize);

    loGrid = new GridBagLayoutHelper();

    this.pnlSplit1b.setLayout(loGrid);
    loGrid.setInsetDefaults();

    this.pnlSplit1b.add(this.txtRestoreFileNameBZ1, loGrid.getConstraint(0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL));
    this.pnlSplit1b.add(this.scrFilesWithinArchiveBZ1, loGrid.getConstraint(0, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH));

    ldSize = new Dimension(150, 150);
    this.pnlSplit1b.setMinimumSize(ldSize);

    // --------------------------------------

    this.splSplitPaneBeoZip1.setLeftComponent(this.pnlSplit1a);
    this.splSplitPaneBeoZip1.setRightComponent(this.pnlSplit1b);

    // --------------------------------------
    loGrid = new GridBagLayoutHelper();

    this.pnlActionProgress1.setLayout(loGrid);
    loGrid.setInsetDefaults();

    this.pnlActionProgress1.add(this.lblActionDescription1, loGrid.getConstraint(1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE));
    this.pnlActionProgress1.add(this.txtActionCurrentFile1, loGrid.getConstraint(1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));
    this.pnlActionProgress1.add(this.lblActionCurrentFile1, loGrid.getConstraint(0, 2, GridBagConstraints.EAST, GridBagConstraints.NONE));
    this.pnlActionProgress1.add(this.barActionCurrentFile1, loGrid.getConstraint(1, 2, GridBagConstraints.WEST, GridBagConstraints.NONE));
    this.pnlActionProgress1.add(this.lblActionCurrentOperation1, loGrid.getConstraint(0, 3, GridBagConstraints.EAST, GridBagConstraints.NONE));
    this.pnlActionProgress1.add(this.barActionCurrentOperation1, loGrid.getConstraint(1, 3, GridBagConstraints.WEST, GridBagConstraints.NONE));

    // --------------------------------------
    loGrid = new GridBagLayoutHelper();

    this.pnlBeoZip1.setLayout(loGrid);
    loGrid.setInsetDefaults();

    this.pnlBeoZip1.add(this.tlbBeozipOptions, loGrid.getConstraint(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE));
    this.pnlBeoZip1.add(this.splSplitPaneBeoZip1, loGrid.getConstraint(0, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH));

    // --------------------------------------
    loGrid = new GridBagLayoutHelper();

    this.pnlQuickZip1.setLayout(loGrid);
    loGrid.setInsetDefaults();

    this.pnlQuickZip1.add(this.tlbQuickZipOptions, loGrid.getConstraint(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE));
    this.pnlQuickZip1.add(this.txtArchiveFileNameQZ1, loGrid.getConstraint(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));
    this.pnlQuickZip1.add(this.scrFilesWithinArchiveQZ1, loGrid.getConstraint(0, 2, GridBagConstraints.WEST, GridBagConstraints.BOTH));

    // --------------------------------------
    loGrid = new GridBagLayoutHelper();

    final Container loContent = this.getContentPane();
    loContent.setLayout(loGrid);
    loGrid.setInsetDefaults();

    loContent.add(this.tabMain1, loGrid.getConstraint(0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH));
    loContent.add(this.pnlActionProgress1, loGrid.getConstraint(0, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE));
    loGrid.setInsets(0, 0, 0, 0);
    loContent.add(this.barStatus1, loGrid.getConstraint(0, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL));
  }

  // ---------------------------------------------------------------------------
  private void setupButtons(final int tnProgress) throws Exception
  {
    this.foSplash.setProgressBar(tnProgress, "Buttons...");

    if (this.menuFile1.getText().isEmpty())
    {
      throw new Exception("The menus have not yet been set for the routine setupButtons in BeoZip.");
    }

    // BeoZip ToolBar buttons.
    MainFrame.setupToolBarButtons(this.btnArchive1, this.menuArchive1);
    MainFrame.setupToolBarButtons(this.btnRestore1, this.menuRestore1);
    MainFrame.setupToolBarButtons(this.btnArchivesLocation1, this.menuArchivesLocation1);
    MainFrame.setupToolBarButtons(this.btnFoldersToArchive1, this.menuFoldersToArchive1);
    MainFrame.setupToolBarButtons(this.btnRescue1, this.menuRescue1);

    // QuickZip ToolBar buttons.
    MainFrame.setupToolBarButtons(this.btnNew1, this.menuNew1);
    MainFrame.setupToolBarButtons(this.btnOpen1, this.menuOpen1);
    MainFrame.setupToolBarButtons(this.btnAdd1, this.menuAdd1);
    MainFrame.setupToolBarButtons(this.btnDelete1, this.menuDelete1);
    MainFrame.setupToolBarButtons(this.btnExtract1, this.menuExtract1);
  }

  // ---------------------------------------------------------------------------
  private static void setupToolBarButtons(final JButton toButton, final JMenuItem toMenuItem)
  {
    toButton.setIcon(toMenuItem.getIcon());
    toButton.setText(toMenuItem.getText());
    toButton.setVerticalTextPosition(SwingConstants.BOTTOM);
    toButton.setHorizontalTextPosition(SwingConstants.CENTER);
    toButton.setMnemonic(toMenuItem.getMnemonic());
    toButton.setToolTipText(toMenuItem.getToolTipText());
  }

  // ---------------------------------------------------------------------------
  private void setupToolBars(final int tnProgress)
  {
    this.foSplash.setProgressBar(tnProgress, "Toolbars...");

    this.tlbBeozipOptions.setFloatable(false);
    this.tlbBeozipOptions.setRollover(true);

    this.tlbBeozipOptions.add(this.btnArchive1);
    this.tlbBeozipOptions.add(this.btnRestore1);
    this.tlbBeozipOptions.addSeparator();
    this.tlbBeozipOptions.add(this.btnArchivesLocation1);
    this.tlbBeozipOptions.add(this.btnFoldersToArchive1);
    this.tlbBeozipOptions.addSeparator();
    this.tlbBeozipOptions.add(this.btnRescue1);

    this.tlbQuickZipOptions.setFloatable(false);
    this.tlbQuickZipOptions.setRollover(true);

    this.tlbQuickZipOptions.add(this.btnNew1);
    this.tlbQuickZipOptions.add(this.btnOpen1);
    this.tlbQuickZipOptions.addSeparator();
    this.tlbQuickZipOptions.add(this.btnAdd1);
    this.tlbQuickZipOptions.add(this.btnDelete1);
    this.tlbQuickZipOptions.addSeparator();
    this.tlbQuickZipOptions.add(this.btnExtract1);
  }

  // ---------------------------------------------------------------------------
  private void setupSplitPanes(final int tnProgress)
  {
    this.foSplash.setProgressBar(tnProgress, "Split Panes...");

    this.splSplitPaneBeoZip1.setOneTouchExpandable(false);
    this.splSplitPaneBeoZip1.setDividerLocation(0.25);
    this.splSplitPaneBeoZip1.setResizeWeight(0.5);
  }

  // ---------------------------------------------------------------------------
  private void setupTables(final int tnProgress)
  {
    this.foSplash.setProgressBar(tnProgress, "Tables...");

    final Dimension ldSize = new Dimension(MainFrame.ARCHIVE_MEMO_WIDTH_THIRD, MainFrame.ARCHIVE_MEMO_HEIGHT);
    this.scrListOfArchives1.setPreferredSize(ldSize);
    this.scrListOfArchives1.setMinimumSize(ldSize);

    this.grdListOfArchives1.setupColumns(new Object[][]{{"File Name", ""}, {"Size", 0L}, {"Last Modified", new Date()}});
    this.grdListOfArchives1.setupHeaderRenderer();

    this.scrFilesWithinArchiveBZ1.setPreferredSize(new Dimension(MainFrame.ARCHIVE_MEMO_WIDTH, MainFrame.ARCHIVE_MEMO_HEIGHT));
    this.grdFilesWithinArchiveBZ1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    this.grdFilesWithinArchiveBZ1.setupColumns(new Object[][]{{"File Name", ""}, {"Modified", new Date()}, {"Size", 0L},
            {"Ratio (%)", (double) 0}, {"Packed", 0L}, {"CRC-32", 0L}, {"Method", ""}, {"Path", ""}});
    this.grdFilesWithinArchiveBZ1.setupHeaderRenderer();

    this.scrFilesWithinArchiveQZ1.setPreferredSize(new Dimension(MainFrame.ARCHIVE_MEMO_WIDTH, MainFrame.ARCHIVE_MEMO_HEIGHT));
    this.grdFilesWithinArchiveQZ1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    this.grdFilesWithinArchiveQZ1.setupColumns(new Object[][]{{"File Name", ""}, {"Modified", new Date()}, {"Size", 0L},
            {"Ratio (%)", (double) 0}, {"Packed", 0L}, {"CRC-32", 0L}, {"Method", ""}, {"Path", ""}});
    this.grdFilesWithinArchiveQZ1.setupHeaderRenderer();
  }

  // ---------------------------------------------------------------------------
  private void setupTextBoxes(final int tnProgress)
  {
    this.foSplash.setProgressBar(tnProgress, "Text Boxes...");

    final Dimension ldSize = new Dimension(MainFrame.ARCHIVE_MEMO_WIDTH, (int) this.txtRestoreFileNameBZ1.getPreferredSize().getHeight());

    this.txtRestoreFileNameBZ1.setText(" ");
    this.txtRestoreFileNameBZ1.setPreferredSize(ldSize);
    this.txtRestoreFileNameBZ1.setMinimumSize(ldSize);
    this.txtRestoreFileNameBZ1.setEditable(false);
    this.txtRestoreFileNameBZ1.setBackground(Util.getButtonBackground());
    this.txtRestoreFileNameBZ1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

    this.txtArchiveFileNameQZ1.setText(" ");
    this.txtArchiveFileNameQZ1.setPreferredSize(ldSize);
    this.txtArchiveFileNameQZ1.setMinimumSize(ldSize);
    this.txtArchiveFileNameQZ1.setEditable(false);
    this.txtArchiveFileNameQZ1.setBackground(Util.getButtonBackground());
    this.txtArchiveFileNameQZ1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

    this.txtActionCurrentFile1.setText(" ");
    this.txtActionCurrentFile1.setPreferredSize(ldSize);
    this.txtActionCurrentFile1.setMinimumSize(ldSize);
    this.txtActionCurrentFile1.setEditable(false);
    this.txtActionCurrentFile1.setBackground(Util.getButtonBackground());
    this.txtActionCurrentFile1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

    final Dimension ldReducedSize = new Dimension(MainFrame.ARCHIVE_MEMO_WIDTH_THIRD, (int) this.txtRestoreFileNameBZ1.getPreferredSize()
            .getHeight());

    this.txtArchiveSearchPath1.setText(" ");
    this.txtArchiveSearchPath1.setPreferredSize(ldReducedSize);
    this.txtArchiveSearchPath1.setMinimumSize(ldReducedSize);
    this.txtArchiveSearchPath1.setEditable(false);
    this.txtArchiveSearchPath1.setBackground(Util.getButtonBackground());
    this.txtArchiveSearchPath1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
  }

  // ---------------------------------------------------------------------------
  private void setupStatusBars(final int tnProgress)
  {
    this.foSplash.setProgressBar(tnProgress, "Status Bars...");

    this.barStatus1.setText("Welcome to BeoZip");
    this.barStatus1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
  }

  // ---------------------------------------------------------------------------
  private void setupProgressBars(final int tnProgress)
  {
    this.foSplash.setProgressBar(tnProgress, "Progress Bars...");

    final Dimension ldSize = new Dimension(MainFrame.ARCHIVE_MEMO_WIDTH, (int) this.barActionCurrentFile1.getPreferredSize().getHeight());

    this.barActionCurrentFile1.setPreferredSize(ldSize);
    this.barActionCurrentFile1.setMinimumSize(ldSize);
    this.barActionCurrentFile1.setValue(0);
    this.barActionCurrentFile1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    // I think it looks silly to have the Current File bar display the percentage.
    this.barActionCurrentFile1.setStringPainted(false);

    this.barActionCurrentOperation1.setPreferredSize(ldSize);
    this.barActionCurrentOperation1.setMinimumSize(ldSize);
    this.barActionCurrentOperation1.setValue(0);
    this.barActionCurrentOperation1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    this.barActionCurrentOperation1.setStringPainted(true);
  }

  // ---------------------------------------------------------------------------
  private void setupLabels(final int tnProgress)
  {
    this.foSplash.setProgressBar(tnProgress, "Labels...");

    this.lblActionDescription1.setText(" ");
    this.lblActionDescription1.setHorizontalAlignment(SwingConstants.LEFT);

    this.lblActionCurrentFile1.setText("Current File");
    this.lblActionCurrentFile1.setHorizontalAlignment(SwingConstants.LEFT);

    this.lblActionCurrentOperation1.setText("Current Operation");
    this.lblActionCurrentOperation1.setHorizontalAlignment(SwingConstants.LEFT);
  }

  // ---------------------------------------------------------------------------
  private void setupTabPanes(final int tnProgress)
  {
    this.foSplash.setProgressBar(tnProgress, "Tab Panes...");

    this.tabMain1.add("", this.pnlBeoZip1);
    this.tabMain1.addTab("", this.pnlQuickZip1);
    this.tabMain1.setTabPlacement(SwingConstants.RIGHT);

    // Always make the archiving appear first.
    this.tabMain1.setSelectedIndex(0);
  }

  // ---------------------------------------------------------------------------
  private void setupMenu(final int tnProgress)
  {
    this.foSplash.setProgressBar(tnProgress, "Menus...");

    // ----
    this.menuFile1.setText("File");
    this.menuFile1.setMnemonic('F');

    this.menuNew1.setText("New...");
    this.menuNew1.setIcon(new ImageIcon(this.getClass().getResource("images/New.gif")));
    this.menuNew1.setMnemonic('N');
    this.menuNew1.setToolTipText("Create a new archive file");

    this.menuOpen1.setText("Open...");
    this.menuOpen1.setIcon(new ImageIcon(this.getClass().getResource("images/Open.gif")));
    this.menuOpen1.setMnemonic('O');
    this.menuOpen1.setToolTipText("Open an existing archive file");

    this.menuExit1.setText("Exit");
    this.menuExit1.setMnemonic('X');
    this.menuExit1.setToolTipText("Exit BeoZip");

    // ----

    this.menuActions1.setText("Actions");
    this.menuActions1.setMnemonic('A');

    this.menuArchive1.setText("Archive");
    this.menuArchive1.setIcon(new ImageIcon(this.getClass().getResource("images/Archive.gif")));
    this.menuArchive1.setMnemonic('V');
    this.menuArchive1.setToolTipText("Archive the folders listed in 'Folders To Archive'");

    this.menuRestore1.setText("Restore...");
    this.menuRestore1.setIcon(new ImageIcon(this.getClass().getResource("images/Restore.gif")));
    this.menuRestore1.setMnemonic('S');
    this.menuRestore1.setToolTipText("Restore selected file(s) from the current archive file");

    this.menuArchivesLocation1.setText("Location...");
    this.menuArchivesLocation1.setIcon(new ImageIcon(this.getClass().getResource("images/Location.gif")));
    this.menuArchivesLocation1.setMnemonic('L');
    this.menuArchivesLocation1.setToolTipText("Select the location/folder where the archives are saved");

    this.menuFoldersToArchive1.setText("Folders...");
    this.menuFoldersToArchive1.setIcon(new ImageIcon(this.getClass().getResource("images/Folders.gif")));
    this.menuFoldersToArchive1.setMnemonic('O');
    this.menuFoldersToArchive1.setToolTipText("Dialog for selecting the folders that need to be archived");

    this.menuRescue1.setText("Rescue...");
    this.menuRescue1.setIcon(new ImageIcon(this.getClass().getResource("images/Rescue.gif")));
    this.menuRescue1.setMnemonic('C');
    this.menuRescue1.setToolTipText("Rescue the 'Folders To Archive' listing from the current archive file");

    this.menuAdd1.setText("Add...");
    this.menuAdd1.setIcon(new ImageIcon(this.getClass().getResource("images/Add.gif")));
    this.menuAdd1.setMnemonic('A');
    this.menuAdd1.setToolTipText("Add file(s) to the current archive");

    this.menuDelete1.setText("Delete");
    this.menuDelete1.setIcon(new ImageIcon(this.getClass().getResource("images/Delete.gif")));
    this.menuDelete1.setMnemonic('D');
    this.menuDelete1.setToolTipText("Delete file(s) from the current archive");

    this.menuExtract1.setText("Extract...");
    this.menuExtract1.setIcon(new ImageIcon(this.getClass().getResource("images/Extract.gif")));
    this.menuExtract1.setMnemonic('X');
    this.menuExtract1.setToolTipText("Extract selected files from the current archive file");

    this.menuTest1.setText("Test...");
    this.menuTest1.setMnemonic('T');
    this.menuTest1.setToolTipText("Test the selected file(s) from the current archive file");

    // ----
    this.menuSelections1.setText("Selections");
    this.menuSelections1.setMnemonic('S');

    this.menuSelectAll1.setText("Select All");
    this.menuSelectAll1.setMnemonic('A');
    this.menuSelectAll1.setToolTipText("Select all of the files in the current archive file");

    this.menuClearSelect1.setText("Clear Selections");
    this.menuClearSelect1.setMnemonic('C');
    this.menuClearSelect1.setToolTipText("Deselect all of the selected files in the current archive file");

    this.menuSelectDeselect1.setText("Select/Deselect Files...");
    this.menuSelectDeselect1.setMnemonic('F');
    this.menuSelectDeselect1.setToolTipText("Select or deselect files by file mask in the current archive file");

    // ----
    this.menuView1.setText("View");
    this.menuView1.setMnemonic('V');

    this.menuModified1.setText("Modified");
    this.menuModified1.setMnemonic('F');
    this.menuModified1.setToolTipText("View the 'Modified' column in the current archive file");

    this.menuSize1.setText("Size");
    this.menuSize1.setMnemonic('Z');
    this.menuSize1.setToolTipText("View the 'Size' column in the current archive file");

    this.menuRatio1.setText("Ratio");
    this.menuRatio1.setMnemonic('R');
    this.menuRatio1.setToolTipText("View the 'Ratio' column in the current archive file");

    this.menuPacked1.setText("Packed");
    this.menuPacked1.setMnemonic('P');
    this.menuPacked1.setToolTipText("View the 'Packed' column in the current archive file");

    this.menuCRC321.setText("CRC-32");
    this.menuCRC321.setMnemonic('C');
    this.menuCRC321.setToolTipText("View the 'CRC-32' column in the current archive file");

    this.menuMethod1.setText("Method");
    this.menuMethod1.setMnemonic('M');
    this.menuMethod1.setToolTipText("View the 'Method' column in the current archive file");

    this.menuPath1.setText("Path");
    this.menuPath1.setMnemonic('T');
    this.menuPath1.setToolTipText("View the 'Patch' column in the current archive file");

    // ----
    this.menuTools1.setText("Tools");
    this.menuTools1.setMnemonic('T');

    this.menuLookFeel1.setText("Look & Feel...");
    this.menuLookFeel1.setMnemonic('L');
    this.menuLookFeel1.setToolTipText("Customize the look & feel of this application");

    this.menuOptions1.setText("Options...");
    this.menuOptions1.setMnemonic('O');
    this.menuOptions1.setToolTipText("Set options specific to zipping files");

    this.menuWinXPZip1.setText("Windows XP Zip Support...");
    this.menuWinXPZip1.setMnemonic('Z');
    this.menuWinXPZip1.setToolTipText("Enable/disable Windows XP zip support");

    this.menuJavaManager1.setText("Java Application Manager");
    this.menuJavaManager1.setToolTipText("Launch the Java Application Manager");

    // ----
    this.menuHelp1.setText("Help");
    this.menuHelp1.setMnemonic('H');

    this.menuCredits1.setText("Credits");
    this.menuCredits1.setMnemonic('C');
    this.menuCredits1.setToolTipText("Credits for pieces parts used in this program");

    this.menuDocumentation1.setText("Help Documentation");
    this.menuDocumentation1.setMnemonic('D');
    this.menuDocumentation1.setToolTipText("Documentation in a PDF-format on using this program");
    this.menuDocumentation1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));


    this.menuAbout1.setText("About");
    this.menuAbout1.setMnemonic('A');
    this.menuAbout1.setToolTipText("About this program");

    // ----
    this.menuBar1.add(this.menuFile1);
    this.menuBar1.add(this.menuActions1);
    this.menuBar1.add(this.menuSelections1);
    this.menuBar1.add(this.menuView1);
    this.menuBar1.add(this.menuTools1);
    this.menuBar1.add(this.menuHelp1);

    this.menuFile1.add(this.menuNew1);
    this.menuFile1.add(this.menuOpen1);

    if (!Util.isMacintosh())
    {
      this.menuFile1.addSeparator();
      this.menuFile1.add(this.menuExit1);
    }

    this.menuActions1.add(this.menuArchive1);
    this.menuActions1.add(this.menuRestore1);
    this.menuActions1.addSeparator();
    this.menuActions1.add(this.menuArchivesLocation1);
    this.menuActions1.add(this.menuFoldersToArchive1);
    this.menuActions1.addSeparator();
    this.menuActions1.add(this.menuRescue1);
    this.menuActions1.addSeparator();
    this.menuActions1.add(this.menuAdd1);
    this.menuActions1.add(this.menuDelete1);
    this.menuActions1.add(this.menuExtract1);
    this.menuActions1.addSeparator();
    this.menuActions1.add(this.menuTest1);

    this.menuSelections1.add(this.menuSelectAll1);
    this.menuSelections1.add(this.menuClearSelect1);
    this.menuSelections1.addSeparator();
    this.menuSelections1.add(this.menuSelectDeselect1);

    this.menuView1.add(this.menuModified1);
    this.menuView1.add(this.menuSize1);
    this.menuView1.add(this.menuRatio1);
    this.menuView1.add(this.menuPacked1);
    this.menuView1.add(this.menuCRC321);
    this.menuView1.add(this.menuMethod1);
    this.menuView1.add(this.menuPath1);

    if (!Util.isMacintosh())
    {
      this.menuTools1.add(this.menuLookFeel1);
      this.menuTools1.add(this.menuOptions1);
      this.menuTools1.add(this.menuWinXPZip1);
      this.menuTools1.addSeparator();
    }

    this.menuTools1.add(this.menuJavaManager1);

    this.menuHelp1.add(this.menuCredits1);
    this.menuHelp1.addSeparator();
    this.menuHelp1.add(this.menuDocumentation1);
    this.menuHelp1.addSeparator();
    this.menuHelp1.add(this.menuAbout1);

    // ----
    this.addMenuActionCommands(this.menuBar1);
    this.setJMenuBar(this.menuBar1);
  }

  // ---------------------------------------------------------------------------
  private void setupListeners(final int tnProgress)
  {
    this.foSplash.setProgressBar(tnProgress, "Listeners...");

    this.btnArchive1.addActionListener(this);
    this.btnRestore1.addActionListener(this);
    this.btnArchivesLocation1.addActionListener(this);
    this.btnFoldersToArchive1.addActionListener(this);
    this.btnRescue1.addActionListener(this);

    this.btnNew1.addActionListener(this);
    this.btnOpen1.addActionListener(this);

    this.btnAdd1.addActionListener(this);
    this.btnDelete1.addActionListener(this);

    this.btnExtract1.addActionListener(this);

    this.tabMain1.addChangeListener(this);

    final int lnCountMenu = this.menuBar1.getMenuCount();
    for (int xx = 0; xx < lnCountMenu; ++xx)
    {
      final JMenu loMenu = this.menuBar1.getMenu(xx);
      final int lnCountComp = loMenu.getMenuComponentCount();
      for (int yy = 0; yy < lnCountComp; ++yy)
      {
        final Component loItem = loMenu.getMenuComponent(yy);
        if (loItem instanceof JMenuItem)
        {
          ((JMenuItem) loItem).addActionListener(this);
          ((JMenuItem) loItem).addMouseMotionListener(this);
        }
      }
    }

    this.grdListOfArchives1.addKeyListener(this);
    this.grdListOfArchives1.addMouseListener(this);
  }

  // ---------------------------------------------------------------------------
  private void setFoldersToArchiveToolTip()
  {
    this.btnFoldersToArchive1.setToolTipText("<html>The following folders are currently used for archiving:<br><br><i>"
            + this.getFoldersToArchiveListing(true) + "</i></html>");
  }

  // ---------------------------------------------------------------------------
  protected void updateMenus()
  {
    this.menuLookFeel1.setEnabled(!Util.isMacintosh());
    this.menuWinXPZip1.setEnabled(Util.isWindows());

    final boolean llBeoZipActive = this.isBeoZipActive();
    final boolean llQuickZipActive = this.isQuickZipActive();

    this.menuArchive1.setEnabled(llBeoZipActive);
    this.menuRestore1.setEnabled(llBeoZipActive);

    this.menuArchivesLocation1.setEnabled(llBeoZipActive);
    this.menuFoldersToArchive1.setEnabled(llBeoZipActive);

    this.menuRescue1.setEnabled(llBeoZipActive);

    this.menuNew1.setEnabled(llQuickZipActive);
    this.menuOpen1.setEnabled(llQuickZipActive);

    this.menuAdd1.setEnabled(llQuickZipActive);
    this.menuDelete1.setEnabled(llQuickZipActive);
    this.menuExtract1.setEnabled(llQuickZipActive);

    this.menuTest1.setEnabled(llBeoZipActive || llQuickZipActive);

    final boolean llMenuSelectEnabled = ((llBeoZipActive && (this.grdFilesWithinArchiveBZ1.getRowCount() > 0)) || (llQuickZipActive && (this.grdFilesWithinArchiveQZ1
            .getRowCount() > 0)));
    this.menuSelectAll1.setEnabled(llMenuSelectEnabled);
    this.menuClearSelect1.setEnabled(llMenuSelectEnabled);
    this.menuSelectDeselect1.setEnabled(llMenuSelectEnabled);
  }

  // ---------------------------------------------------------------------------
  // Overridden so we can exit when window is closed
  @Override
  protected void processWindowEvent(final WindowEvent e)
  {
    super.processWindowEvent(e);

    switch (e.getID())
    {
      case WindowEvent.WINDOW_CLOSING:
        this.performShutdownMaintenance();
        break;
    }
  }

  // ---------------------------------------------------------------------------
  private void performShutdownMaintenance()
  {
    MainFrame.runCleanupOnAllFrames();

    this.setProperties();
    this.foAppProperties.writeProperties();

    System.exit(0);
  }

  // ---------------------------------------------------------------------------
  static private void runCleanupOnAllFrames()
  {
    final Frame[] loFrames = Frame.getFrames();

    for (final Frame loXFrame : loFrames)
    {
      if (loXFrame instanceof BaseFrame)
      {
        final BaseFrame loFrame = (BaseFrame) loXFrame;
        loFrame.cleanUp();
      }
    }
  }

  // ---------------------------------------------------------------------------
  protected JMenuBar getMainMenuBar()
  {
    return (this.menuBar1);
  }

  // ---------------------------------------------------------------------------
  private void performMenuAction(final ActionEvent toEvent)
  {
    final Object loObject = toEvent.getSource();

    final String lcCommand = (loObject instanceof JMenuItem) ? ((JMenuItem) loObject).getActionCommand() : null;

    if (lcCommand == null)
    {
      return;
    }

    if (lcCommand.equals(this.menuExit1.getActionCommand()))
    {
      this.performShutdownMaintenance();
    }
    else if (lcCommand.equals(this.menuLookFeel1.getActionCommand()))
    {
      new LFDialog(BaseFrame.getActiveFrame());
    }
    else if (lcCommand.equals(this.menuCredits1.getActionCommand()))
    {
      this.showCredits();
    }
    else if (lcCommand.equals(this.menuDocumentation1.getActionCommand()))
    {
      Util.launchBrowser("https://www.beowurks.com/Software/BeoZip/Help/BZManual.pdf");
    }
    else if (lcCommand.equals(this.menuAbout1.getActionCommand()))
    {
      this.showAbout();
    }
    else if (lcCommand.equals(this.menuArchive1.getActionCommand()))
    {
      this.btnArchive1.doClick();
    }
    else if (lcCommand.equals(this.menuNew1.getActionCommand()))
    {
      this.btnNew1.doClick();
    }
    else if (lcCommand.equals(this.menuOpen1.getActionCommand()))
    {
      this.btnOpen1.doClick();
    }
    else if (lcCommand.equals(this.menuRestore1.getActionCommand()))
    {
      this.btnRestore1.doClick();
    }
    else if (lcCommand.equals(this.menuRescue1.getActionCommand()))
    {
      this.btnRescue1.doClick();
    }
    else if (lcCommand.equals(this.menuAdd1.getActionCommand()))
    {
      this.btnAdd1.doClick();
    }
    else if (lcCommand.equals(this.menuDelete1.getActionCommand()))
    {
      this.btnDelete1.doClick();
    }
    else if (lcCommand.equals(this.menuExtract1.getActionCommand()))
    {
      this.btnExtract1.doClick();
    }
    else if (lcCommand.equals(this.menuArchivesLocation1.getActionCommand()))
    {
      this.btnArchivesLocation1.doClick();
    }
    else if (lcCommand.equals(this.menuFoldersToArchive1.getActionCommand()))
    {
      this.btnFoldersToArchive1.doClick();
    }
    else if (lcCommand.equals(this.menuSelectAll1.getActionCommand()))
    {
      if (this.isBeoZipActive())
      {
        this.grdFilesWithinArchiveBZ1.selectAll();
      }
      else
      {
        this.grdFilesWithinArchiveQZ1.selectAll();
      }
    }
    else if (lcCommand.equals(this.menuClearSelect1.getActionCommand()))
    {
      if (this.isBeoZipActive())
      {
        this.grdFilesWithinArchiveBZ1.clearSelection();
      }
      else
      {
        this.grdFilesWithinArchiveQZ1.clearSelection();
      }
    }
    else if (lcCommand.equals(this.menuSelectDeselect1.getActionCommand()))
    {
      ZipTable loTable = null;
      if (this.isBeoZipActive())
      {
        loTable = this.grdFilesWithinArchiveBZ1;
      }
      else
      {
        loTable = this.grdFilesWithinArchiveQZ1;
      }

      new FileMaskSelectorDialog1(this, loTable);
    }
    else if (lcCommand.equals(this.menuModified1.getActionCommand()))
    {
      this.updateColumnVisibilityOfFilesWithinArchiveModel();
    }
    else if (lcCommand.equals(this.menuSize1.getActionCommand()))
    {
      this.updateColumnVisibilityOfFilesWithinArchiveModel();
    }
    else if (lcCommand.equals(this.menuRatio1.getActionCommand()))
    {
      this.updateColumnVisibilityOfFilesWithinArchiveModel();
    }
    else if (lcCommand.equals(this.menuPacked1.getActionCommand()))
    {
      this.updateColumnVisibilityOfFilesWithinArchiveModel();
    }
    else if (lcCommand.equals(this.menuCRC321.getActionCommand()))
    {
      this.updateColumnVisibilityOfFilesWithinArchiveModel();
    }
    else if (lcCommand.equals(this.menuMethod1.getActionCommand()))
    {
      this.updateColumnVisibilityOfFilesWithinArchiveModel();
    }
    else if (lcCommand.equals(this.menuPath1.getActionCommand()))
    {
      this.updateColumnVisibilityOfFilesWithinArchiveModel();
    }
    else if (lcCommand.equals(this.menuOptions1.getActionCommand()))
    {
      new OptionsDialog1(this, "Options");
      this.updateAllComponents(false);
    }
    else if (lcCommand.equals(this.menuWinXPZip1.getActionCommand()))
    {
      new WinXPZipDialog1(this, "Windows XP Zip Support");
    }
    else if (lcCommand.equals(this.menuJavaManager1.getActionCommand()))
    {
      Util.launchJavaApplicationManager();
    }
    else if (lcCommand.equals(this.menuTest1.getActionCommand()))
    {
      this.testZipTable();
    }
    // Repaint so the gradient background gets redrawn.
    this.repaint();

  }

  // ---------------------------------------------------------------------------
  private void performButtonAction(final ActionEvent toEvent)
  {
    final Object loButton = toEvent.getSource();

    if (loButton == this.btnArchive1)
    {
      this.pushButtonArchive();
    }
    else if (loButton == this.btnNew1)
    {
      this.pushButtonNew();
    }
    else if (loButton == this.btnOpen1)
    {
      this.pushButtonOpen();
    }
    else if (loButton == this.btnRestore1)
    {
      this.pushButtonRestore();
    }
    else if (loButton == this.btnRescue1)
    {
      this.pushButtonRescue();
    }
    else if (loButton == this.btnAdd1)
    {
      this.pushButtonAdd();
    }
    else if (loButton == this.btnDelete1)
    {
      this.pushButtonDelete();
    }
    else if (loButton == this.btnExtract1)
    {
      this.pushButtonExtract();
    }
    else if (loButton == this.btnArchivesLocation1)
    {
      this.pushButtonArchivesLocation();
    }
    else if (loButton == this.btnFoldersToArchive1)
    {
      this.pushButtonFoldersToArchive();
      this.updateAllComponents(false);
    }

    // Repaint so the gradient background gets redrawn.
    this.repaint();

  }

  // ---------------------------------------------------------------------------
  private void pushButtonFoldersToArchive()
  {
    final ArchiveFoldersDialog1 loDialog = new ArchiveFoldersDialog1(this, "Folders to Archive", this.foAppProperties.getGridFoldersToArchive());

    if (loDialog.isOkay())
    {
      this.foAppProperties.setGridFoldersToArchive(loDialog.getFolders());
    }
  }

  // ---------------------------------------------------------------------------
  private void pushButtonArchivesLocation()
  {
    final SelectFileReturnValue loValue = this.foSelectFilesChooser.selectFileDialog(this.foAppProperties.getArchivesLocation(),
            JFileChooser.DIRECTORIES_ONLY, false, this.foAppProperties.getOptionIncludeHiddenDirectoriesBZ(), false, false, "Select Archives' Location",
            "Select Folder", SelectFilesChooser.ACCESSORY_NONE);

    if (loValue != null)
    {
      final String lcFolder = loValue.SelectedFile.getPath();

      this.foAppProperties.setArchivesLocation(lcFolder);

      this.updateAllComponents(true);
    }
  }

  // ---------------------------------------------------------------------------
  private void pushButtonArchive()
  {
    if (Global.isThreadActive(ThreadCompile.class))
    {
      return;
    }

    this.updateAllComponents(false);

    final String lcFileName = this.fcNewArchiveFileName.toString();
    final File loFile = new File(lcFileName);
    if (loFile.exists())
    {
      if (!Util.yesNo(this, lcFileName + " already exists. Do you want to overwrite it?"))
      {
        return;
      }

      if (!loFile.delete())
      {
        Util.errorMessage(this, "Unable to delete the file of " + loFile.getPath() + ". It is probably open in another program.");
        return;
      }
    }

    final String[] laFoldersToArchive = this.foAppProperties.getGridFoldersToArchive();

    if ((laFoldersToArchive == null) || (laFoldersToArchive.length == 0))
    {
      Util.errorMessage(this, "You have not yet selected any folders to archive.");
      return;
    }

    Object loCallbackObject = null;
    Method lmCallbackMethod = null;
    Object[] laParameters = null;

    try
    {
      loCallbackObject = this;
      lmCallbackMethod = this.getClass().getMethod("updateAllComponents", new Class[]{Boolean.class});
      laParameters = new Object[]{Boolean.TRUE};
    }
    catch (final Exception loErr)
    {
      loCallbackObject = null;
      lmCallbackMethod = null;
      laParameters = null;

      Global.errorException(this, "There were problems in setting the callback method:<br>" + loErr.getMessage());
    }

    final ThreadCompile loThread = new ThreadCompile(this, this, loFile, this.foAppProperties.getOptionIncludeHiddenDirectoriesBZ(),
            this.foAppProperties.getOptionIncludeHiddenFilesBZ(), this.foAppProperties.getOptionCompressionLevelBZ(), laFoldersToArchive,
            ZipComment.buildBeoZipComment(laFoldersToArchive), loCallbackObject, lmCallbackMethod, laParameters);
    loThread.start();
  }

  // ---------------------------------------------------------------------------
  private void pushButtonNew()
  {
    final SelectFileReturnValue loValue = this.foSelectFilesChooser.selectFileDialog(this.foAppProperties.getPushButtonOpenFolderQZ(),
            JFileChooser.FILES_ONLY, false, this.foAppProperties.getOptionIncludeHiddenDirectoriesQZ(),
            this.foAppProperties.getOptionIncludeHiddenFilesQZ(), true, "New Archive File", "Create", SelectFilesChooser.ACCESSORY_NONE);

    if (loValue != null)
    {
      String lcFileName = loValue.SelectedFile.getPath();
      boolean llFileExists = loValue.SelectedFile.exists();

      // If the file does not exist and the user did not type an extension
      // with the file name, then add one.
      if (!llFileExists)
      {
        if (Util.extractFileExtension(lcFileName) == null)
        {
          lcFileName += Global.ZIP_EXTENSION;
          final File loFile = new File(lcFileName);
          llFileExists = loFile.exists();
        }
      }

      boolean llContinue = true;
      if (llFileExists)
      {
        llContinue = Util.yesNo(this, lcFileName + " already exists. Do you want to open this file instead?");
      }

      if (llContinue)
      {
        // Update the QuickZip FileName
        Util.clearStringBuilder(this.fcQuickZipFileName);
        this.fcQuickZipFileName.append(lcFileName);

        // Update the start directory for open folder.
        this.foAppProperties.setPushButtonOpenFolderQZ(loValue.CurrentDirectory.getPath());

        this.updateAllComponents(false);

        // If you have gotten this far, then if the file exists, open it; otherwise add
        // to the file.
        if (llFileExists)
        {
          this.populateZipTableWithFilesWithinArchive(lcFileName, MainFrame.TARGET_QUICKZIP);
        }
        else
        {
          this.btnAdd1.doClick();
        }
      }
    }

  }

  // ---------------------------------------------------------------------------
  private void pushButtonOpen()
  {
    final SelectFileReturnValue loValue = this.foSelectFilesChooser.selectFileDialog(this.foAppProperties.getPushButtonOpenFolderQZ(),
            JFileChooser.FILES_ONLY, false, this.foAppProperties.getOptionIncludeHiddenDirectoriesQZ(),
            this.foAppProperties.getOptionIncludeHiddenFilesQZ(), true, "Select Archive File", "Open", SelectFilesChooser.ACCESSORY_NONE);

    if (loValue != null)
    {
      Util.clearStringBuilder(this.fcQuickZipFileName);
      this.fcQuickZipFileName.append(loValue.SelectedFile.getPath());

      this.foAppProperties.setPushButtonOpenFolderQZ(loValue.CurrentDirectory.getPath());

      this.populateZipTableWithFilesWithinArchive(this.fcQuickZipFileName.toString(), MainFrame.TARGET_QUICKZIP);
    }

  }

  // ---------------------------------------------------------------------------
  private void pushButtonAdd()
  {
    this.addFilesToQuickZipArchive();
  }

  // ---------------------------------------------------------------------------
  private void pushButtonDelete()
  {
    this.deleteFilesFromQuickZipArchive();
  }

  // ---------------------------------------------------------------------------
  private void pushButtonRestore()
  {
    if (!this.isBeoZipRestoreFileNameSet())
    {
      Util.errorMessage(this, "Please select an archive file from which files can be restored.");
      return;
    }

    Global.loadDriveRootListIfNeeded();
    final int lnCount = Global.getRootDriveCountFromList();
    final JComboBox loComboBox = new JComboBox();
    int lnSelected = 0;
    for (int i = 0; i < lnCount; ++i)
    {
      if (Global.getRootDriveFromList(i).compareToIgnoreCase(this.foAppProperties.getBeoZipRestorePath()) == 0)
      {
        lnSelected = i;
      }

      loComboBox.addItem(Global.getRootDriveFromList(i));
    }
    loComboBox.setSelectedIndex(lnSelected);

    final int lnResults = JOptionPane.showConfirmDialog(this, new Object[]{new JLabel("Restore To Drive:"), loComboBox}, "Select Restore Drive",
            JOptionPane.OK_CANCEL_OPTION);

    if (lnResults == JOptionPane.OK_OPTION)
    {
      this.foAppProperties.setBeoZipRestorePath(loComboBox.getSelectedItem().toString());

      final ThreadExtract loThread = new ThreadExtract(this, this, this.fcBeoZipRestoreFileName.toString(), this.grdFilesWithinArchiveBZ1,
              this.foAppProperties.getBeoZipRestorePath(), true, true);
      loThread.start();
    }
  }

  // ---------------------------------------------------------------------------
  private void pushButtonExtract()
  {
    if (!this.isQuickZipFileNameSet())
    {
      Util.errorMessage(this, "Please select an archive file from which files can be extracted.");
      return;
    }

    this.foAppProperties
            .setOptionExtractionSelectAllFiles(((this.grdFilesWithinArchiveQZ1.getSelectedRowCount() == 0) || (this.grdFilesWithinArchiveQZ1
                    .getSelectedRowCount() == this.grdFilesWithinArchiveQZ1.getRowCount())));

    final SelectFileReturnValue loValue = this.foSelectFilesChooser.selectFileDialog(this.foAppProperties.getPushButtonExtractFolderQZ(),
            JFileChooser.DIRECTORIES_ONLY, false, this.foAppProperties.getOptionIncludeHiddenDirectoriesBZ(), false, false, "Extract Files", "Extract",
            SelectFilesChooser.ACCESSORY_EXTRACTION);
    if (loValue != null)
    {
      final String lcFolder = loValue.SelectedFile.getPath();

      this.foAppProperties.setPushButtonExtractFolderQZ(lcFolder);

      final ThreadExtract loThread = new ThreadExtract(this, this, this.fcQuickZipFileName.toString(), this.grdFilesWithinArchiveQZ1,
              this.foAppProperties.getPushButtonExtractFolderQZ(), this.foAppProperties.getOptionExtractionUsePathNames(),
              this.foAppProperties.getOptionExtractionOverWriteFiles());
      loThread.start();
    }
  }

  // ---------------------------------------------------------------------------
  private void pushButtonRescue()
  {
    if (!this.isBeoZipRestoreFileNameSet())
    {
      Util.errorMessage(
              this,
              new JLabel(
                      "<html><font face=\"Arial\">Please select an archive file from which the <i>Folders To Archive</i> information can be gleaned.<br></font></html>"));
      return;
    }

    final String lcFileName = this.fcBeoZipRestoreFileName.toString();
    final ZipComment loComment = new ZipComment(lcFileName);
    final String[] laFolders = loComment.getFoldersFromComments();

    if (laFolders == null)
    {
      Util.errorMessage(
              this,
              new JLabel(
                      "<html><font face=\"Arial\">Unfortunately, the file of <i><b>"
                              + lcFileName
                              + "</b></i> was either not created with this version of BeoZip<br>or has been altered outside of BeoZip in some way.<br><br>Therefore, it cannot be used to rescue the <i>Folders To Archive</i> listing.<br><br></font></html>"));
      return;
    }

    if (!Util.yesNo(this, new JLabel("<html><font face=\"Arial\">Using the zip file of <i><b>" + lcFileName
            + "</b></i>,<br>do you want to rescue the <i>Folders To Archive</i> listing?<br><br></font></html>")))
    {
      return;
    }

    this.foAppProperties.setGridFoldersToArchive(laFolders);

    Util.infoMessage(
            this,
            new JLabel("<html><font face=\"Arial\">Now the current list of folder(s) to archive is as follows:<br><br>"
                    + this.getFoldersToArchiveListing(true) + "<br></font></html>"));

  }

  // ---------------------------------------------------------------------------
  private void addFilesToQuickZipArchive()
  {
    if (Global.isThreadActive(ThreadCompile.class))
    {
      return;
    }

    if (!this.isQuickZipFileNameSet())
    {
      if (Util.yesNo(this, "As you haven't opened an archive file, you cannot add any files. Do you want to open an archive file now?"))
      {
        this.btnOpen1.doClick();
      }

      return;
    }

    final SelectFileReturnValue loValue = this.foSelectFilesChooser.selectFileDialog(this.foAppProperties.getPushButtonAddFolderQZ(),
            JFileChooser.FILES_ONLY, true, this.foAppProperties.getOptionIncludeHiddenDirectoriesQZ(),
            this.foAppProperties.getOptionIncludeHiddenFilesQZ(), false, "File(s) To Be Archived", "Add File(s)", SelectFilesChooser.ACCESSORY_QUICKZIP);

    if (loValue == null)
    {
      return;
    }

    this.foAppProperties.setPushButtonAddFolderQZ(loValue.CurrentDirectory.getPath());

    File[] laFiles = null;
    if (loValue.SelectedFiles.length > 1)
    {
      laFiles = loValue.SelectedFiles;
    }
    else
    {
      laFiles = new File[1];
      laFiles[0] = loValue.SelectedFile;
    }

    Object loCallbackObject = null;
    Method lmCallbackMethod = null;
    Object[] laParameters = null;

    try
    {
      loCallbackObject = this;
      lmCallbackMethod = this.getClass().getMethod("populateZipTableWithFilesWithinArchive", new Class[]{String.class, Integer.class});
      laParameters = new Object[]{this.fcQuickZipFileName.toString(), MainFrame.TARGET_QUICKZIP};
    }
    catch (final Exception loErr)
    {
      loCallbackObject = null;
      lmCallbackMethod = null;
      laParameters = null;

      Global.errorException(this, "There were problems in setting the callback method:<br>" + loErr.getMessage());
    }

    final ThreadCompile loThread = new ThreadCompile(this, this, new File(this.fcQuickZipFileName.toString()),
            this.foAppProperties.getOptionIncludeSubFoldersQZ(), this.foAppProperties.getOptionIncludeHiddenDirectoriesQZ(),
            this.foAppProperties.getOptionIncludeHiddenFilesQZ(), this.foAppProperties.getOptionSavePathInformationQZ(),
            this.foAppProperties.getOptionCompressionLevelQZ(), laFiles, loCallbackObject, lmCallbackMethod, laParameters);

    loThread.start();
  }

  // ---------------------------------------------------------------------------
  private void deleteFilesFromQuickZipArchive()
  {
    if (Global.isThreadActive(ThreadDelete.class))
    {
      return;
    }

    if (this.grdFilesWithinArchiveQZ1.getSelectedColumnCount() == 0)
    {
      Util.errorMessage(this, "You have not selected any files to remove.");
      return;
    }

    if (!Util.yesNo(this, "Do you want to delete the selected file(s)?"))
    {
      return;
    }

    Object loCallbackObject = null;
    Method lmCallbackMethod = null;
    Object[] laParameters = null;

    try
    {
      loCallbackObject = this;
      lmCallbackMethod = this.getClass().getMethod("populateZipTableWithFilesWithinArchive", new Class[]{String.class, Integer.class});
      laParameters = new Object[]{this.fcQuickZipFileName.toString(), MainFrame.TARGET_QUICKZIP};
    }
    catch (final Exception loErr)
    {
      loCallbackObject = null;
      lmCallbackMethod = null;
      laParameters = null;

      Global.errorException(this, "There were problems in setting the callback method:<br>" + loErr.getMessage());
    }

    final ThreadDelete loThread = new ThreadDelete(this, this, new File(this.fcQuickZipFileName.toString()), this.grdFilesWithinArchiveQZ1,
            this.foAppProperties.getOptionCompressionLevelQZ(), loCallbackObject, lmCallbackMethod, laParameters);

    loThread.start();
  }

  // ---------------------------------------------------------------------------
  private void testZipTable()
  {
    if (Global.isThreadActive(ThreadTest.class))
    {
      return;
    }

    final boolean llBeoZipActive = this.isBeoZipActive();
    final boolean llQuickZipActive = this.isQuickZipActive();

    StringBuilder lcFileName = null;
    ZipTable loZipTable = null;
    if (llBeoZipActive)
    {
      if (this.isBeoZipRestoreFileNameSet())
      {
        lcFileName = this.fcBeoZipRestoreFileName;
        loZipTable = this.grdFilesWithinArchiveBZ1;
      }
    }
    else if (llQuickZipActive)
    {
      if (this.isQuickZipFileNameSet())
      {
        lcFileName = this.fcQuickZipFileName;
        loZipTable = this.grdFilesWithinArchiveQZ1;
      }
    }

    if ((lcFileName != null) && (loZipTable != null))
    {
      final ThreadTest loThread = new ThreadTest(this, this, lcFileName, loZipTable);
      loThread.start();
    }
    else
    {
      Util.errorMessage(this, "You have not yet opened an archive file.");
    }
  }

  // ---------------------------------------------------------------------------
  private void getProperties()
  {
    final AppProperties loProp = this.foAppProperties;

    this.menuModified1.setSelected(loProp.getMenuModified());
    this.menuSize1.setSelected(loProp.getMenuSize());
    this.menuRatio1.setSelected(loProp.getMenuRatio());
    this.menuPacked1.setSelected(loProp.getMenuPacked());
    this.menuCRC321.setSelected(loProp.getMenuCRC32());
    this.menuMethod1.setSelected(loProp.getMenuMethod());
    this.menuPath1.setSelected(loProp.getMenuPath());

    this.updateColumnVisibilityOfFilesWithinArchiveModel();

  }

  // ---------------------------------------------------------------------------
  private void setProperties()
  {
    final AppProperties loProp = this.foAppProperties;

    loProp.setMenuModified(this.menuModified1.isSelected());
    loProp.setMenuSize(this.menuSize1.isSelected());
    loProp.setMenuRatio(this.menuRatio1.isSelected());
    loProp.setMenuPacked(this.menuPacked1.isSelected());
    loProp.setMenuCRC32(this.menuCRC321.isSelected());
    loProp.setMenuMethod(this.menuMethod1.isSelected());
    loProp.setMenuPath(this.menuPath1.isSelected());

    loProp.setGridListOfArchivesSort(this.grdListOfArchives1.getSortModel().getSortColumn());
    loProp.setGridListOfArchivesAscending(this.grdListOfArchives1.getSortButtonRenderer().isCurrentColumnAscending());

    loProp.setGridFilesWithinArchiveBZSort(this.grdFilesWithinArchiveBZ1.getSortModel().getSortColumn());
    loProp.setGridFilesWithinArchiveBZAscending(this.grdFilesWithinArchiveBZ1.getSortButtonRenderer().isCurrentColumnAscending());

    loProp.setGridFilesWithinArchiveQZSort(this.grdFilesWithinArchiveQZ1.getSortModel().getSortColumn());
    loProp.setGridFilesWithinArchiveQZAscending(this.grdFilesWithinArchiveQZ1.getSortButtonRenderer().isCurrentColumnAscending());

    loProp.setLookAndFeel();
    loProp.setMetalTheme();
  }

  // ---------------------------------------------------------------------------
  private void updateColumnVisibilityOfFilesWithinArchiveModel()
  {
    this.setColumnVisibility(this.menuModified1, 1);
    this.setColumnVisibility(this.menuSize1, 2);
    this.setColumnVisibility(this.menuRatio1, 3);
    this.setColumnVisibility(this.menuPacked1, 4);
    this.setColumnVisibility(this.menuCRC321, 5);
    this.setColumnVisibility(this.menuMethod1, 6);
    this.setColumnVisibility(this.menuPath1, 7);
  }

  // ---------------------------------------------------------------------------
  private void setColumnVisibility(final JCheckBoxMenuItem toItem, final int tnColumn)
  {
    TableColumnModel loModel;
    int lnColumn;

    // BeoZip grid
    loModel = this.grdFilesWithinArchiveBZ1.getColumnModel();
    lnColumn = this.grdFilesWithinArchiveBZ1.convertColumnIndexToView(tnColumn);

    loModel.getColumn(lnColumn).setMinWidth(toItem.isSelected() ? 15 : 0);
    loModel.getColumn(lnColumn).setMaxWidth(toItem.isSelected() ? Integer.MAX_VALUE : 0);
    loModel.getColumn(lnColumn).setPreferredWidth(toItem.isSelected() ? 75 : 0);

    // QuickZip grid
    loModel = this.grdFilesWithinArchiveQZ1.getColumnModel();
    lnColumn = this.grdFilesWithinArchiveQZ1.convertColumnIndexToView(tnColumn);

    loModel.getColumn(lnColumn).setMinWidth(toItem.isSelected() ? 15 : 0);
    loModel.getColumn(lnColumn).setMaxWidth(toItem.isSelected() ? Integer.MAX_VALUE : 0);
    loModel.getColumn(lnColumn).setPreferredWidth(toItem.isSelected() ? 75 : 0);
  }

  // ---------------------------------------------------------------------------
  protected String buildArchiveFileName()
  {
    return (this.buildArchiveFileName(this.foAppProperties.getOptionArchiveNameTypeBZ()));
  }

  // ---------------------------------------------------------------------------
  protected String buildArchiveFileName(final int tnArchiveNameType)
  {
    Util.clearStringBuilder(this.fcNewArchiveFileName);

    final String lcFormat;

    switch (tnArchiveNameType)
    {
      case AppProperties.ARCHIVENAME_DATEONLY:
        lcFormat = "yyyyMMdd";
        break;

      case AppProperties.ARCHIVENAME_DATETIME:
        lcFormat = "yyyyMMdd-HHmmss";
        break;

      case AppProperties.ARCHIVENAME_DATETIMEMILLISECONDS:
        lcFormat = "yyyyMMdd-HHmmss.SSS";
        break;

      default:
        lcFormat = "yyyyMMdd";

    }

    final SimpleDateFormat loFormat = new SimpleDateFormat(lcFormat);

    this.fcNewArchiveFileName.append(this.foAppProperties.getArchivesLocation());
    this.fcNewArchiveFileName.append(loFormat.format(new Date()));
    this.fcNewArchiveFileName.append(".zip");

    return (this.fcNewArchiveFileName.toString());
  }

  // ---------------------------------------------------------------------------
  public void updateAllComponents(final Boolean tlUpdateListOfArchives)
  {
    final boolean llUpdateListOfArchives = tlUpdateListOfArchives;

    this.buildArchiveFileName();

    this.txtRestoreFileNameBZ1.setText(this.fcBeoZipRestoreFileName.toString());

    this.txtArchiveSearchPath1.setText(this.foAppProperties.getArchivesLocation() + "*.zip");

    this.txtArchiveFileNameQZ1.setText((this.isQuickZipFileNameSet()) ? this.fcQuickZipFileName.toString() : " ");

    this.setFoldersToArchiveToolTip();

    if (llUpdateListOfArchives)
    {
      this.populateListOfArchives();
    }

    this.updateMenus();
  }

  // ---------------------------------------------------------------------------
  public void populateZipTableWithFilesWithinArchive(final String tcZipFileName, final Integer tnWhichZip)
  {
    ZipTable loZipTable = null;
    final int lnWhichZip = tnWhichZip;

    int lnInitialSort = -1;
    boolean llInitialAscend = false;

    final AppProperties loProp = this.foAppProperties;

    switch (lnWhichZip)
    {
      case MainFrame.TARGET_BEOZIP:
        loZipTable = this.grdFilesWithinArchiveBZ1;
        lnInitialSort = loProp.getGridFilesWithinArchiveBZSort();
        llInitialAscend = loProp.getGridFilesWithinArchiveBZAscending();
        break;

      case MainFrame.TARGET_QUICKZIP:
        loZipTable = this.grdFilesWithinArchiveQZ1;
        lnInitialSort = loProp.getGridFilesWithinArchiveQZSort();
        llInitialAscend = loProp.getGridFilesWithinArchiveQZAscending();
        break;

      default:
        Global.errorException(this, "Undefined tnWhichZip in populateZipTableWithFilesWithinArchive.");
        return;
    }

    Object loCallbackObject = null;
    Method lmCallbackMethod = null;
    Object[] laParameters = null;

    try
    {
      loCallbackObject = this;
      lmCallbackMethod = this.getClass().getMethod("updateAllComponents", new Class[]{Boolean.class});
      laParameters = new Object[]{Boolean.FALSE};
    }
    catch (final Exception loErr)
    {
      loCallbackObject = null;
      lmCallbackMethod = null;
      laParameters = null;

      Global.errorException(this, "There were problems in setting the callback method:<br>" + loErr.getMessage());
    }

    final ThreadPopulateZipTable loThread = new ThreadPopulateZipTable(this, tcZipFileName, loZipTable, lnInitialSort, llInitialAscend, true,
            loCallbackObject, lmCallbackMethod, laParameters);

    loThread.start();
  }

  // ---------------------------------------------------------------------------
  private void populateListOfArchives()
  {
    final SortingTableModel loModel = this.grdListOfArchives1.getSortModel();

    loModel.clearAll();

    final File loFile = new File(this.foAppProperties.getArchivesLocation());
    final File[] laLists = loFile.listFiles(new ListZipFilesFilter());

    if (laLists != null)
    {
      for (final File loList : laLists)
      {
        if (!loList.isFile())
        {
          continue;
        }

        loModel.addRow(new Object[]{loList.getName(), loList.length(), new Date(loList.lastModified())});
      }
    }

    final boolean llLoadFromProperties = !loModel.isSorted();

    final int lnColumn = llLoadFromProperties ? this.foAppProperties.getGridListOfArchivesSort() : loModel.getSortColumn();

    this.grdListOfArchives1.sortColumn(lnColumn, false, llLoadFromProperties ? this.foAppProperties.getGridListOfArchivesAscending()
            : this.grdListOfArchives1.getSortButtonRenderer().isCurrentColumnAscending());

    this.grdListOfArchives1.autoFitAllColumns();
  }

  // ---------------------------------------------------------------------------
  private boolean isBeoZipRestoreFileNameSet()
  {
    return (this.fcBeoZipRestoreFileName.length() > 0);
  }

  // ---------------------------------------------------------------------------
  private boolean isQuickZipFileNameSet()
  {
    return (this.fcQuickZipFileName.length() > 0);
  }

  // ---------------------------------------------------------------------------
  private String getFoldersToArchiveListing(final boolean tlUseHTML)
  {
    final StringBuilder lcString = new StringBuilder(256);
    final String[] laFoldersToArchive = this.foAppProperties.getGridFoldersToArchive();

    if (laFoldersToArchive != null)
    {
      final int lnCount = laFoldersToArchive.length;
      for (int i = 0; i < lnCount; ++i)
      {
        lcString.append(laFoldersToArchive[i]);
        if (i < (lnCount - 1))
        {
          lcString.append((lnCount < 10) ? ((tlUseHTML) ? "<br>" : "\n") : ", ");
        }
      }
    }

    return (lcString.toString());
  }

  // ---------------------------------------------------------------------------
  private String getSelectedArchive()
  {
    String lcValue = null;

    final int lnRow = this.grdListOfArchives1.getSelectedRow();
    if (lnRow >= 0)
    {
      lcValue = this.foAppProperties.getArchivesLocation() + this.grdListOfArchives1.getSortModel().getValueAt(lnRow, 0);
    }

    return (lcValue);
  }

  // ---------------------------------------------------------------------------
  private boolean setBeoZipRestoreFileName()
  {
    Util.clearStringBuilder(this.fcBeoZipRestoreFileName);

    final String lcFullFileName = this.getSelectedArchive();
    if (lcFullFileName != null)
    {
      this.fcBeoZipRestoreFileName.append(lcFullFileName);
    }

    return (this.isBeoZipRestoreFileNameSet());
  }

  // ---------------------------------------------------------------------------
  private boolean isQuickZipActive()
  {
    return (this.tabMain1.getSelectedIndex() == 1);
  }

  // ---------------------------------------------------------------------------
  private boolean isBeoZipActive()
  {
    return (this.tabMain1.getSelectedIndex() == 0);
  }

  // ---------------------------------------------------------------------------
  private void showAbout()
  {
    try
    {
      final BufferedImage loLogo = ImageIO.read(this.getClass().getResource("/com/beowurks/BeoZip/images/Logo300x300.jpg"));
      final String lcTitleURL = "http://beozip.sourceforge.net/";

      final IAbout loAbout = new AboutAdapter(Util.getTitle(), lcTitleURL,
              loLogo, lcTitleURL,
              "Eclipse Public License 2.0", "https://www.eclipse.org/legal/epl-2.0/",
              2001, "Beowurks", "https://www.beowurks.com/");

      new DialogAbout(BaseFrame.getActiveFrame(), loAbout);
    }
    catch (final Exception loError)
    {
      Util.showStackTraceInMessage(this, loError, "Oops!");
    }
  }

  // ---------------------------------------------------------------------------
  private void showCredits()
  {
    final Vector<ICredit> loVectorLinks = new Vector<>();

    loVectorLinks.add(new CreditAdapter("This program was originally written with Eclipse Java IDE.",
            "https://www.eclipse.org/"));

    loVectorLinks.add(new CreditAdapter("Code examples from the book, Swing, 2nd Edition, by Matthew Robinson & Pavel Vorobiev, gave us great tutorials on Java Swing.",
            "https://www.manning.com/books/swing-second-edition"));

    new DialogCredits(this, loVectorLinks);
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface MouseMotionListener
  // ---------------------------------------------------------------------------
  @Override
  public void mouseDragged(final MouseEvent e)
  {
  }

  // ---------------------------------------------------------------------------
  @Override
  public void mouseMoved(final MouseEvent e)
  {
    final Object loSource = e.getSource();
    if (loSource instanceof JMenuItem)
    {
      this.barStatus1.setText(((JMenuItem) loSource).getToolTipText());
    }
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface ActionListener
  // ---------------------------------------------------------------------------
  @Override
  public void actionPerformed(final ActionEvent toEvent)
  {
    final Object loSource = toEvent.getSource();

    if (loSource instanceof JMenuItem)
    {
      this.performMenuAction(toEvent);
    }
    else if (loSource instanceof JButton)
    {
      this.performButtonAction(toEvent);
    }
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface ChangeListener
  // ---------------------------------------------------------------------------
  @Override
  public void stateChanged(final ChangeEvent e)
  {
    if (e.getSource() == this.tabMain1)
    {
      this.updateAllComponents(false);
    }
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface KeyListener
  // ---------------------------------------------------------------------------
  @Override
  public void keyTyped(final KeyEvent e)
  {
  }

  // ---------------------------------------------------------------------------
  @Override
  public void keyPressed(final KeyEvent e)
  {
    if (e.getSource() != this.grdListOfArchives1)
    {
      return;
    }

    if (e.getKeyCode() == KeyEvent.VK_ENTER)
    {
      if (this.setBeoZipRestoreFileName())
      {
        this.populateZipTableWithFilesWithinArchive(this.fcBeoZipRestoreFileName.toString(), MainFrame.TARGET_BEOZIP);
      }
      e.consume();
    }
    else if (e.getKeyCode() == KeyEvent.VK_DELETE)
    {
      final String lcFullFileName = this.getSelectedArchive();
      if (lcFullFileName != null)
      {
        if (Util.yesNo(this, "Do you want to permanently delete the file of " + lcFullFileName + "?"))
        {
          final File loDeleteFile = new File(lcFullFileName);
          if (loDeleteFile.delete())
          {
            // If the currently opened file is the one deleted, then clear the name
            // and clear the grid.
            if (this.fcBeoZipRestoreFileName.toString().compareTo(lcFullFileName) == 0)
            {
              Util.clearStringBuilder(this.fcBeoZipRestoreFileName);
              this.grdFilesWithinArchiveBZ1.getSortModel().clearAll();
            }

            this.updateAllComponents(true);
            Util.infoMessage(this, "The file of " + lcFullFileName + " was successfully deleted.");
          }
          else
          {
            Util.errorMessage(this, "Unable to delete the file of " + lcFullFileName + ".");
          }
        }
      }
      e.consume();
    }
  }

  // ---------------------------------------------------------------------------
  @Override
  public void keyReleased(final KeyEvent e)
  {
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface MouseListener
  // ---------------------------------------------------------------------------
  @Override
  public void mouseClicked(final MouseEvent toEvent)
  {
    if (toEvent.getSource() != this.grdListOfArchives1)
    {
      return;
    }

    if (toEvent.getClickCount() <= 1)
    {
      return;
    }

    if (this.setBeoZipRestoreFileName())
    {
      this.populateZipTableWithFilesWithinArchive(this.fcBeoZipRestoreFileName.toString(), MainFrame.TARGET_BEOZIP);
    }

  }

  // ---------------------------------------------------------------------------
  @Override
  public void mousePressed(final MouseEvent toEvent)
  {
  }

  // ---------------------------------------------------------------------------
  @Override
  public void mouseReleased(final MouseEvent toEvent)
  {
  }

  // ---------------------------------------------------------------------------
  @Override
  public void mouseEntered(final MouseEvent toEvent)
  {
  }

  // ---------------------------------------------------------------------------
  @Override
  public void mouseExited(final MouseEvent toEvent)
  {
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface IZipProgressComponents
  // ---------------------------------------------------------------------------
  @Override
  public JLabel getActionDescriptionLabel()
  {
    return (this.lblActionDescription1);
  }

  // ---------------------------------------------------------------------------
  @Override
  public JTextField getCurrentFileTextField()
  {
    return (this.txtActionCurrentFile1);
  }

  // ---------------------------------------------------------------------------
  @Override
  public JProgressBar getCurrentFileProgressBar()
  {
    return (this.barActionCurrentFile1);
  }

  // ---------------------------------------------------------------------------
  @Override
  public JProgressBar getCurrentOperationProgressBar()
  {
    return (this.barActionCurrentOperation1);
  }

  // ---------------------------------------------------------------------------
  @Override
  public CancelDialog getCancelDialog()
  {
    return (this.foCancelDialog);
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface IOSXAdapter
  // These routines are only called when a user selects from Mac OS X application menu.
  @Override
  public void AboutHandler()
  {
    this.showAbout();
  }

  // ---------------------------------------------------------------------------
  @Override
  public void FileHandler(final String tcFileName)
  {
    this.pushButtonOpen();
  }

  // ---------------------------------------------------------------------------
  @Override
  public void PreferencesHandler()
  {
    new OptionsDialog1(this, "Preferences");
    this.updateAllComponents(false);
  }

  // ---------------------------------------------------------------------------
  @Override
  public void QuitHandler()
  {
    this.performShutdownMaintenance();
  }
  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
