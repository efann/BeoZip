/*
 * BeoZip : a simple archiving application for the Java(tm) Swing platform previously written in C++.
 *
 * Copyright(c) 2001-2018, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.BeoZip;

import com.beowurks.BeoCommon.Util;

import java.io.File;

// By the way, FileDialogFilter and ListZipFilesFilter are different:
// ListZipFilesFilter uses the interface of java.io.FileFilter
// FileDialogFilter is a super class of javax.swing.filechooser.FileFilter.

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
class ListZipFilesFilter implements java.io.FileFilter
{
  // ---------------------------------------------------------------------------
  // Accept only zip files
  public ListZipFilesFilter()
  {
  }

  // ---------------------------------------------------------------------------
  @Override
  public boolean accept(final File toFile)
  {
    if (toFile == null)
    {
      return (false);
    }

    if (toFile.isDirectory())
    {
      return (false);
    }

    final String lcExt = Util.extractFileExtension(toFile);

    if (lcExt == null)
    {
      return (false);
    }

    return (lcExt.compareToIgnoreCase("zip") == 0);
  }
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
