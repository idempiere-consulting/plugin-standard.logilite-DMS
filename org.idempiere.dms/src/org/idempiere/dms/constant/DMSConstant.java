/******************************************************************************
 * Copyright (C) 2016 Logilite Technologies LLP								  *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/

package org.idempiere.dms.constant;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import org.compiere.model.MSysConfig;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.idempiere.dms.util.Utils;

/**
 * DMS Constant
 * 
 * @author Sachin Bhimani
 */
public final class DMSConstant
{
	// System Configuration
	public static final int					MAX_FILENAME_LENGTH						= MSysConfig.getIntValue("DMS_FILENAME_LENGTH", 150);
	public static final int					MAX_DIRECTORY_LENGTH					= MSysConfig.getIntValue("DMS_DIRECTORY_LENGTH", 50);
	public static final int					DMS_VIEWER_LABLE_FONT_SIZE				= MSysConfig.getIntValue("DMS_VIEWER_LABLE_FONT_SIZE", 11);

	public static final String				DMS_MOUNTING_BASE						= "DMS_MOUNTING_BASE";
	public static final String				DMS_MOUNTING_ARCHIVE_BASE				= "DMS_MOUNTING_ARCHIVE_BASE";
	public static final String				DMS_ALLOW_DOCUMENT_CONTENT_SEARCH		= "DMS_ALLOW_DOCUMENT_CONTENT_SEARCH";

	// Content widget size
	public static final int					CONTENT_LARGE_ICON_WIDTH				= 135;
	public static final int					CONTENT_LARGE_ICON_HEIGHT				= 135;

	// Cell attributes
	public static final String				COMP_ATTRIBUTE_CONTENT					= "ATTR_CONTENT";
	public static final String				COMP_ATTRIBUTE_VERSION					= "ATTR_VERSION";
	public static final String				COMP_ATTRIBUTE_ASSOCIATION				= "ATTR_ASSOCIATION";

	// View thumbnail toggle action
	public static final String				ICON_VIEW_LIST							= "ICON_VIEW_LIST";
	public static final String				ICON_VIEW_LARGE							= "ICON_VIEW_LARGE";
	public static final String				ICON_VIEW_VERSION						= "ICON_VIEW_VERSION";

	// Regular Expression
	public static final String				REG_SPACE								= "\\S+";
	public static final String				REG_EXP_FILENAME						= "^[A-Za-z0-9\\s\\-\\._\\(\\)]+$";
	public static final String				REG_EXP_WINDOWS_DIRNAME_VALIDATE		= "((^(CON|PRN|AUX|NUL|COM[0-9]|LPT[0-9])$)|([\\\\//:*?\\\"<>|?*\\x00-\\x1F]))";
	public static final String				REG_EXP_LINUX_DIRNAME_VALIDATE			= "(/)";
	public static final String				REG_EXP_VERSION_FILE					= "^.*\\(\\d+\\).\\w+$";
	public static final String				REG_EXP_LIKE_STR						= "%";
	public static final String				REG_EXP_PERIOD							= ".";
	public static final String				REG_EXP_UNDERSCORE_LIKE_STR				= "__%";
	public static final String				REG_EXP_UNDERSCORE_STR					= "_";

	// Pattern
	public static final Pattern				PATTERN_WINDOWS_DIRNAME_ISVALID			= Pattern.compile(REG_EXP_WINDOWS_DIRNAME_VALIDATE);
	public static final Pattern				PATTERN_LINX_DIRNAME_ISVALID			= Pattern.compile(REG_EXP_LINUX_DIRNAME_VALIDATE);

	// File Separator
	public static final String				STORAGE_PROVIDER_FILE_SEPARATOR			= "STORAGE_PROVIDER_FILE_SEPARATOR";
	public static final String				FILE_SEPARATOR							= Utils.getStorageProviderFileSeparator();

	// Button
	public static final String				TOOLBAR_BUTTON_DOCUMENT_EXPLORER		= "Document Explorer";

	// Event
	public static final String				EVENT_ON_RENAME_COMPLETE				= "onRenameComplete";
	public static final String				EVENT_ON_UPLOAD_COMPLETE				= "onUploadComplete";

	// Context Menu Item
	public static final String				MENUITEM_CUT							= "Cut";
	public static final String				MENUITEM_COPY							= "Copy";
	public static final String				MENUITEM_PASTE							= "Paste";
	public static final String				MENUITEM_RENAME							= "Rename";
	public static final String				MENUITEM_DELETE							= "Delete";
	public static final String				MENUITEM_DOWNLOAD						= "Download";
	public static final String				MENUITEM_ASSOCIATE						= "Associate";
	public static final String				MENUITEM_CREATELINK						= "Create Link";
	public static final String				MENUITEM_VERSIONlIST					= "Version List";
	public static final String				MENUITEM_UPLOADVERSION					= "Upload Version";
	public static final String				MENUITEM_ZOOMCONTENTWIN					= "Zoom";

	// DMS MimeType
	public static final String				DEFAULT									= "Default";
	public static final String				DIRECTORY								= "Directory";

	// AD_Image
	public static final String				DOWNLOAD								= "Download";

	// constant for index fields
	public static final String				NAME									= "Name";
	public static final String				CREATED									= "Created";
	public static final String				UPDATED									= "Updated";
	public static final String				CREATEDBY								= "CreatedBy";
	public static final String				UPDATEDBY								= "UpdatedBy";
	public static final String				RECORD_ID								= "Record_ID";
	public static final String				DESCRIPTION								= "Description";
	public static final String				CONTENTTYPE								= "ContentType";
	public static final String				AD_TABLE_ID								= "AD_Table_ID";
	public static final String				AD_CLIENT_ID							= "AD_Client_ID";
	public static final String				SHOW_INACTIVE							= "Show_InActive";
	public static final String				DMS_CONTENT_ID							= "DMS_Content_ID";
	public static final String				DMS_VERSION_ID							= "DMS_Version_ID";
	public static final String				DMS_ASSOCIATION_ID						= "DMS_Association_ID";
	public static final String				VERSION_SEQ_NO							= "Version_Seq_No";
	public static final String				FILE_CONTENT							= "File_Content";

	// Msg translate
	public static final String				MSG_NAME								= Msg.translate(Env.getCtx(), "Name");
	public static final String				MSG_CREATED								= Msg.translate(Env.getCtx(), "Created");
	public static final String				MSG_UPDATED								= Msg.translate(Env.getCtx(), "Updated");
	public static final String				MSG_FILESIZE							= Msg.translate(Env.getCtx(), "FileSize");
	public static final String				MSG_CREATEDBY							= Msg.translate(Env.getCtx(), "CreatedBy");
	public static final String				MSG_UPDATEDBY							= Msg.translate(Env.getCtx(), "UpdatedBy");
	public static final String				MSG_DESCRIPTION							= Msg.translate(Env.getCtx(), "Description");
	public static final String				MSG_CONTENT_TYPE						= Msg.translate(Env.getCtx(), "Content Type");
	public static final String				MSG_CONTENT_META						= Msg.translate(Env.getCtx(), "Content Meta");
	public static final String				MSG_FILL_MANDATORY						= Msg.translate(Env.getCtx(), "FillMandatory");
	public static final String				MSG_CONTENT_NAME						= Msg.translate(Env.getCtx(), "DMS_CONTENT_ID");
	public static final String				MSG_ADVANCE_SEARCH						= Msg.translate(Env.getCtx(), "Advance Search");
	public static final String				MSG_DIRECTORY_NAME						= Msg.translate(Env.getCtx(), "Directory Name");
	public static final String				MSG_DMS_CONTENT_TYPE					= Msg.translate(Env.getCtx(), "DMS_ContentType_ID");
	public static final String				MSG_SIGNATURE							= Msg.translate(Env.getCtx(), "Signature");

	// Msg
	public static final String				MSG_RENAME								= Msg.getMsg(Env.getCtx(), "Rename");
	public static final String				MSG_EXPLORER							= Msg.getMsg(Env.getCtx(), "Explorer");
	public static final String				MSG_ATTRIBUTES							= Msg.getMsg(Env.getCtx(), "Attributes");
	public static final String				MSG_SELECT_FILE							= Msg.getMsg(Env.getCtx(), "SelectFile");
	public static final String				MSG_ATTRIBUTE_SET						= Msg.getMsg(Env.getCtx(), "attribute.set");
	public static final String				MSG_SIZE								= "Size";
	public static final String				MSG_LINK								= "Link";
	public static final String				MSG_FILE_TYPE							= "File Type";
	public static final String				MSG_FILE_FOLDER							= "File Folder";
	public static final String				MSG_SHOW_IN_ACTIVE						= "Show InActive";
	public static final String				MSG_UPLOAD_CONTENT						= "Upload Content";
	public static final String				MSG_VERSION_HISTORY						= "Version History";
	public static final String				MSG_DMS_VERSION_LIST					= "DMS Version List";
	public static final String				MSG_CREATE_DIRECTORY					= "Create Directory";
	public static final String				MSG_ENTER_DIRETORY_NAME					= "Enter Directory Name";
	public static final String				MSG_NO_VERSION_DOC_EXISTS				= "No version Document available.";
	public static final String				MSG_ENTER_NEW_NAME_FOR_ITEM				= "Please enter a new name for the item:";

	// ToolTipText
	public static final String				TTT_SAVE								= "Save";
	public static final String				TTT_EDIT								= "Edit";
	public static final String				TTT_SEARCH								= "Search";
	public static final String				TTT_DOWNLOAD							= "Download";
	public static final String				TTT_NEXT_RECORD							= "Next Record";
	public static final String				TTT_UPLOAD_VERSION						= "Upload Version";
	public static final String				TTT_PREVIOUS_RECORD						= "Previous Record";
	public static final String				TTT_DISPLAYS_ITEMS_LAYOUT				= "Displays items Layout";

	// Contents Type
	public static final String				CONTENT_FILE							= "File";
	public static final String				CONTENT_DIR								= "Dir";

	// Operations
	public static final String				OPERATION_CREATE						= "OpsCreate";
	public static final String				OPERATION_RENAME						= "OpsRename";
	public static final String				OPERATION_COPY							= "OpsCopy";

	/*
	 * Date Format
	 */
	public static final SimpleDateFormat	SDF										= new SimpleDateFormat("yyyy-MM-dd hh:mm z");
	public static final SimpleDateFormat	SDF_WITH_TIME							= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
	public static final SimpleDateFormat	SDF_DATE_FORMAT_WITH_TIME				= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	public static final SimpleDateFormat	SDF_WITH_TIME_INDEXING					= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	/*
	 * CSS Style
	 */
	public static final String				CSS_DATEBOX								= "width: 100%; display:flex; flex-direction: row;";
	public static final String				CSS_BREAD_CRUMB_LINK					= "font-weight: bold; font-size: small; padding-left: 5px; color: dimgray;";
	public static final String				CSS_FLEX_ROW_DIRECTION					= "display: flex; flex-direction: row; flex-wrap: wrap; height: 100%;";
	public static final String				CSS_FLEX_ROW_DIRECTION_NOWRAP			= "padding-top: 4px; display: flex; flex-direction: row; flex-wrap: nowrap; height: 100%;";																									//

	public static final String				CSS_CONTENT_COMP_VIEWER_LARGE_NORMAL	= "margin: 5px; background: #e2e2e2; border: 4px double #ffffff !important; border-radius: 5px;";
	public static final String				CSS_CONTENT_COMP_VIEWER_LARGE_SELECTED	= "margin: 5px; background: #abcdff; border: 4px double #3363ad !important; border-radius: 5px;";

	public static final String				CSS_CONTENT_COMP_VIEWER_LIST_NORMAL		= "border-top: 1px solid #ffffff; border-bottom: 0px double #ffffff;";
	public static final String				CSS_CONTENT_COMP_VIEWER_LIST_SELECTED	= "border-top: 1px solid #3363ad; border-bottom: 1px double #3363ad;";

	public static final String				CSS_HIGHLIGHT_LABEL						= "font-weight: bold; text-align: center; border: 4px double #909090; padding: 4px 0px;";

	/*
	 * Queries
	 */

	public static final String				SQL_GET_CONTENT_LATEST_VERSION_NONLINK	= "SELECT v.DMS_Version_ID, a.DMS_Association_ID, v.SeqNo FROM DMS_Version v "
																						+ "INNER JOIN DMS_Association a ON ((a.DMS_Content_Related_ID = v.DMS_Content_ID OR a.DMS_Content_ID = v.DMS_Content_ID) AND COALESCE(DMS_AssociationType_ID, 0) IN (0, 1000001)) "
																						+ "WHERE v.DMS_Content_ID = ? "
																						+ "ORDER BY v.SeqNo DESC FETCH FIRST ROW ONLY";

	public static final String				SQL_GET_SUB_MOUNTING_BASE_CONTENT		= " SELECT c.DMS_Content_ID 	FROM DMS_Content c "
																						+ " INNER JOIN DMS_Association a	ON (a.DMS_Content_ID = c.DMS_Content_ID) "
																						+ " WHERE c.ContentBaseType = 'DIR' AND c.IsActive = 'Y' AND c.IsMounting = 'Y' AND c.ParentURL IS NOT NULL "
																						+ "			AND c.AD_Client_ID = ? AND a.DMS_Content_Related_ID = ? AND a.AD_Table_ID = ? AND c.Name = ? ";

	public static final String				SQL_GET_ROOT_MOUNTING_BASE_CONTENT		= "SELECT DMS_Content_ID FROM DMS_Content "
																						+ " WHERE Name = ? AND AD_Client_ID = ? AND ContentBaseType = 'DIR' AND IsActive = 'Y' AND IsMounting = 'Y' AND ParentUrl IS NULL ORDER BY Created";

	// Correct
	public static final String				SQL_GET_MOUNTING_CONTENT_FOR_TABLE		= "SELECT dc.DMS_Content_ID FROM DMS_Content dc "
																						+ " INNER JOIN DMS_Association da ON (dc.DMS_Content_ID = da.DMS_Content_ID) "
																						+ " WHERE dc.Name = ? AND dc.IsMounting = 'Y' AND da.AD_Table_ID = ? AND da.Record_ID = ?";

	public static final String				SQL_GET_CONTENT_ID_BY_CONTENT_NAME		= "SELECT c.DMS_Content_ID FROM DMS_Content c 																		"
																						+ " WHERE c.AD_Client_ID=? AND c.Name =? AND ((c.ParentURL=? AND True=?) OR (c.ParentURL IS NULL AND False=?))	";

	public static final String				SQL_GET_CONTENT_ID_BY_CONTENT_VALUE		= "SELECT v.DMS_Content_ID FROM DMS_Content c 																		"
																						+ " JOIN DMS_Version v  ON (v.DMS_Content_ID = c.DMS_Content_ID) 												"
																						+ " WHERE v.AD_Client_ID=? AND v.Value=? AND ((c.ParentURL=? AND True=?) OR (c.ParentURL IS NULL AND False=?))	";

	public static final String				SQL_GET_MATCHING_CONTENT_BY_NAME		= "SELECT Name  FROM DMS_Content c																											"
																						+ " WHERE c.AD_Client_ID=? AND (c.Name  LIKE ? OR c.Name  LIKE ?) AND ((c.ParentURL=? AND True=?) OR (c.ParentURL IS NULL AND False=?))	";

	public static final String				SQL_GET_MATCHING_CONTENT_BY_VALUE		= "SELECT v.Value FROM DMS_Content c																										"
																						+ " JOIN DMS_Version v  ON (v.DMS_Content_ID = c.DMS_Content_ID) 																		"
																						+ " WHERE v.AD_Client_ID=? AND (v.Value LIKE ? OR v.Value LIKE ?) AND ((c.ParentURL=? AND True=?) OR (c.ParentURL IS NULL AND False=?)) ";

	public static final String				SQL_GET_CONTENT_TYPE					= "SELECT at.Value FROM DMS_Content c 																										"
																						+ "	INNER JOIN DMS_Association 		a	ON a.DMS_Content_ID = c.DMS_Content_ID 															"
																						+ " INNER JOIN DMS_AssociationType	at	ON at.DMS_AssociationType_ID = a.DMS_AssociationType_ID AND at.DMS_AssociationType_ID<>1000003 	"
																						+ " WHERE c.DMS_Content_ID = ? 																											";

	public static final String				SQL_GET_ANOTHER_VERSION_IDS				= "SELECT c.DMS_Content_ID FROM DMS_Content c 																									"
																						+ " INNER JOIN DMS_Association a 	ON a.DMS_Content_ID = c.DMS_Content_ID 																	"
																						+ " INNER JOIN DMS_Association aa 	ON aa.DMS_Content_Related_ID = a.DMS_Content_Related_ID  OR aa.DMS_Content_Related_ID = c.DMS_Content_ID"
																						+ " WHERE aa.DMS_Content_ID = ? AND COALESCE(aa.DMS_AssociationType_ID, 0) <> 1000003 														";

	public static final String				SQL_GET_VERSION_SEQ_NO					= "SELECT NVL(MAX(SeqNo), 0) + 1  FROM DMS_Version WHERE DMS_Content_ID = ? AND AD_Client_ID = ?";

	public static final String				SQL_GET_ASI_INFO						= "SELECT REPLACE(a.Name,' ','_') AS Name, ai.Value, ai.ValueTimestamp, ai.ValueNumber, ai.ValueInt FROM M_AttributeInstance ai "
																						+ " INNER JOIN M_Attribute a ON (ai.M_Attribute_ID = a.M_Attribute_ID) "
																						+ " WHERE ai.M_AttributeSetInstance_ID = ?";

	public static final String				SQL_CONTENT_FROM_ASI					= "SELECT DMS_Content_ID FROM DMS_Content WHERE M_AttributeSetInstance_ID = ? 		";

	public static final String				SQL_GET_RELATED_CONTENT					= "SELECT c.DMS_Content_ID, a.DMS_Association_ID FROM DMS_Content c					"
																						+ " INNER JOIN DMS_Association a ON c.DMS_Content_ID = a.DMS_Content_ID 		"
																						+ " WHERE a.DMS_Content_Related_ID = ? AND c.IsActive='Y' AND a.IsActive='Y' 	";

	public static final String				SQL_GET_ASSOCIATION_FOR_COPY_PASTE		= "SELECT DMS_Association_ID, DMS_Content_ID FROM DMS_Association 					"
																						+ " WHERE DMS_Content_Related_ID = ? AND DMS_AssociationType_ID = ? OR DMS_Content_ID = ? AND DMS_AssociationType_ID != ?"
																						+ " ORDER BY DMS_Association_ID													";
	// TODO - Refactor Query
	public static final String				SQL_GET_CONTENT_DIR_LEVEL_WISE			= " WITH ContentAssociation AS ( 																										"
																						+ " 	SELECT 	c.DMS_Content_ID, a.DMS_Content_Related_ID, c.ContentBasetype, a.DMS_Association_ID, a.DMS_AssociationType_ID, a.AD_Table_ID, a.Record_ID "
																						+ " 	FROM 	DMS_Association a 																									"
																						+ " 	JOIN 	DMS_Content c 			ON ( c.DMS_Content_ID = a.DMS_Content_ID #IsActive# ) 										"
																						+ " 	WHERE 	c.AD_Client_ID = ? AND NVL(a.DMS_Content_Related_ID, 0) = ? 														"
																						+ " ), 																																"
																						+ " VersionList AS ( 																												"
																						+ " 	SELECT 	vr.DMS_Content_ID, a.DMS_Content_Related_ID, a.DMS_AssociationType_ID, NVL(a.AD_Table_ID, 0) AS AD_Table_ID, NVL(a.Record_ID, 0) AS Record_ID, MAX(vr.SeqNo) AS SeqNo "
																						+ " 	FROM 	DMS_Association a 																									"
																						+ "		JOIN 	ContentAssociation ca 	ON ( ca.DMS_Content_ID = a.DMS_Content_Related_ID OR ca.DMS_Content_Related_ID = a.DMS_Content_Related_ID ) "
																						+ "		JOIN 	DMS_Version vr 			ON ( vr.DMS_Content_ID = ca.DMS_Content_ID AND vr.IsActive='Y' )							"
																						+ " 	WHERE 	a.AD_Client_ID=? AND NVL(a.DMS_Content_Related_ID, 0)=? AND NVL(a.DMS_AssociationType_ID, 0) IN (0, 1000001, 1000002, 1000003)"
																						+ " 	GROUP BY vr.DMS_Content_ID, a.DMS_Content_Related_ID, a.DMS_AssociationType_ID, NVL(a.AD_Table_ID, 0), NVL(a.Record_ID, 0) 	"
																						+ " ) 																																"
																						+ " SELECT  DISTINCT																												"
																						+ "			NVL(a.DMS_Content_ID, 			c.DMS_Content_ID) 			AS DMS_Content_ID, 											"
																						+ " 		NVL(a.DMS_Content_Related_ID, 	c.DMS_Content_Related_ID) 	AS DMS_Content_Related_ID,									"
																						+ " 		NVL(a.DMS_Association_ID, 		c.DMS_Association_ID) 		AS DMS_Association_ID, 										"
																						+ " 		NVL(a.DMS_AssociationType_ID, 	c.DMS_AssociationType_ID)	AS DMS_AssociationType_ID, 									"
																						+ " 		vrs.DMS_Version_ID, vrs.SeqNo																							"
																						+ " FROM 		ContentAssociation c 																								"
																						+ " LEFT JOIN 	VersionList v		ON (v.DMS_Content_ID = c.DMS_Content_ID) 														"
																						+ " LEFT JOIN 	DMS_Association a 	ON (a.DMS_Content_ID = c.DMS_Content_ID AND a.DMS_Content_Related_ID = c.DMS_Content_Related_ID "
																						+ "										AND NVL(a.DMS_AssociationType_ID, 0) = NVL(c.DMS_AssociationType_ID, 0))					"
																						+ " LEFT JOIN 	DMS_Version vrs 	ON (vrs.DMS_Content_ID = c.DMS_Content_ID AND vrs.SeqNo = NVL(v.SeqNo, 0))  					"
																						+ " WHERE 		(NVL(c.DMS_Content_Related_ID,0) = ?) 																				";
	// OR (NVL(c.DMS_Content_Related_ID,0) = ? AND c.ContentBaseType = 'DIR')";

	public static String					SQL_GET_CONTENT_DIR_LEVEL_WISE_ALL		= SQL_GET_CONTENT_DIR_LEVEL_WISE.replace("#IsActive#", "");

	public static String					SQL_GET_CONTENT_DIR_LEVEL_WISE_ACTIVE	= SQL_GET_CONTENT_DIR_LEVEL_WISE.replace(	"#IsActive#",
																																"AND c.IsActive='Y' AND a.IsActive='Y'");
	// Restrict copy paste while parent directory copy paste into itself or it's child directory.
	public static String					SQL_CHECK_HIERARCHY_CONTENT_RECURSIVELY	= " WITH RECURSIVE ContentHierarchy AS "
																						+ " (	SELECT DMS_Content_ID,DMS_Content_Related_ID FROM DMS_Association "
																						+ "		WHERE  AD_Client_ID = ? AND DMS_Content_ID = ? "
																						+ "	 UNION"
																						+ "		SELECT a.DMS_Content_ID, a.DMS_Content_Related_ID FROM DMS_Association a "
																						+ " 	JOIN ContentHierarchy h ON (h.DMS_Content_Related_ID = a.DMS_Content_ID)"
																						+ " ) SELECT DMS_Content_ID FROM ContentHierarchy WHERE DMS_Content_ID = ? ";

	/*
	 * Access & Permission
	 */
	public static final String				SQL_GET_CONTENT_ON_CONTENTTTYPE_ACCESS	= "SELECT c.DMS_Content_ID FROM DMS_Content c "
																						+ " LEFT JOIN DMS_ContentType_Access ca ON (c.DMS_ContentType_ID = ca.DMS_ContentType_ID AND ca.IsActive = 'Y') "
																						+ " WHERE (ca.DMS_ContentType_ID IS NULL OR (ca.DMS_ContentType_ID IS NOT NULL AND ca.AD_Role_ID = ?)) ";

}
