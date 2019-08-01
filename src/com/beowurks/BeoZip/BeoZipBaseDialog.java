/*
 * BeoZip : a simple archiving application for the Java(tm) Swing platform previously written in C++.
 *
 * Copyright(c) 2001-2019, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.BeoZip;

import com.beowurks.BeoCommon.BaseDialog;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class BeoZipBaseDialog extends BaseDialog
{

  protected MainFrame foMainFrame;

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------
  public BeoZipBaseDialog(final MainFrame toFrame, final String tcTitle)
  {
    super(toFrame, tcTitle);

    this.foMainFrame = toFrame;
  }

  // ---------------------------------------------------------------------------
  public MainFrame getMainFrame()
  {
    return (this.foMainFrame);
  }

  // ---------------------------------------------------------------------------
  @Override
  public void releasePointers()
  {
    this.foMainFrame = null;

    super.removeListeners();
  }
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
