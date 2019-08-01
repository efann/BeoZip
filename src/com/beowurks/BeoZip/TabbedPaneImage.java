/*
 * BeoZip : a simple archiving application for the Java(tm) Swing platform previously written in C++.
 *
 * Copyright(c) 2001-2019, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.BeoZip;

import com.beowurks.BeoCommon.BaseTabbedPane;

import java.awt.FontMetrics;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

// If you don't create this subclass and the Look and Feel changes,
// the SideTabbedPaneUI gets changed back to the default UI and
// the images disappear.
// I got the idea for the fix from the following:
// Newsgroups: comp.lang.java.gui
// From: "ak" <s...@imagero.com> - Find messages by this author
// Date: Tue, 2 Mar 2004 10:41:54 +0100
// Local: Tues, Mar 2 2004 4:41 am
// Subject: Re: JInternalFrame, Swing, disable move functionality - how ?

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class TabbedPaneImage extends BaseTabbedPane
{
  private SideTabbedPaneUI foSidePaneUI = null;

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public TabbedPaneImage()
  {
  }

  // ---------------------------------------------------------------------------
  @Override
  public void updateUI()
  {
    // This routine seems to be called before foSidePaneUI is initialized as
    // a variable. So this solves the problem.
    if (this.foSidePaneUI == null)
    {
      this.foSidePaneUI = new SideTabbedPaneUI();
    }

    this.setUI(this.foSidePaneUI);

    this.invalidate();
  }

  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // Code idea from http://www.mail-archive.com/swing@eos.dk/msg01269.html
  static private class SideTabbedPaneUI extends BasicTabbedPaneUI
  {
    private final ImageIcon[] faImageIcons;

    // ---------------------------------------------------------------------------
    public SideTabbedPaneUI()
    {
      this.faImageIcons = new ImageIcon[2];

      // Gets rid of the following bug:
      // Calling this.getClass().getResource(...) could give results other than expected if this class is extended by a
      // class in another package.
      this.faImageIcons[0] = new ImageIcon(SideTabbedPaneUI.class.getResource("images/ArchivingPNG32.png"));
      this.faImageIcons[1] = new ImageIcon(SideTabbedPaneUI.class.getResource("images/QuickZipPNG32.png"));
    }

    // ---------------------------------------------------------------------------
    @Override
    protected Icon getIconForTab(final int tabIndex)
    {
      return (this.faImageIcons[tabIndex]);
    }

    // ---------------------------------------------------------------------------
    // For some reason if you don't override the tab width, it will be too large.
    @Override
    protected int calculateTabWidth(final int tnTabPlacement, final int tnTabIndex, final FontMetrics toMetrics)
    {
      return (this.faImageIcons[tnTabIndex].getIconWidth() + 2);
    }

    // ---------------------------------------------------------------------------
    // For some reason if you don't override the tab height, it will be too large.
    @Override
    protected int calculateTabHeight(final int tnTabPlacement, final int tnTabIndex, final int tnFontHeight)
    {
      return (this.faImageIcons[tnTabIndex].getIconHeight() + 2);
    }
    // ---------------------------------------------------------------------------
  }
  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
