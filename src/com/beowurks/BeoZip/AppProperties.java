/*
 * BeoZip : a simple archiving application for the Java(tm) Swing platform previously written in C++.
 *
 * Copyright(c) 2001-2019, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/legal/epl-2.0/)
 *
 */

package com.beowurks.BeoZip;

import com.beowurks.BeoCommon.BaseProperties;
import com.beowurks.BeoCommon.Util;
import com.beowurks.BeoLookFeel.LFCommon;

import java.util.StringTokenizer;

// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
public class AppProperties extends BaseProperties
{
  public final static int ARCHIVENAME_DATEONLY = 0;
  public final static int ARCHIVENAME_DATETIME = 1;
  public final static int ARCHIVENAME_DATETIMEMILLISECONDS = 2;

  final private static String PROPERTY_HEADER = "BeoZip\u00a9 Parameters - DO NOT EDIT . . . please";

  final private static String PREFIX_APPL = "Application ";
  final private static String PREFIX_MAIN = "BeoZip.MainFrame ";

  private final static String LOOKANDFEEL = AppProperties.PREFIX_APPL + "LookAndFeel";
  private final static String METALTHEME = AppProperties.PREFIX_APPL + "MetalTheme";

  private final static String ARCHIVES_LOCATION = AppProperties.PREFIX_MAIN + "fcArchiveFolder";

  private final static String PUSHBUTTON_ADDFOLDER_QZ = AppProperties.PREFIX_MAIN + "fcPushButtonAddFolderQZ";
  private final static String PUSHBUTTON_OPENFOLDER_QZ = AppProperties.PREFIX_MAIN + "fcPushButtonOpenFolderQZ";
  private final static String PUSHBUTTON_EXTRACTFOLDER_QZ = AppProperties.PREFIX_MAIN + "fcPushButtonExtractFolderQZ";

  // The tag of 'grdFoldersToArchive1' is a legacy tag that I'd rather not change as it contains the list of folders
  // to archive which is central to BeoZip.
  private final static String GRID_FOLDERS_TO_ARCHIVE = AppProperties.PREFIX_MAIN + "grdFoldersToArchive1";

  private final static String MENU_MODIFIED = AppProperties.PREFIX_MAIN + "menuModified1";
  private final static String MENU_SIZE = AppProperties.PREFIX_MAIN + "menuSize1";
  private final static String MENU_RATIO = AppProperties.PREFIX_MAIN + "menuRatio1";
  private final static String MENU_PACKED = AppProperties.PREFIX_MAIN + "menuPacked1";
  private final static String MENU_CRC32 = AppProperties.PREFIX_MAIN + "menuCRC321";
  private final static String MENU_METHOD = AppProperties.PREFIX_MAIN + "menuMethod1";
  private final static String MENU_PATH = AppProperties.PREFIX_MAIN + "menuPath1";

  private final static String GRID_LISTOFARCHIVES_SORT = AppProperties.PREFIX_MAIN + "grdListOfArchives1 Sort";
  private final static String GRID_LISTOFARCHIVES_ASCENDING = AppProperties.PREFIX_MAIN + "grdListOfArchives1 Ascending";

  private final static String GRID_FILESWITHINARCHIVE_BZ_SORT = AppProperties.PREFIX_MAIN + "grdFilesWithinArchiveBZ1 Sort";
  private final static String GRID_FILESWITHINARCHIVE_BZ_ASCENDING = AppProperties.PREFIX_MAIN + "grdFilesWithinArchiveBZ1 Ascending";

  private final static String GRID_FILESWITHINARCHIVE_QZ_SORT = AppProperties.PREFIX_MAIN + "grdFilesWithinArchiveQZ1 Sort";
  private final static String GRID_FILESWITHINARCHIVE_QZ_ASCENDING = AppProperties.PREFIX_MAIN + "grdFilesWithinArchiveQZ1 Ascending";

  private final static String BEOZIP_RESTORE_PATH = AppProperties.PREFIX_MAIN + "fcBeoZipRestorePath";

  private final static String OPTION_COMPRESSIONLEVEL_BZ = AppProperties.PREFIX_MAIN + "fnOptionCompressionLevelBZ";
  private final static String OPTION_COMPRESSIONLEVEL_QZ = AppProperties.PREFIX_MAIN + "fnOptionCompressionLevelQZ";

  private final static String OPTION_INCLUDEHIDDENDIRECTORIES_BZ = AppProperties.PREFIX_MAIN + "flOptionIncludeHiddenDirectoriesBZ";
  private final static String OPTION_INCLUDEHIDDENDIRECTORIES_QZ = AppProperties.PREFIX_MAIN + "flOptionIncludeHiddenDirectoriesQZ";

  private final static String OPTION_INCLUDEHIDDENFILES_BZ = AppProperties.PREFIX_MAIN + "flOptionIncludeHiddenFilesBZ";
  private final static String OPTION_INCLUDEHIDDENFILES_QZ = AppProperties.PREFIX_MAIN + "flOptionIncludeHiddenFilesQZ";

  // The tag of 'fnOptionBeoZipArchiveNameType' is a legacy tag that I'd rather not change as it designates the format
  // of the BeoZip archive file name.
  private final static String OPTION_ARCHIVENAME_TYPE_BZ = AppProperties.PREFIX_MAIN + "fnOptionBeoZipArchiveNameType";

  private final static String OPTION_INCLUDESUBFOLDERS_QZ = AppProperties.PREFIX_MAIN + "flOptionIncludeSubFoldersQZ";

  private final static String OPTION_SAVEPATHINFO_QZ = AppProperties.PREFIX_MAIN + "flOptionSavePathInformationQZ";

  private final static String OPTION_EXTRACTION_USEPATHNAME = AppProperties.PREFIX_MAIN + "flOptionExtractionUsePathNames";

  private final static String OPTION_EXTRACTION_OVERWRITE = AppProperties.PREFIX_MAIN + "flOptionExtractionOverwriteFiles";

  private final static String OPTION_EXTRACTION_SELECTALLFILES = AppProperties.PREFIX_MAIN + "flOptionExtractionSelectAllFiles";

  // ---------------------------------------------------------------------------
  public AppProperties()
  {
    super(Global.LOCAL_PATH, System.getProperty("user.name") + ".Properties", AppProperties.PROPERTY_HEADER);
  }

  // ---------------------------------------------------------------------------
  public String getLookAndFeel()
  {
    return (this.getProperty(AppProperties.LOOKANDFEEL, "javax.swing.plaf.metal.MetalLookAndFeel"));
  }

  // ---------------------------------------------------------------------------
  public void setLookAndFeel()
  {
    this.setProperty(AppProperties.LOOKANDFEEL, LFCommon.getCurrentLookAndFeel());
  }

  // ---------------------------------------------------------------------------
  public String getMetalTheme()
  {
    return (this.getProperty(AppProperties.METALTHEME, "javax.swing.plaf.metal.OceanTheme"));
  }

  // ---------------------------------------------------------------------------
  public void setMetalTheme()
  {
    this.setProperty(AppProperties.METALTHEME, LFCommon.getCurrentMetalTheme());
  }

  // ---------------------------------------------------------------------------
  public String getArchivesLocation()
  {
    return (Util.includeTrailingBackslash(this.getProperty(AppProperties.ARCHIVES_LOCATION, System.getProperty("user.home"))));
  }

  // ---------------------------------------------------------------------------
  public void setArchivesLocation(final String tcValue)
  {
    this.setProperty(AppProperties.ARCHIVES_LOCATION, Util.includeTrailingBackslash(tcValue));
  }

  // ---------------------------------------------------------------------------
  public String getPushButtonAddFolderQZ()
  {
    return (this.getProperty(AppProperties.PUSHBUTTON_ADDFOLDER_QZ, System.getProperty("user.home")));
  }

  // ---------------------------------------------------------------------------
  public void setPushButtonAddFolderQZ(final String tcValue)
  {
    this.setProperty(AppProperties.PUSHBUTTON_ADDFOLDER_QZ, tcValue);
  }

  // ---------------------------------------------------------------------------
  public String getPushButtonOpenFolderQZ()
  {
    return (this.getProperty(AppProperties.PUSHBUTTON_OPENFOLDER_QZ, System.getProperty("user.home")));
  }

  // ---------------------------------------------------------------------------
  public void setPushButtonOpenFolderQZ(final String tcValue)
  {
    this.setProperty(AppProperties.PUSHBUTTON_OPENFOLDER_QZ, tcValue);
  }

  // ---------------------------------------------------------------------------
  public String getPushButtonExtractFolderQZ()
  {
    return (this.getProperty(AppProperties.PUSHBUTTON_EXTRACTFOLDER_QZ, System.getProperty("user.home")));
  }

  // ---------------------------------------------------------------------------
  public void setPushButtonExtractFolderQZ(final String tcValue)
  {
    this.setProperty(AppProperties.PUSHBUTTON_EXTRACTFOLDER_QZ, tcValue);
  }

  // ---------------------------------------------------------------------------
  public String[] getGridFoldersToArchive()
  {
    final StringTokenizer lcTokens = new StringTokenizer(this.getProperty(AppProperties.GRID_FOLDERS_TO_ARCHIVE, ""), Global.ARRAY_DELIMITER);

    final int lnCount = lcTokens.countTokens();

    final String[] laFoldersToArchive = (lnCount > 0) ? new String[lnCount] : null;
    if (laFoldersToArchive != null)
    {
      int i = 0;
      while (lcTokens.hasMoreElements())
      {
        laFoldersToArchive[i++] = lcTokens.nextElement().toString();
      }
    }

    return (laFoldersToArchive);
  }

  // ---------------------------------------------------------------------------
  public void setGridFoldersToArchive(final String[] taValues)
  {
    final StringBuilder lcArchiveFolders = new StringBuilder(1024);
    if (taValues != null)
    {
      for (final String lcValue : taValues)
      {
        lcArchiveFolders.append(lcValue);
        lcArchiveFolders.append(Global.ARRAY_DELIMITER);
      }
    }

    this.setProperty(AppProperties.GRID_FOLDERS_TO_ARCHIVE, lcArchiveFolders.toString());
  }

  // ---------------------------------------------------------------------------
  public boolean getMenuModified()
  {
    return (this.getProperty(AppProperties.MENU_MODIFIED, true));
  }

  // ---------------------------------------------------------------------------
  public void setMenuModified(final boolean tlValue)
  {
    this.setProperty(AppProperties.MENU_MODIFIED, tlValue);
  }

  // ---------------------------------------------------------------------------
  public boolean getMenuSize()
  {
    return (this.getProperty(AppProperties.MENU_SIZE, true));
  }

  // ---------------------------------------------------------------------------
  public void setMenuSize(final boolean tlValue)
  {
    this.setProperty(AppProperties.MENU_SIZE, tlValue);
  }

  // ---------------------------------------------------------------------------
  public boolean getMenuRatio()
  {
    return (this.getProperty(AppProperties.MENU_RATIO, true));
  }

  // ---------------------------------------------------------------------------
  public void setMenuRatio(final boolean tlValue)
  {
    this.setProperty(AppProperties.MENU_RATIO, tlValue);
  }

  // ---------------------------------------------------------------------------
  public boolean getMenuPacked()
  {
    return (this.getProperty(AppProperties.MENU_PACKED, true));
  }

  // ---------------------------------------------------------------------------
  public void setMenuPacked(final boolean tlValue)
  {
    this.setProperty(AppProperties.MENU_PACKED, tlValue);
  }

  // ---------------------------------------------------------------------------
  public boolean getMenuCRC32()
  {
    return (this.getProperty(AppProperties.MENU_CRC32, true));
  }

  // ---------------------------------------------------------------------------
  public void setMenuCRC32(final boolean tlValue)
  {
    this.setProperty(AppProperties.MENU_CRC32, tlValue);
  }

  // ---------------------------------------------------------------------------
  public boolean getMenuMethod()
  {
    return (this.getProperty(AppProperties.MENU_METHOD, true));
  }

  // ---------------------------------------------------------------------------
  public void setMenuMethod(final boolean tlValue)
  {
    this.setProperty(AppProperties.MENU_METHOD, tlValue);
  }

  // ---------------------------------------------------------------------------
  public boolean getMenuPath()
  {
    return (this.getProperty(AppProperties.MENU_PATH, true));
  }

  // ---------------------------------------------------------------------------
  public void setMenuPath(final boolean tlValue)
  {
    this.setProperty(AppProperties.MENU_PATH, tlValue);
  }

  // ---------------------------------------------------------------------------
  public int getGridListOfArchivesSort()
  {
    return (this.getProperty(AppProperties.GRID_LISTOFARCHIVES_SORT, 0));
  }

  // ---------------------------------------------------------------------------
  public void setGridListOfArchivesSort(final int tnValue)
  {
    this.setProperty(AppProperties.GRID_LISTOFARCHIVES_SORT, tnValue);
  }

  // ---------------------------------------------------------------------------
  public boolean getGridListOfArchivesAscending()
  {
    return (this.getProperty(AppProperties.GRID_LISTOFARCHIVES_ASCENDING, true));
  }

  // ---------------------------------------------------------------------------
  public void setGridListOfArchivesAscending(final boolean tlValue)
  {
    this.setProperty(AppProperties.GRID_LISTOFARCHIVES_ASCENDING, tlValue);
  }

  // ---------------------------------------------------------------------------
  public int getGridFilesWithinArchiveBZSort()
  {
    return (this.getProperty(AppProperties.GRID_FILESWITHINARCHIVE_BZ_SORT, 0));
  }

  // ---------------------------------------------------------------------------
  public void setGridFilesWithinArchiveBZSort(final int tnValue)
  {
    this.setProperty(AppProperties.GRID_FILESWITHINARCHIVE_BZ_SORT, tnValue);
  }

  // ---------------------------------------------------------------------------
  public boolean getGridFilesWithinArchiveBZAscending()
  {
    return (this.getProperty(AppProperties.GRID_FILESWITHINARCHIVE_BZ_ASCENDING, true));
  }

  // ---------------------------------------------------------------------------
  public void setGridFilesWithinArchiveBZAscending(final boolean tlValue)
  {
    this.setProperty(AppProperties.GRID_FILESWITHINARCHIVE_BZ_ASCENDING, tlValue);
  }

  // ---------------------------------------------------------------------------
  public int getGridFilesWithinArchiveQZSort()
  {
    return (this.getProperty(AppProperties.GRID_FILESWITHINARCHIVE_QZ_SORT, 0));
  }

  // ---------------------------------------------------------------------------
  public void setGridFilesWithinArchiveQZSort(final int tnValue)
  {
    this.setProperty(AppProperties.GRID_FILESWITHINARCHIVE_QZ_SORT, tnValue);
  }

  // ---------------------------------------------------------------------------
  public boolean getGridFilesWithinArchiveQZAscending()
  {
    return (this.getProperty(AppProperties.GRID_FILESWITHINARCHIVE_QZ_ASCENDING, true));
  }

  // ---------------------------------------------------------------------------
  public void setGridFilesWithinArchiveQZAscending(final boolean tlValue)
  {
    this.setProperty(AppProperties.GRID_FILESWITHINARCHIVE_QZ_ASCENDING, tlValue);
  }

  // ---------------------------------------------------------------------------
  public String getBeoZipRestorePath()
  {
    return (this.getProperty(AppProperties.BEOZIP_RESTORE_PATH, Util.isWindows() ? "c:/" : "/"));
  }

  // ---------------------------------------------------------------------------
  public void setBeoZipRestorePath(final String tcValue)
  {
    this.setProperty(AppProperties.BEOZIP_RESTORE_PATH, tcValue);
  }

  // ---------------------------------------------------------------------------
  public int getOptionCompressionLevelBZ()
  {
    return (this.getProperty(AppProperties.OPTION_COMPRESSIONLEVEL_BZ, 6));
  }

  // ---------------------------------------------------------------------------
  public void setOptionCompressionLevelBZ(final int tnValue)
  {
    this.setProperty(AppProperties.OPTION_COMPRESSIONLEVEL_BZ, tnValue);
  }

  // ---------------------------------------------------------------------------
  public int getOptionCompressionLevelQZ()
  {
    return (this.getProperty(AppProperties.OPTION_COMPRESSIONLEVEL_QZ, 6));
  }

  // ---------------------------------------------------------------------------
  public void setOptionCompressionLevelQZ(final int tnValue)
  {
    this.setProperty(AppProperties.OPTION_COMPRESSIONLEVEL_QZ, tnValue);
  }

  // ---------------------------------------------------------------------------
  public boolean getOptionIncludeHiddenDirectoriesBZ()
  {
    return (this.getProperty(AppProperties.OPTION_INCLUDEHIDDENDIRECTORIES_BZ, false));
  }

  // ---------------------------------------------------------------------------
  public void setOptionIncludeHiddenDirectoriesBZ(final boolean tlValue)
  {
    this.setProperty(AppProperties.OPTION_INCLUDEHIDDENDIRECTORIES_BZ, tlValue);
  }

  // ---------------------------------------------------------------------------
  public boolean getOptionIncludeHiddenDirectoriesQZ()
  {
    return (this.getProperty(AppProperties.OPTION_INCLUDEHIDDENDIRECTORIES_QZ, false));
  }

  // ---------------------------------------------------------------------------
  public void setOptionIncludeHiddenDirectoriesQZ(final boolean tlValue)
  {
    this.setProperty(AppProperties.OPTION_INCLUDEHIDDENDIRECTORIES_QZ, tlValue);
  }

  // ---------------------------------------------------------------------------
  public boolean getOptionIncludeHiddenFilesBZ()
  {
    return (this.getProperty(AppProperties.OPTION_INCLUDEHIDDENFILES_BZ, true));
  }

  // ---------------------------------------------------------------------------
  public void setOptionIncludeHiddenFilesBZ(final boolean tlValue)
  {
    this.setProperty(AppProperties.OPTION_INCLUDEHIDDENFILES_BZ, tlValue);
  }

  // ---------------------------------------------------------------------------
  public boolean getOptionIncludeHiddenFilesQZ()
  {
    return (this.getProperty(AppProperties.OPTION_INCLUDEHIDDENFILES_QZ, true));
  }

  // ---------------------------------------------------------------------------
  public void setOptionIncludeHiddenFilesQZ(final boolean tlValue)
  {
    this.setProperty(AppProperties.OPTION_INCLUDEHIDDENFILES_QZ, tlValue);
  }

  // ---------------------------------------------------------------------------
  public int getOptionArchiveNameTypeBZ()
  {
    return (this.getProperty(AppProperties.OPTION_ARCHIVENAME_TYPE_BZ, AppProperties.ARCHIVENAME_DATEONLY));
  }

  // ---------------------------------------------------------------------------
  public void setOptionArchiveNameTypeBZ(final int tnValue)
  {
    this.setProperty(AppProperties.OPTION_ARCHIVENAME_TYPE_BZ, tnValue);
  }

  // ---------------------------------------------------------------------------
  public boolean getOptionIncludeSubFoldersQZ()
  {
    return (this.getProperty(AppProperties.OPTION_INCLUDESUBFOLDERS_QZ, false));
  }

  // ---------------------------------------------------------------------------
  public void setOptionIncludeSubFoldersQZ(final boolean tlValue)
  {
    this.setProperty(AppProperties.OPTION_INCLUDESUBFOLDERS_QZ, tlValue);
  }

  // ---------------------------------------------------------------------------
  public boolean getOptionSavePathInformationQZ()
  {
    return (this.getProperty(AppProperties.OPTION_SAVEPATHINFO_QZ, true));
  }

  // ---------------------------------------------------------------------------
  public void setOptionSavePathInformationQZ(final boolean tlValue)
  {
    this.setProperty(AppProperties.OPTION_SAVEPATHINFO_QZ, tlValue);
  }

  // ---------------------------------------------------------------------------
  public boolean getOptionExtractionUsePathNames()
  {
    return (this.getProperty(AppProperties.OPTION_EXTRACTION_USEPATHNAME, true));
  }

  // ---------------------------------------------------------------------------
  public void setOptionExtractionUsePathNames(final boolean tlValue)
  {
    this.setProperty(AppProperties.OPTION_EXTRACTION_USEPATHNAME, tlValue);
  }

  // ---------------------------------------------------------------------------
  public boolean getOptionExtractionOverWriteFiles()
  {
    return (this.getProperty(AppProperties.OPTION_EXTRACTION_OVERWRITE, true));
  }

  // ---------------------------------------------------------------------------
  public void setOptionExtractionOverWriteFiles(final boolean tlValue)
  {
    this.setProperty(AppProperties.OPTION_EXTRACTION_OVERWRITE, tlValue);
  }

  // ---------------------------------------------------------------------------
  public boolean getOptionExtractionSelectAllFiles()
  {
    return (this.getProperty(AppProperties.OPTION_EXTRACTION_SELECTALLFILES, true));
  }

  // ---------------------------------------------------------------------------
  public void setOptionExtractionSelectAllFiles(final boolean tlValue)
  {
    this.setProperty(AppProperties.OPTION_EXTRACTION_SELECTALLFILES, tlValue);
  }
  // ---------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
