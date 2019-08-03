/*
 * BeoZip : a simple archiving application for the Java(tm) Swing platform previously written in C++.
 *
 * Copyright(c) 2001-2019, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.BeoZip;

import com.beowurks.BeoCommon.BaseButton;
import com.beowurks.BeoCommon.BaseProperties;
import com.beowurks.BeoCommon.GridBagLayoutHelper;
import com.beowurks.BeoZippin.ZipTable;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Comparator;
import java.util.Vector;
import java.util.regex.Pattern;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class FileMaskSelectorDialog1 extends BeoZipBaseDialog implements ActionListener, Runnable, Comparator<String>
{
  static private final int ACTION_SELECT = 1;
  static private final int ACTION_DESELECT = 2;

  private Pattern foPattern;

  private final String fcPropertyPrefix = this.getClass().getName();

  private ZipTable foZipTable = null;

  private final Vector<String> foDirectoryListing = new Vector<>();

  private final JLabel lblFileMask1 = new JLabel();

  private final BaseButton btnOK1 = new BaseButton();
  private final BaseButton btnCancel1 = new BaseButton();
  private final JComboBox cboFileMasks1 = new JComboBox();
  private final Box boxButtons1 = Box.createHorizontalBox();

  private final ButtonGroup opgSelectAction1 = new ButtonGroup();
  private final JRadioButton radSelectAction1 = new JRadioButton();
  private final JRadioButton radSelectAction2 = new JRadioButton();
  private final JPanel pnlSelectAction1 = new JPanel();

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public FileMaskSelectorDialog1(final MainFrame toMainFrame, final ZipTable toZipTable)
  {
    super(toMainFrame, "Select/Deselect Files");

    this.foZipTable = toZipTable;

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
  @Override
  protected void jbInit() throws Exception
  {
    super.jbInit();

    this.setupLabels();
    this.setupOptionGroups();
    this.setupComboBoxes();
    this.setupButtons();

    this.setupListeners();
    this.setupLayouts();

    this.getProperties();
  }

  // ---------------------------------------------------------------------------
  private void setupLayouts()
  {
    this.pnlSelectAction1.setLayout(new GridLayout(0, 1));
    this.pnlSelectAction1.add(this.radSelectAction1);
    this.pnlSelectAction1.add(this.radSelectAction2);

    this.boxButtons1.add(this.btnOK1, null);
    this.boxButtons1.add(this.btnCancel1, null);

    final GridBagLayoutHelper loGrid = new GridBagLayoutHelper();

    this.getContentPane().setLayout(loGrid);

    loGrid.setInsets(10, 10, 1, 10);
    this.getContentPane().add(this.lblFileMask1, loGrid.getConstraint(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE));
    loGrid.setInsets(1, 10, 10, 10);
    this.getContentPane().add(this.cboFileMasks1, loGrid.getConstraint(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));
    loGrid.setInsets(10, 10, 20, 10);
    this.getContentPane().add(this.pnlSelectAction1, loGrid.getConstraint(0, 2, GridBagConstraints.CENTER, GridBagConstraints.NONE));
    loGrid.setInsets(20, 10, 10, 10);
    this.getContentPane().add(this.boxButtons1, loGrid.getConstraint(0, 3, GridBagConstraints.CENTER, GridBagConstraints.NONE));
  }

  // ---------------------------------------------------------------------------
  private void setupOptionGroups()
  {
    this.radSelectAction1.setText("Select files matching the file mask");
    this.radSelectAction2.setText("Deselect files matching the file mask");

    this.opgSelectAction1.add(this.radSelectAction1);
    this.opgSelectAction1.add(this.radSelectAction2);
  }

  // ---------------------------------------------------------------------------
  private void setupComboBoxes()
  {
    final Dimension ldSize = new Dimension(450, (int) this.cboFileMasks1.getPreferredSize().getHeight());

    this.cboFileMasks1.setMinimumSize(ldSize);
    this.cboFileMasks1.setPreferredSize(ldSize);

    this.cboFileMasks1.setEditable(true);

    this.foDirectoryListing.clear();
    final int lnRows = this.foZipTable.getRowCount();
    for (int i = 0; i < lnRows; ++i)
    {
      final String lcMask = this.foZipTable.extractDirectory(i) + "*.*";
      if (!this.foDirectoryListing.contains(lcMask))
      {
        this.foDirectoryListing.addElement(lcMask);
      }
    }

    this.foDirectoryListing.sort(this);

    final int lnLen = this.foDirectoryListing.size();
    for (int i = 0; i < lnLen; ++i)
    {
      this.cboFileMasks1.addItem(this.foDirectoryListing.elementAt(i));
    }
  }

  // ---------------------------------------------------------------------------
  private void setupLabels()
  {
    this.lblFileMask1.setText("File Mask");
    this.lblFileMask1.setHorizontalAlignment(SwingConstants.LEFT);
  }

  // ---------------------------------------------------------------------------
  private void setupButtons()
  {
    this.btnOK1.setText("OK");
    this.btnOK1.setMnemonic('O');
    this.btnOK1.setToolTipText("Select the files in the current zip file according to the file mask");

    this.btnCancel1.setText("Cancel");
    this.btnCancel1.setMnemonic('C');
    this.btnCancel1.setToolTipText("Cancel selecting any files");
  }

  // ---------------------------------------------------------------------------
  private void setupListeners()
  {
    this.btnOK1.addActionListener(this);
    this.btnCancel1.addActionListener(this);
  }

  // ---------------------------------------------------------------------------
  private int getSelectAction()
  {
    int lnIndex = FileMaskSelectorDialog1.ACTION_SELECT;
    if (this.radSelectAction1.isSelected())
    {
      lnIndex = FileMaskSelectorDialog1.ACTION_SELECT;
    }
    else if (this.radSelectAction2.isSelected())
    {
      lnIndex = FileMaskSelectorDialog1.ACTION_DESELECT;
    }

    return (lnIndex);
  }

  // ---------------------------------------------------------------------------
  private void getProperties()
  {
    final BaseProperties loProp = this.getMainFrame().foAppProperties;

    final int lnIndex = loProp.getProperty(this.fcPropertyPrefix + " opgSelectAction1", FileMaskSelectorDialog1.ACTION_SELECT);

    this.radSelectAction1.setSelected(true);
    if (lnIndex == FileMaskSelectorDialog1.ACTION_SELECT)
    {
      this.radSelectAction1.setSelected(true);
    }
    else if (lnIndex == FileMaskSelectorDialog1.ACTION_DESELECT)
    {
      this.radSelectAction2.setSelected(true);
    }
  }

  // ---------------------------------------------------------------------------
  private void setProperties()
  {
    final BaseProperties loProp = this.getMainFrame().foAppProperties;

    loProp.setProperty(this.fcPropertyPrefix + " opgSelectAction1", this.getSelectAction());
  }

  // ---------------------------------------------------------------------------
  public String getFileMask()
  {
    return (this.cboFileMasks1.getSelectedItem().toString());
  }

  // ---------------------------------------------------------------------------
  @Override
  public void removeListeners()
  {
    this.btnOK1.removeActionListener(this);
    this.btnCancel1.removeActionListener(this);

    super.removeListeners();
  }

  // ---------------------------------------------------------------------------
  @Override
  public void releasePointers()
  {
    this.foZipTable = null;

    super.removeListeners();
  }

  // ---------------------------------------------------------------------------
  // This section of code is from GlobFilter.setPattern located
  // in javax/swing/plaf/basic/BasicFileChooserUI.java.
  // I've modified it to fit within my coding style.
  public void setPattern(final String tcGlobPattern)
  {
    final char[] laGlobPattern = tcGlobPattern.toCharArray();
    final char[] laRevisedPattern = new char[laGlobPattern.length * 2];
    final boolean llWin32 = (File.separatorChar == '\\');
    boolean llBetweenBrackets = false;

    int j = 0;

    if (llWin32)
    {
      // On windows, a pattern ending with *.* is equal to ending with *
      int lnLength = laGlobPattern.length;
      if (tcGlobPattern.endsWith("*.*"))
      {
        lnLength -= 2;
      }
      for (int i = 0; i < lnLength; i++)
      {
        switch (laGlobPattern[i])
        {
          case '*':
            laRevisedPattern[j++] = '.';
            laRevisedPattern[j++] = '*';
            break;

          case '?':
            laRevisedPattern[j++] = '.';
            break;

          case '\\':
            laRevisedPattern[j++] = '\\';
            laRevisedPattern[j++] = '\\';
            break;

          default:
            if ("+()^$.{}[]".indexOf(laGlobPattern[i]) >= 0)
            {
              laRevisedPattern[j++] = '\\';
            }
            laRevisedPattern[j++] = laGlobPattern[i];
            break;
        }
      }
    }
    else
    {
      for (int i = 0; i < laGlobPattern.length; i++)
      {
        switch (laGlobPattern[i])
        {
          case '*':
            if (!llBetweenBrackets)
            {
              laRevisedPattern[j++] = '.';
            }
            laRevisedPattern[j++] = '*';
            break;

          case '?':
            laRevisedPattern[j++] = llBetweenBrackets ? '?' : '.';
            break;

          case '[':
            llBetweenBrackets = true;
            laRevisedPattern[j++] = laGlobPattern[i];

            if (i < laGlobPattern.length - 1)
            {
              switch (laGlobPattern[i + 1])
              {
                case '!':
                case '^':
                  laRevisedPattern[j++] = '^';
                  i++;
                  break;

                case ']':
                  laRevisedPattern[j++] = laGlobPattern[++i];
                  break;
              }
            }
            break;

          case ']':
            laRevisedPattern[j++] = laGlobPattern[i];
            llBetweenBrackets = false;
            break;

          case '\\':
            if ((i == 0) && (laGlobPattern.length > 1) && (laGlobPattern[1] == '~'))
            {
              laRevisedPattern[j++] = laGlobPattern[++i];
            }
            else
            {
              laRevisedPattern[j++] = '\\';
              if ((i < laGlobPattern.length - 1) && ("*?[]".indexOf(laGlobPattern[i + 1]) >= 0))
              {
                laRevisedPattern[j++] = laGlobPattern[++i];
              }
              else
              {
                laRevisedPattern[j++] = '\\';
              }
            }
            break;

          default:
            if (!Character.isLetterOrDigit(laGlobPattern[i]))
            {
              laRevisedPattern[j++] = '\\';
            }
            laRevisedPattern[j++] = laGlobPattern[i];
            break;
        }
      }
    }

    this.foPattern = Pattern.compile(new String(laRevisedPattern, 0, j), Pattern.CASE_INSENSITIVE);
  }

  // ---------------------------------------------------------------------------
  public boolean accept(final String tcFile)
  {
    if (tcFile == null)
    {
      return false;
    }

    return (this.foPattern.matcher(tcFile).matches());
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface Comparator
  // ---------------------------------------------------------------------------
  // I use this routine to ensure that the directories and their sub-directories
  // are grouped together.
  @Override
  public int compare(final String tcValue1, final String tcValue2)
  {
    if ((tcValue1 == null) && (tcValue2 == null))
    {
      return (0);
    }
    else if (tcValue1 == null)
    {
      return (-1);
    }
    else if (tcValue2 == null)
    {
      return (1);
    }

    return (tcValue1.compareToIgnoreCase(tcValue2));
  }

  // ---------------------------------------------------------------------------
  // I've decided not to override the equals routine.
  // From the help documentation:
  // Note that it is always safe NOT to override Object.equals(Object). However,
  // if you do override it, remember that it should return true only if
  // the specified object is also a comparator and it imposes the same ordering
  // as this comparator.
  //
  // For example,
  //
  // return ((obj == this) || (obj instanceof BuildFileList));
  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface ActionListener
  // ---------------------------------------------------------------------------
  @Override
  public void actionPerformed(final ActionEvent e)
  {
    final Object loSource = e.getSource();

    if (loSource == this.btnOK1)
    {
      // At the end of the thread, the window will be closed and properties set.
      // Otherwise, if you close the window after starting the thread, releasePointers
      // is called and somewhere in the middle of the thread, foZipTable is set to
      // null which really causes some problems.
      new Thread(this).start();
    }
    else if (loSource == this.btnCancel1)
    {
      this.closeWindow();
    }
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Interface Runnable
  // ---------------------------------------------------------------------------
  @Override
  public void run()
  {
    this.setBusy(true);

    final String lcMask = this.getFileMask();
    final int lnAction = this.getSelectAction();
    final boolean llSelect = lnAction == FileMaskSelectorDialog1.ACTION_SELECT;

    this.setPattern(lcMask);
    final int lnRows = this.foZipTable.getRowCount();
    for (int i = 0; i < lnRows; ++i)
    {
      final String lcFile = this.foZipTable.getFullPath(i);
      if (this.accept(lcFile))
      {
        if (llSelect)
        {
          this.foZipTable.addRowSelectionInterval(i, i);
        }
        else
        {
          this.foZipTable.removeRowSelectionInterval(i, i);
        }
      }
    }

    this.setBusy(false);

    // Otherwise, if you close the window after starting the thread, releasePointers
    // is called and somewhere in the middle of the thread, foZipTable is set to
    // null which really causes some problems.
    this.setProperties();
    this.closeWindow();
  }
  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
