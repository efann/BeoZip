/*
 * BeoZip : a simple archiving application for the Java(tm) Swing platform previously written in C++.
 *
 * Copyright(c) 2001-2019, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.BeoZip;

import com.beowurks.BeoCommon.GridBagLayoutHelper;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class SplashScreenWindow extends JWindow
{

  protected final JProgressBar barAction1 = new JProgressBar(0, 160);

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public SplashScreenWindow()
  {

    this.barAction1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    this.barAction1.setStringPainted(true);

    final ImageIcon loIcon = new ImageIcon(this.getClass().getResource("images/Logo300x300.jpg"));
    final JLabel loLabel = new JLabel(loIcon);

    final GridBagLayoutHelper loGrid = new GridBagLayoutHelper();

    final Container loContent = this.getContentPane();
    loContent.setLayout(loGrid);
    loGrid.setInsetDefaults();

    loContent.add(loLabel, loGrid.getConstraint(0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE));
    loContent.add(this.barAction1, loGrid.getConstraint(0, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH));

    this.centerWindow();
  }

  // ---------------------------------------------------------------------------
  protected void centerWindow()
  {
    this.pack();

    // Center the window
    final Dimension ldScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
    final Dimension ldFrameSize = this.getSize();

    if (ldFrameSize.height > ldScreenSize.height)
    {
      ldFrameSize.height = ldScreenSize.height;
    }
    if (ldFrameSize.width > ldScreenSize.width)
    {
      ldFrameSize.width = ldScreenSize.width;
    }

    this.setLocation((ldScreenSize.width - ldFrameSize.width) / 2, (ldScreenSize.height - ldFrameSize.height) / 2);
  }

  // ---------------------------------------------------------------------------
  protected void setProgressBar(final int tnValue, final String tcValue)
  {
    SwingUtilities.invokeLater(() ->
    {
      SplashScreenWindow.this.barAction1.setValue(tnValue);
      SplashScreenWindow.this.barAction1.setString(tcValue);
    });

  }
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
