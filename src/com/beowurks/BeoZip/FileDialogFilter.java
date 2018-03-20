/*
 * BeoZip : a simple archiving application for the Java(tm) Swing platform previously written in C++.
 *
 * Copyright(c) 2001-2018, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html)
 *
 */

package com.beowurks.BeoZip;

import com.beowurks.BeoCommon.Util;

import java.io.File;

import javax.swing.JFileChooser;

// By the way, FileDialogFilter and ListZipFilesFilter are different:
// ListZipFilesFilter uses the interface of java.io.FileFilter
// FileDialogFilter is a super class of javax.swing.filechooser.FileFilter.

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
class FileDialogFilter extends javax.swing.filechooser.FileFilter
{
  private final int fnFileSelectionMode;
  private final boolean flHiddenDirectories;
  private final boolean flHiddenFiles;
  private final boolean flZipFiles;

  // ---------------------------------------------------------------------------
  // Accept only zip files
  public FileDialogFilter(final int tnFileSelectionMode, final boolean tlHiddenDirectories, final boolean tlHiddenFiles, final boolean tlZipFiles)
  {
    this.fnFileSelectionMode = tnFileSelectionMode;
    this.flHiddenDirectories = tlHiddenDirectories;
    this.flHiddenFiles = tlHiddenFiles;
    this.flZipFiles = tlZipFiles;
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
      // Remember: you will always display the file folders (unless hidden)
      // no matter what the fnFileSelectionMode is.
      if ((toFile.isHidden()) && (!this.flHiddenDirectories))
      {
        return (false);
      }

      return (true);
    }

    // Only reaches this area of the code if the file is not a directory.
    // If you can only select directories, then don't display files.
    if (this.fnFileSelectionMode == JFileChooser.DIRECTORIES_ONLY)
    {
      return (false);
    }

    if ((toFile.isHidden()) && (!this.flHiddenFiles))
    {
      return (false);
    }

    if (!this.flZipFiles)
    {
      return (true);
    }

    final String lcExt = Util.extractFileExtension(toFile);

    if (lcExt == null)
    {
      return (false);
    }

    return (lcExt.compareToIgnoreCase("zip") == 0);
  }

  // ---------------------------------------------------------------------------
  @Override
  public String getDescription()
  {
    final String lcDescription;

    if (this.fnFileSelectionMode == JFileChooser.DIRECTORIES_ONLY)
    {
      lcDescription = "Folders Only";
    }
    else if (this.flZipFiles)
    {
      lcDescription = "Archive Files (*.zip)";
    }
    else
    {
      lcDescription = "All Files (*.*)";
    }

    return (lcDescription);
  }
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
