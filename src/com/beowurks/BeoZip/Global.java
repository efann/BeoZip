/*
 * BeoZip : a simple archiving application for the Java(tm) Swing platform previously written in C++.
 *
 * Copyright(c) 2001-2019, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.BeoZip;

import com.beowurks.BeoCommon.Util;

import java.awt.Window;
import java.io.File;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public final class Global
{

  protected final static String LOCAL_PATH = Global.isDevelopmentEnvironment() ? Util.includeTrailingBackslash("\\IntelliJ\\BeoZip") : Util
          .includeTrailingBackslash(Util.includeTrailingBackslash(Util.includeTrailingBackslash(System
                  .getProperty("user.home")) + "Beowurks")
                  + "BeoZip");

  protected final static String ARRAY_DELIMITER = System.getProperty("path.separator") + System.getProperty("path.separator")
          + System.getProperty("path.separator");
  protected final static String PROP_LOOKANDFEEL = "Application1 LookAndFeel";
  protected final static String PROP_METALTHEME = "Application1 MetalTheme";
  protected final static String ZIP_EXTENSION = ".zip";

  protected final static int TEXT_HEIGHT = 21;

  private static String[] faDriveRootList = null;
  private static ThreadGroup foRootGroup = null;
  private static final StringBuilder fcExceptionError = new StringBuilder(256);

  // ---------------------------------------------------------------------------
  private Global()
  {
  }

  // ---------------------------------------------------------------------------
  static protected void errorException(final Window toWindow, final String tcException)
  {
    Global.errorException(toWindow, "Please notify support@beowurks.com of the following error:", tcException);
  }

  // ---------------------------------------------------------------------------
  static protected void errorException(final Window toWindow, final String tcMessage, final String tcException)
  {
    Global.setExceptionError(tcMessage, tcException);

    Util.errorMessage(toWindow, Global.fcExceptionError.toString());
  }

  // ---------------------------------------------------------------------------
  static private void setExceptionError(final String tcMessage, final String tcException)
  {
    Util.clearStringBuilder(Global.fcExceptionError);

    Global.fcExceptionError.append("<html><font face=\"Arial\">");
    Global.fcExceptionError.append(tcMessage);
    Global.fcExceptionError.append(" <br><br><i> ");
    Global.fcExceptionError.append(tcException);
    Global.fcExceptionError.append(" </i><p></p></font></html>");
  }

  // ---------------------------------------------------------------------------
  static protected boolean isDevelopmentEnvironment()
  {
    final String lcUserDir = System.getProperty("user.dir").toLowerCase();
    final String lcFileSeparator = System.getProperty("file.separator");

    return (lcUserDir.contains("intellij" + lcFileSeparator + "beozip" + lcFileSeparator + "src"));
  }

  // ---------------------------------------------------------------------------
  static protected void loadDriveRootListIfNeeded()
  {
    if (Global.faDriveRootList != null)
    {
      return;
    }

    // From the recommendations of
    // LI: Incorrect lazy initialization and update of static field (LI_LAZY_INIT_UPDATE_STATIC)
    final File[] loFiles = File.listRoots();
    final int lnCount = loFiles.length;
    final String[] laDriveRootList = new String[lnCount];
    for (int i = 0; i < lnCount; ++i)
    {
      laDriveRootList[i] = Util.replaceAll(loFiles[i].getPath(), "\\", "/").toString();
    }

    Global.faDriveRootList = laDriveRootList;
  }

  // ---------------------------------------------------------------------------
  static protected int getRootDriveCountFromList()
  {
    return (Global.faDriveRootList.length);
  }

  // ---------------------------------------------------------------------------
  static protected String getRootDriveFromList(final int tnIndex)
  {
    if ((tnIndex < 0) || (tnIndex >= Global.faDriveRootList.length))
    {
      Global.errorException(null, "Wrong index for drive root list");
      return ("");
    }

    return (Global.faDriveRootList[tnIndex]);
  }

  // ---------------------------------------------------------------------------
  static protected boolean isThreadActive(final Class<?> toClass)
  {
    if (Global.foRootGroup == null)
    {
      ThreadGroup loGroup = Thread.currentThread().getThreadGroup();
      // traverse the tree to the root group
      while (loGroup != null)
      {
        Global.foRootGroup = loGroup;
        loGroup = loGroup.getParent();
      }
    }

    // double the current active count to cover all threads
    final int lnSizeEstimate = Global.foRootGroup.activeCount() * 2;
    final Thread[] laThreadList = new Thread[lnSizeEstimate];

    final int lnSize = Global.foRootGroup.enumerate(laThreadList);

    for (int i = 0; i < lnSize; i++)
    {
      if (laThreadList[i].getClass() == toClass)
      {
        return (true);
      }
    }

    return (false);
  }
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
