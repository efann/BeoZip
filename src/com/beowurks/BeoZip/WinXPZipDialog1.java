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

import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class WinXPZipDialog1 extends BeoZipBaseDialog implements ActionListener
{
  private static final String ENABLE_ZIPSUPPORT = "Enable Windows XP Zip Support";
  private static final String DISABLE_ZIPSUPPORT = "Disable Windows XP Zip Support";

  private final String fcPropertyPrefix = this.getClass().getName();

  private final BaseButton btnApply1 = new BaseButton();
  private final BaseButton btnClose1 = new BaseButton();

  private final ButtonGroup foButtonGroup1 = new ButtonGroup();
  private JPanel pnlOptions1 = null;

  private final Box boxButtons1 = Box.createHorizontalBox();

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public WinXPZipDialog1(final MainFrame toMainFrame, final String tcTitle)
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
    this.setupButtons();
    this.setupListeners();
    this.setupLayouts();

    this.getProperties();
  }

  // ---------------------------------------------------------------------------
  private void setupLayouts()
  {
    this.boxButtons1.add(this.btnApply1, null);
    this.boxButtons1.add(this.btnClose1, null);

    final GridBagLayoutHelper loGrid;
    loGrid = new GridBagLayoutHelper();

    this.getContentPane().setLayout(loGrid);

    loGrid.setInsets(20, 40, 20, 40);

    this.getContentPane().add(this.pnlOptions1, loGrid.getConstraint(0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE));
    this.getContentPane().add(this.boxButtons1, loGrid.getConstraint(0, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE));
  }

  // ---------------------------------------------------------------------------
  private void setupOptionGroups() throws Exception
  {
    if (this.pnlOptions1 == null)
    {
      throw new Exception("The panels have not been initialized in the routine setupPanels in OptionsDialog1.");
    }

    JRadioButton loRadio;

    loRadio = new JRadioButton(WinXPZipDialog1.ENABLE_ZIPSUPPORT);
    loRadio.setCursor(new Cursor(Cursor.HAND_CURSOR));
    loRadio.setSelected(true);
    loRadio.setActionCommand(loRadio.getText());

    this.foButtonGroup1.add(loRadio);
    this.pnlOptions1.add(loRadio);

    loRadio = new JRadioButton(WinXPZipDialog1.DISABLE_ZIPSUPPORT);
    loRadio.setCursor(new Cursor(Cursor.HAND_CURSOR));
    loRadio.setSelected(true);
    loRadio.setActionCommand(loRadio.getText());

    this.foButtonGroup1.add(loRadio);
    this.pnlOptions1.add(loRadio);
  }

  // ---------------------------------------------------------------------------
  private void setupPanels()
  {
    final Border loBorder = new CompoundBorder(new SoftBevelBorder(BevelBorder.LOWERED), new EmptyBorder(5, 5, 5, 5));

    this.pnlOptions1 = new JPanel();
    this.pnlOptions1.setLayout(new BoxLayout(this.pnlOptions1, BoxLayout.Y_AXIS));
    this.pnlOptions1.setAlignmentY(Component.TOP_ALIGNMENT);
    this.pnlOptions1.setAlignmentX(Component.LEFT_ALIGNMENT);
    this.pnlOptions1.setBorder(loBorder);
  }

  // ---------------------------------------------------------------------------
  private void setupListeners()
  {
    this.btnApply1.addActionListener(this);
    this.btnClose1.addActionListener(this);

    final Enumeration<AbstractButton> loE = this.foButtonGroup1.getElements();
    while (loE.hasMoreElements())
    {
      final AbstractButton loAbstractButton = loE.nextElement();
      if (loAbstractButton instanceof JRadioButton)
      {
        ((JRadioButton) loAbstractButton).addActionListener(this);
      }
    }
  }

  // ---------------------------------------------------------------------------
  private void setupButtons()
  {
    this.btnApply1.setText("Apply");
    this.btnApply1.setMnemonic('A');
    this.btnApply1.setToolTipText("Apply these settings");

    this.btnClose1.setText("Close");
    this.btnClose1.setMnemonic('C');
    this.btnClose1.setToolTipText("Close this dialog");
  }

  // ---------------------------------------------------------------------------
  private void getProperties()
  {
    final BaseProperties loProp = this.getMainFrame().foAppProperties;
    final String lcSelected = loProp.getProperty(this.fcPropertyPrefix + " foButtonGroup1", WinXPZipDialog1.ENABLE_ZIPSUPPORT);

    final Enumeration<AbstractButton> loEnum = this.foButtonGroup1.getElements();
    while (loEnum.hasMoreElements())
    {
      final AbstractButton loAbstractButton = loEnum.nextElement();
      if (loAbstractButton instanceof JRadioButton)
      {
        final JRadioButton loRadio = (JRadioButton) loAbstractButton;
        loRadio.setSelected(loRadio.getActionCommand().compareToIgnoreCase(lcSelected) == 0);
      }
    }
  }

  // ---------------------------------------------------------------------------
  private void setProperties()
  {
    final BaseProperties loProp = this.getMainFrame().foAppProperties;

    loProp.setProperty(this.fcPropertyPrefix + " foButtonGroup1", this.foButtonGroup1.getSelection().getActionCommand());
  }

  // ---------------------------------------------------------------------------
  private void runXPZipSupportFile(final boolean tlEnable)
  {
    final String lcFileName = Global.LOCAL_PATH + (tlEnable ? "enableXPZip.bat" : "disableXPZip.bat");
    boolean llOkay = true;

    try
    {
      final DataOutputStream loOutStream = new DataOutputStream(new FileOutputStream(new File(lcFileName)));

      if (tlEnable)
      {
        loOutStream.writeBytes("regsvr32 %windir%\\system32\\zipfldr.dll\n");
      }
      else
      {
        loOutStream.writeBytes("regsvr32 /u %windir%\\system32\\zipfldr.dll\n");
      }

      loOutStream.flush();
      loOutStream.close();
    }
    catch (final IOException loErr)
    {
      Util.infoMessage(this, "There was a problem in generating " + lcFileName + "\n\n" + loErr.getMessage());
      llOkay = false;
    }

    if (llOkay)
    {
      try
      {
        Runtime.getRuntime().exec("cmd /c " + "\"" + lcFileName + "\"");
      }
      catch (final IOException loErr)
      {
        Util.infoMessage(this, "There was a problem in running " + lcFileName + "\n\n" + loErr.getMessage());
      }
    }
  }

  // ---------------------------------------------------------------------------
  @Override
  public void removeListeners()
  {
    this.btnApply1.removeActionListener(this);
    this.btnClose1.removeActionListener(this);

    final Enumeration<AbstractButton> loE = this.foButtonGroup1.getElements();
    while (loE.hasMoreElements())
    {
      final AbstractButton loAbstractButton = loE.nextElement();
      if (loAbstractButton instanceof JRadioButton)
      {
        ((JRadioButton) loAbstractButton).removeActionListener(this);
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
      if (loSource == this.btnApply1)
      {
        this.runXPZipSupportFile(this.foButtonGroup1.getSelection().getActionCommand().compareToIgnoreCase(WinXPZipDialog1.ENABLE_ZIPSUPPORT) == 0);

        this.setProperties();
      }
      else if (loSource == this.btnClose1)
      {
        this.closeWindow();
      }
    }
  }
  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
