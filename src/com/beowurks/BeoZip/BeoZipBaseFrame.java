/*
 * BeoZip : a simple archiving application for the Java(tm) Swing platform previously written in C++.
 *
 * Copyright(c) 2001-2019, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.BeoZip;

import com.beowurks.BeoCommon.BaseFrame;

import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.net.URL;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class BeoZipBaseFrame extends BaseFrame
{
  protected MainFrame foMainFrame;

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public BeoZipBaseFrame()
  {
  }

  // ---------------------------------------------------------------------------
  public BeoZipBaseFrame(final MainFrame toMainFrame, final String tcTitle, final boolean tlPackFrame)
  {
    super(tcTitle, tlPackFrame);

    this.foMainFrame = toMainFrame;
  }

  // ---------------------------------------------------------------------------
  // Component initialization
  @Override
  protected void jbInit() throws Exception
  {
    super.jbInit();

    if (this.foMainFrame != this)
    {
      this.createDuplicateTopMenu(this.foMainFrame.getJMenuBar());
      this.setJMenuBar(this.foDuplicateMenuBar);
    }
    // Gets rid of the following bug:
    // Calling this.getClass().getResource(...) could give results other than expected if this class is extended by a
    // class in another package.

    final URL loURLImage = BeoZipBaseFrame.class.getResource("images/Logo15Percent.jpg");
    if (loURLImage != null)
    {
      this.setIconImage(Toolkit.getDefaultToolkit().createImage(loURLImage));
    }
  }

  // ---------------------------------------------------------------------------
  // Overridden so we can exit when window is closed
  @Override
  protected void processWindowEvent(final WindowEvent e)
  {
    // This has to go here. this.foMainFrame.updateMenus() uses the getActiveFrame
    // which is set in super.processWindowEvent.
    super.processWindowEvent(e);

    switch (e.getID())
    {
      case WindowEvent.WINDOW_ACTIVATED:
        this.foMainFrame.updateMenus();
        break;
    }
  }

  // ---------------------------------------------------------------------------
  @Override
  protected void releasePointers()
  {
    this.foMainFrame = null;

    super.releasePointers();
  }
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
