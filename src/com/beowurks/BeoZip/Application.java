/*
 * BeoZip : a simple archiving application for the Java(tm) Swing platform previously written in C++.
 *
 * Copyright(c) 2001-2018, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.BeoZip;

import com.beowurks.BeoCommon.Util;
import com.beowurks.BeoLookFeel.LFCommon;

import javax.swing.SwingUtilities;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class Application
{
  protected final AppProperties foAppProperties = new AppProperties();
  protected final SplashScreenWindow foSplash = new SplashScreenWindow();

  // ---------------------------------------------------------------------------
  // Construct the application
  public Application()
  {
    // Turns off the security sand box which should improve performance somewhat.
    System.setSecurityManager(null);

    Util.setTitle(Util.buildTitleFromManifest(this));

    SwingUtilities.invokeLater(() ->
    {
      Application.this.foSplash.setProgressBar(0, "Starting...");
      Application.this.foSplash.setVisible(true);
    });

    this.setLookAndFeel();

    new MainFrame(this);
  }

  // ---------------------------------------------------------------------------
  private void setLookAndFeel()
  {
    Util.setUIManagerAppearances();

    final String lcLookAndFeel = this.foAppProperties.getLookAndFeel();
    final String lcMetalTheme = this.foAppProperties.getMetalTheme();

    LFCommon.setLookFeel(lcLookAndFeel, lcMetalTheme, null, true);

    // You can a list of the defaults by doing the following:
    // Util.listDefaultSettings();
  }

  // ---------------------------------------------------------------------------
  // Main method
  public static void main(final String[] args)
  {
    new Application();
  }
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
