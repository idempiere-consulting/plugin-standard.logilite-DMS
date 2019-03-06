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

package org.idempiere.webui.apps.form;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TimeZone;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.util.Callback;
import org.adempiere.webui.adwindow.AbstractADWindowContent;
import org.adempiere.webui.adwindow.BreadCrumbLink;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Checkbox;
import org.adempiere.webui.component.Column;
import org.adempiere.webui.component.Columns;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.Datebox;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Menupopup;
import org.adempiere.webui.component.NumberBox;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Searchbox;
import org.adempiere.webui.component.Tab;
import org.adempiere.webui.component.Tabbox;
import org.adempiere.webui.component.Tabpanel;
import org.adempiere.webui.component.Tabpanels;
import org.adempiere.webui.component.Tabs;
import org.adempiere.webui.component.Textbox;
import org.adempiere.webui.component.ZkCssHelper;
import org.adempiere.webui.editor.WDateEditor;
import org.adempiere.webui.editor.WDatetimeEditor;
import org.adempiere.webui.editor.WEditor;
import org.adempiere.webui.editor.WNumberEditor;
import org.adempiere.webui.editor.WTableDirEditor;
import org.adempiere.webui.editor.WTimeEditor;
import org.adempiere.webui.event.DialogEvents;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.window.FDialog;
import org.compiere.model.MClientInfo;
import org.compiere.model.MColumn;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MRole;
import org.compiere.model.MUser;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Language;
import org.compiere.util.Util;
import org.idempiere.componenet.AbstractComponentIconViewer;
import org.idempiere.dms.DMS;
import org.idempiere.dms.DMS_ZK_Util;
import org.idempiere.dms.constant.DMSConstant;
import org.idempiere.dms.factories.DMSClipboard;
import org.idempiere.dms.factories.Utils;
import org.idempiere.model.I_DMS_Association;
import org.idempiere.model.I_DMS_Content;
import org.idempiere.model.MDMSAssociation;
import org.idempiere.model.MDMSContent;
import org.idempiere.model.MDMSContentType;
import org.idempiere.model.MDMSMimeType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Space;
import org.zkoss.zul.Splitter;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.impl.XulElement;

import com.lowagie.text.DocumentException;

public class WDMSPanel extends Panel implements EventListener<Event>, ValueChangeListener
{
	private static final long		serialVersionUID		= -6813481516566180243L;
	private static CLogger			log						= CLogger.getCLogger(WDMSPanel.class);

	public static final String		ATTRIBUTE_TOGGLE		= "Toggle";
	private String					currThumbViewerAction	= DMSConstant.ICON_VIEW_LARGE;

	private Tabbox					tabBox					= new Tabbox();
	private Tabs					tabs					= new Tabs();
	private Tabpanels				tabPanels				= new Tabpanels();

	private Grid					grid					= GridFactory.newGridLayout();
	private Grid					gridBreadCrumb			= GridFactory.newGridLayout();

	private BreadCrumbLink			breadCrumbEvent			= null;

	private Rows					breadRows				= new Rows();
	private Row						breadRow				= new Row();

	private Searchbox				vsearchBox				= new Searchbox();

	private Label					lblAdvanceSearch		= new Label(DMSConstant.MSG_ADVANCE_SEARCH);
	private Label					lblDocumentName			= new Label(DMSConstant.MSG_NAME);
	private Label					lblContentType			= new Label(DMSConstant.MSG_CONTENT_TYPE);
	private Label					lblCreated				= new Label(DMSConstant.MSG_CREATED);
	private Label					lblUpdated				= new Label(DMSConstant.MSG_UPDATED);
	private Label					lblContentMeta			= new Label(DMSConstant.MSG_CONTENT_META);
	private Label					lblDescription			= new Label(DMSConstant.MSG_DESCRIPTION);
	private Label					lblCreatedBy			= new Label(DMSConstant.MSG_CREATEDBY);
	private Label					lblUpdatedBy			= new Label(DMSConstant.MSG_UPDATEDBY);
	private Label					lblPositionInfo			= new Label();
	private Label					lblShowBreadCrumb		= null;

	private Datebox					dbCreatedTo				= new Datebox();
	private Datebox					dbCreatedFrom			= new Datebox();
	private Datebox					dbUpdatedTo				= new Datebox();
	private Datebox					dbUpdatedFrom			= new Datebox();

	private ConfirmPanel			confirmPanel			= new ConfirmPanel();

	private Button					btnClear				= confirmPanel.createButton(ConfirmPanel.A_RESET);
	private Button					btnRefresh				= confirmPanel.createButton(ConfirmPanel.A_REFRESH);
	private Button					btnCloseTab				= confirmPanel.createButton(ConfirmPanel.A_CANCEL);
	private Button					btnSearch				= new Button();
	private Button					btnCreateDir			= new Button();
	private Button					btnUploadContent		= new Button();
	private Button					btnBack					= new Button();
	private Button					btnNext					= new Button();
	private Button					btnToggleView			= new Button();

	private Textbox					txtDocumentName			= new Textbox();
	private Textbox					txtDescription			= new Textbox();

	private WTableDirEditor			lstboxContentType		= null;
	private WTableDirEditor			lstboxCreatedBy			= null;
	private WTableDirEditor			lstboxUpdatedBy			= null;
	private Checkbox				chkInActive				= new Checkbox();

	private DMS						dms						= null;
	private MDMSContent				currDMSContent			= null;
	private MDMSContent				nextDMSContent			= null;
	private MDMSContent				copyDMSContent			= null;
	private MDMSContent				dirContent				= null;
	private MDMSAssociation			previousDMSAssociation	= null;

	private Stack<MDMSContent>		selectedDMSContent		= new Stack<MDMSContent>();
	private Stack<MDMSAssociation>	selectedDMSAssociation	= new Stack<MDMSAssociation>();

	//
	private Component				compCellRowViewer		= null;
	private WUploadContent			uploadContent			= null;
	private WCreateDirectoryForm	createDirectoryForm		= null;
	private WDLoadASIPanel			asiPanel				= null;

	private Panel					panelAttribute			= new Panel();

	private Menupopup				contentContextMenu		= new Menupopup();
	private Menupopup				canvasContextMenu		= new Menupopup();

	private Menuitem				mnu_cut					= null;
	private Menuitem				mnu_copy				= null;
	private Menuitem				mnu_paste				= null;
	private Menuitem				mnu_rename				= null;
	private Menuitem				mnu_delete				= null;
	private Menuitem				mnu_download			= null;
	private Menuitem				mnu_associate			= null;
	private Menuitem				mnu_createLink			= null;
	private Menuitem				mnu_versionList			= null;
	private Menuitem				mnu_uploadVersion		= null;

	private Menuitem				mnu_canvasPaste			= null;
	private Menuitem				mnu_canvasCreateLink	= null;

	private int						recordID				= 0;
	private int						tableID					= 0;
	private int						windowID				= 0;

	private boolean					isSearch				= false;
	private boolean					isGenericSearch			= false;
	private boolean					isAllowCreateDirectory	= true;
	private boolean					isWindowAccess			= true;

	private ArrayList<WEditor>		m_editors				= new ArrayList<WEditor>();

	private Map<String, WEditor>	ASI_Value				= new HashMap<String, WEditor>();

	private AbstractADWindowContent	winContent;

	/**
	 * Constructor initialize
	 */
	public WDMSPanel()
	{
		dms = new DMS(Env.getAD_Client_ID(Env.getCtx()));

		try
		{
			initForm();
			renderViewer();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "Render Component Problem.", e);
			throw new AdempiereException("Render Component Problem: " + e);
		}
	}

	public WDMSPanel(int Table_ID, int Record_ID, AbstractADWindowContent winContent)
	{
		this();
		this.winContent = winContent;
		this.windowID = winContent.getADWindow().getAD_Window_ID();
		isWindowAccess = MRole.getDefault().getWindowAccess(windowID);

		setTable_ID(Table_ID);
		setRecord_ID(Record_ID);

		dms.initMountingStrategy(null);
		currDMSContent = dms.getRootContent(Table_ID, Record_ID);

		/*
		 * Navigation and createDir buttons are disabled based on
		 * "IsAllowCreateDirectory" check on client info.
		 */
		MClientInfo clientInfo = MClientInfo.get(Env.getCtx(), Env.getAD_Client_ID(Env.getCtx()));
		isAllowCreateDirectory = clientInfo.get_ValueAsBoolean("IsAllowCreateDirectory");
		if (isTabViewer() && !isAllowCreateDirectory)
		{
			btnBack.setEnabled(false);
			btnNext.setEnabled(false);
			btnCreateDir.setEnabled(false);
		}

		btnCreateDir.setEnabled(isWindowAccess);
		btnUploadContent.setEnabled(isWindowAccess);
	}

	public int getRecord_ID()
	{
		return recordID;
	}

	public void setRecord_ID(int record_ID)
	{
		this.recordID = record_ID;
	}

	public int getTable_ID()
	{
		return tableID;
	}

	public void setWindow_ID(int AD_Window_ID)
	{
		this.windowID = AD_Window_ID;
	}

	public int getWindow_ID()
	{
		return windowID;
	}

	public void setTable_ID(int table_ID)
	{
		this.tableID = table_ID;
	}

	public MDMSContent getCurrDMSContent()
	{
		return currDMSContent;
	}

	public void setCurrDMSContent(MDMSContent currDMSContent)
	{
		this.currDMSContent = currDMSContent;
		selectedDMSContent.add(currDMSContent);
	}

	public boolean isTabViewer()
	{
		return (tableID > 0 && recordID > 0);
	}

	/**
	 * initialize components
	 */
	private void initForm()
	{
		this.setHeight("100%");
		this.setWidth("100%");
		this.appendChild(tabBox);
		this.addEventListener(Events.ON_CLICK, this);
		this.addEventListener(Events.ON_DOUBLE_CLICK, this);

		grid.setSclass("SB-Grid");
		grid.addEventListener(Events.ON_RIGHT_CLICK, this); // For_Canvas_Context_Menu
		grid.setStyle("width: 100%; height: 95%; position: relative; overflow: auto;");

		// View Result Tab
		Grid btnGrid = GridFactory.newGridLayout();
		// Rows Header Buttons
		Rows rowsBtn = btnGrid.newRows();

		// Row Navigation Button
		DMS_ZK_Util.setButtonData(btnBack, "Left24.png", DMSConstant.TTT_PREVIOUS_RECORD, this);
		btnBack.setEnabled(false);

		lblPositionInfo.setHflex("1");
		ZkCssHelper.appendStyle(lblPositionInfo, "float: right; font-weight: bold; text-align: center;");

		btnNext.setEnabled(false);
		btnNext.setStyle("float:right;");
		DMS_ZK_Util.setButtonData(btnNext, "Right24.png", DMSConstant.TTT_NEXT_RECORD, this);

		Row row = rowsBtn.newRow();
		row.appendChild(btnBack);
		row.appendChild(lblPositionInfo);
		row.appendChild(btnNext);

		// Row Operation - Create Directory, Upload Content
		DMS_ZK_Util.setButtonData(btnCreateDir, "Folder24.png", DMSConstant.MSG_CREATE_DIRECTORY, this);
		DMS_ZK_Util.setButtonData(btnUploadContent, "Upload24.png", DMSConstant.MSG_UPLOAD_CONTENT, this);

		row = rowsBtn.newRow();
		row.appendChild(btnCreateDir);
		row.appendChild(btnUploadContent);

		//
		Grid searchGridView = GridFactory.newGridLayout();
		searchGridView.setVflex(true);
		searchGridView.setStyle("max-height: 100%; width: 100%; position: relative; overflow: auto;");

		Rows rowsSearch = searchGridView.newRows();

		row = rowsSearch.newRow();
		DMS_ZK_Util.createCellUnderRow(row, 1, 3, vsearchBox);

		DMS_ZK_Util.setButtonData(vsearchBox.getButton(), "Search16.png", DMSConstant.TTT_SEARCH, this);
		vsearchBox.addEventListener(Events.ON_OK, this);

		row = rowsSearch.newRow();
		row.appendCellChild(lblAdvanceSearch);
		lblAdvanceSearch.setHflex("1");
		ZkCssHelper.appendStyle(lblAdvanceSearch, "font-weight: bold;");

		row = rowsSearch.newRow();
		row.appendChild(lblDocumentName);
		DMS_ZK_Util.createCellUnderRow(row, 0, 2, txtDocumentName);
		ZkCssHelper.appendStyle(lblDocumentName, "font-weight: bold;");
		txtDocumentName.setWidth("100%");

		row = rowsSearch.newRow();
		row.appendChild(lblDescription);
		DMS_ZK_Util.createCellUnderRow(row, 0, 2, txtDescription);
		txtDescription.setWidth("100%");

		Language lang = Env.getLanguage(Env.getCtx());
		int Column_ID = MColumn.getColumn_ID(MUser.Table_Name, MUser.COLUMNNAME_AD_User_ID);
		MLookup lookup = null;
		try
		{
			lookup = MLookupFactory.get(Env.getCtx(), 0, Column_ID, DisplayType.TableDir, lang, MUser.COLUMNNAME_AD_User_ID, 0, true, "");
			lstboxCreatedBy = new WTableDirEditor(MUser.COLUMNNAME_AD_User_ID, false, false, true, lookup);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "User fetching failure :", e);
			throw new AdempiereException("User fetching failure :" + e);
		}

		row = rowsSearch.newRow();
		row.setAlign("right");
		row.appendChild(lblCreatedBy);
		DMS_ZK_Util.createCellUnderRow(row, 0, 2, lstboxCreatedBy.getComponent());
		lblCreatedBy.setStyle("float: left;");
		lstboxCreatedBy.getComponent().setWidth("100%");

		Column_ID = MColumn.getColumn_ID(MUser.Table_Name, MUser.COLUMNNAME_AD_User_ID);
		lookup = null;
		try
		{
			lookup = MLookupFactory.get(Env.getCtx(), 0, Column_ID, DisplayType.TableDir, lang, MUser.COLUMNNAME_AD_User_ID, 0, true, "");
			lstboxUpdatedBy = new WTableDirEditor(MUser.COLUMNNAME_AD_User_ID, false, false, true, lookup);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "User fetching failure :", e);
			throw new AdempiereException("User fetching failure :" + e);
		}

		row = rowsSearch.newRow();
		row.setAlign("right");
		row.appendChild(lblUpdatedBy);
		DMS_ZK_Util.createCellUnderRow(row, 0, 2, lstboxUpdatedBy.getComponent());
		lblUpdatedBy.setStyle("float: left;");
		lstboxUpdatedBy.getComponent().setWidth("100%");

		dbCreatedFrom.setStyle(DMSConstant.CSS_DATEBOX);
		dbUpdatedFrom.setStyle(DMSConstant.CSS_DATEBOX);
		dbCreatedTo.setStyle(DMSConstant.CSS_DATEBOX);
		dbUpdatedTo.setStyle(DMSConstant.CSS_DATEBOX);

		//
		row = rowsSearch.newRow();
		row.appendChild(lblCreated);
		Hbox hbox = new Hbox();
		hbox.appendChild(dbCreatedFrom);
		hbox.appendChild(dbCreatedTo);
		DMS_ZK_Util.createCellUnderRow(row, 0, 2, hbox);

		//
		row = rowsSearch.newRow();
		row.appendChild(lblUpdated);
		hbox = new Hbox();
		hbox.appendChild(dbUpdatedFrom);
		hbox.appendChild(dbUpdatedTo);
		DMS_ZK_Util.createCellUnderRow(row, 0, 2, hbox);

		Column_ID = MColumn.getColumn_ID(MDMSContentType.Table_Name, MDMSContentType.COLUMNNAME_DMS_ContentType_ID);
		lookup = null;
		try
		{
			lookup = MLookupFactory.get(Env.getCtx(), 0, Column_ID, DisplayType.TableDir, lang, MDMSContentType.COLUMNNAME_DMS_ContentType_ID, 0, true, "");
			lstboxContentType = new WTableDirEditor(MDMSContentType.COLUMNNAME_DMS_ContentType_ID, false, false, true, lookup);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "Contenttype fetching failure :", e);
			throw new AdempiereException("Contenttype fetching failure :" + e);
		}

		//
		row = rowsSearch.newRow();
		row.setAlign("right");
		row.appendChild(lblContentType);
		DMS_ZK_Util.createCellUnderRow(row, 0, 2, lstboxContentType.getComponent());
		lblContentType.setStyle("float: left;");
		lstboxContentType.getComponent().setWidth("100%");
		lstboxContentType.addValueChangeListener(this);

		//
		row = rowsSearch.newRow();
		row.setStyle("padding-left : 109px;");
		DMS_ZK_Util.createCellUnderRow(row, 0, 2, chkInActive);
		chkInActive.setChecked(false);
		chkInActive.setLabel(DMSConstant.MSG_SHOW_IN_ACTIVE);
		chkInActive.addEventListener(Events.ON_CLICK, this);

		//
		row = rowsSearch.newRow();
		row.appendChild(lblContentMeta);
		ZkCssHelper.appendStyle(lblContentMeta, "font-weight: bold;");

		//
		row = rowsSearch.newRow();
		DMS_ZK_Util.createCellUnderRow(row, 0, 3, panelAttribute);

		//
		row = rowsSearch.newRow();

		DMS_ZK_Util.setButtonData(btnClear, "Reset24.png", "", this);
		DMS_ZK_Util.setButtonData(btnSearch, "Search24.png", DMSConstant.TTT_SEARCH, this);
		DMS_ZK_Util.setButtonData(btnRefresh, "Refresh24.png", "", this);
		DMS_ZK_Util.setButtonData(btnCloseTab, "Close24.png", "", this);
		DMS_ZK_Util.setButtonData(btnToggleView, "List16.png", DMSConstant.TTT_DISPLAYS_ITEMS_LAYOUT, this);

		btnToggleView.setAttribute(ATTRIBUTE_TOGGLE, currThumbViewerAction);
		btnToggleView.setStyle("float: left; padding: 5px 7px; margin: 0px 0px 5px 0px !important;");

		hbox = new Hbox();
		hbox.setStyle(DMSConstant.CSS_FLEX_ROW_DIRECTION);
		hbox.appendChild(btnSearch);
		hbox.appendChild(btnRefresh);
		hbox.appendChild(btnClear);
		hbox.appendChild(btnCloseTab);

		Cell cell = DMS_ZK_Util.createCellUnderRow(row, 1, 3, hbox);
		cell.setAlign("right");

		/*
		 * Main Layout View
		 */
		Cell cell_layout = new Cell();
		cell_layout.setWidth("70%");
		cell_layout.appendChild(btnToggleView);
		cell_layout.appendChild(gridBreadCrumb);
		cell_layout.appendChild(grid);

		gridBreadCrumb.setStyle("font-family: Roboto,sans-serif; min-height: 32px; "
				+ "border: 1px solid #AAA !important; border-radius: 5px; box-shadow: 1px 1px 1px 0px;");

		breadRow.setZclass("none");
		breadRow.setStyle(DMSConstant.CSS_FLEX_ROW_DIRECTION);

		Splitter splitter = new Splitter();
		splitter.setCollapse("after");

		Cell cell_attribute = new Cell();
		cell_attribute.setWidth("30%");
		cell_attribute.setHeight("100%");
		cell_attribute.appendChild(btnGrid);
		cell_attribute.appendChild(searchGridView);

		Hbox boxViewSeparator = new Hbox();
		boxViewSeparator.setWidth("100%");
		boxViewSeparator.setHeight("100%");
		boxViewSeparator.appendChild(cell_layout);
		boxViewSeparator.appendChild(splitter);
		boxViewSeparator.appendChild(cell_attribute);

		Tabpanel tabViewPanel = new Tabpanel();
		tabViewPanel.setHeight("100%");
		tabViewPanel.setWidth("100%");
		tabViewPanel.appendChild(boxViewSeparator);
		tabPanels.appendChild(tabViewPanel);

		tabBox.setWidth("100%");
		tabBox.setHeight("100%");
		tabBox.appendChild(tabs);
		tabBox.appendChild(tabPanels);
		tabBox.addEventListener(Events.ON_SELECT, this);

		tabs.appendChild(new Tab(DMSConstant.MSG_EXPLORER));

		// Context Menu item for Right click on DMSContent
		mnu_cut = DMS_ZK_Util.createMenuItem(contentContextMenu, DMSConstant.MENUITEM_CUT, "Cut24.png", this);
		mnu_copy = DMS_ZK_Util.createMenuItem(contentContextMenu, DMSConstant.MENUITEM_COPY, "Copy24.png", this);
		mnu_paste = DMS_ZK_Util.createMenuItem(contentContextMenu, DMSConstant.MENUITEM_PASTE, "Paste24.png", this);
		mnu_rename = DMS_ZK_Util.createMenuItem(contentContextMenu, DMSConstant.MENUITEM_RENAME, "Rename24.png", this);
		mnu_delete = DMS_ZK_Util.createMenuItem(contentContextMenu, DMSConstant.MENUITEM_DELETE, "Delete24.png", this);
		mnu_download = DMS_ZK_Util.createMenuItem(contentContextMenu, DMSConstant.MENUITEM_DOWNLOAD, "Downloads24.png", this);
		mnu_associate = DMS_ZK_Util.createMenuItem(contentContextMenu, DMSConstant.MENUITEM_ASSOCIATE, "Associate24.png", this);
		mnu_createLink = DMS_ZK_Util.createMenuItem(contentContextMenu, DMSConstant.MENUITEM_CREATELINK, "Link24.png", this);
		mnu_versionList = DMS_ZK_Util.createMenuItem(contentContextMenu, DMSConstant.MENUITEM_VERSIONlIST, "Versions24.png", this);
		mnu_uploadVersion = DMS_ZK_Util.createMenuItem(contentContextMenu, DMSConstant.MENUITEM_UPLOADVERSION, "uploadversion24.png", this);

		// Context Menu item for Right click on Canvas area
		mnu_canvasPaste = DMS_ZK_Util.createMenuItem(canvasContextMenu, DMSConstant.MENUITEM_PASTE, "Paste24.png", this);
		mnu_canvasCreateLink = DMS_ZK_Util.createMenuItem(canvasContextMenu, DMSConstant.MENUITEM_CREATELINK, "Link24.png", this);

		//
		DMSConstant.SDF_DATE_FORMAT_WITH_TIME.setTimeZone(TimeZone.getTimeZone("UTC"));
		addRootBreadCrumb();
		SessionManager.getAppDesktop();
	}

	@Override
	public void onEvent(Event event) throws Exception
	{
		log.info(event.getName());

		if (Events.ON_DOUBLE_CLICK.equals(event.getName()) && (event.getTarget() instanceof Cell || event.getTarget() instanceof Row))
		{
			openDirectoryORContent(event.getTarget());
		}
		else if (event.getTarget().equals(btnCreateDir))
		{
			if (isWindowAccess)
				createDirectory();
		}
		else if (event.getTarget().equals(btnUploadContent))
		{
			if (isWindowAccess)
				uploadContent();
		}
		else if (event.getTarget().equals(btnBack))
		{
			if (isSearch || isGenericSearch)
			{
				isSearch = false;
				isGenericSearch = false;
				currDMSContent = null;
				lblPositionInfo.setValue(null);
			}
			backNavigation();
		}
		else if (event.getTarget().equals(btnNext))
		{
			directoryNavigation();
		}
		else if (event.getTarget().getId().equals(ConfirmPanel.A_RESET))
		{
			// For solve navigation issue After search and reset button pressed.
			isGenericSearch = true;

			isSearch = false;
			clearComponents();
		}
		else if (event.getTarget().getId().equals(ConfirmPanel.A_CANCEL))
		{
			isSearch = false;
			breadRow.getChildren().clear();
			addRootBreadCrumb();

			if (isTabViewer())
			{
				MDMSContent mountingContent = dms.getMountingStrategy().getMountingParent(tableID, recordID);
				setCurrDMSContent(mountingContent);

				if (currDMSContent != null)
					lblPositionInfo.setText(currDMSContent.getName());
				else
					lblPositionInfo.setText(String.valueOf(recordID));
			}
			else
			{
				currDMSContent = null;
				lblPositionInfo.setText(null);
			}

//			btnBack.setEnabled(false);
//			btnNext.setEnabled(false);

			renderViewer();
		}
		else if (event.getTarget() == btnToggleView)
		{
			if (btnToggleView.getAttribute(ATTRIBUTE_TOGGLE).equals(DMSConstant.ICON_VIEW_LARGE))
				currThumbViewerAction = DMSConstant.ICON_VIEW_LIST;
			else
				currThumbViewerAction = DMSConstant.ICON_VIEW_LARGE;

			btnToggleView.setAttribute(ATTRIBUTE_TOGGLE, currThumbViewerAction);

			renderViewer();
		}
		else if (event.getTarget().getId().equals(ConfirmPanel.A_REFRESH) || event.getTarget().equals(btnSearch))
		{
			HashMap<String, List<Object>> params = getQueryParamas();
			String query = dms.buildSolrSearchQuery(params);

			if (query.equals("*:*") || query.startsWith("AD_Table_ID"))
			{
				isSearch = false;
				if (currDMSContent != null)
				{
					lblPositionInfo.setValue(currDMSContent.getName());
				}
				else
					lblPositionInfo.setValue(null);
			}
			else
			{
				isSearch = true;
				breadRow.getChildren().clear();
				btnBack.setEnabled(true);
				lblPositionInfo.setValue(null);
			}
			btnNext.setEnabled(false);

			renderViewer();
		}
		// Event for any area of panel user doing right click then show context
		// related paste or else...
		else if (Events.ON_RIGHT_CLICK.equals(event.getName()) && event.getTarget().getClass().equals(Grid.class))
		{
			openCanvasContextMenu(event);
		}
		else if (Events.ON_RIGHT_CLICK.equals(event.getName())
				&& (event.getTarget().getClass().equals(Cell.class) || event.getTarget().getClass().equals(Row.class)))
		{
			compCellRowViewer = event.getTarget();
			openContentContextMenu(compCellRowViewer);

			// show only download option on menu context if access are
			// read-only.
			if (!isWindowAccess)
			{
				mnu_download.setDisabled(false);
				mnu_copy.setDisabled(false);
			}
		}
		else if (event.getTarget().equals(mnu_versionList))
		{
			new WDMSVersion(dms, (MDMSContent) compCellRowViewer.getAttribute(DMSConstant.COMP_ATTRIBUTE_CONTENT));
		}
		else if (event.getTarget().equals(mnu_uploadVersion))
		{
			final WUploadContent uploadContent = new WUploadContent(dms, dirContent, true, this.getTable_ID(), this.getRecord_ID());
			uploadContent.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>() {

				@Override
				public void onEvent(Event e) throws Exception
				{
					if (!uploadContent.isCancel())
					{
						renderViewer();
					}
				}
			});
		}
		else if (event.getTarget().equals(mnu_copy))
		{
			DMSClipboard.put((MDMSContent) compCellRowViewer.getAttribute(DMSConstant.COMP_ATTRIBUTE_CONTENT), true);
		}
		else if (event.getTarget().equals(mnu_createLink))
		{
			linkCopyDocument(dirContent, true);
		}
		else if (event.getTarget().equals(mnu_cut))
		{
			DMSClipboard.put((MDMSContent) compCellRowViewer.getAttribute(DMSConstant.COMP_ATTRIBUTE_CONTENT), false);
		}
		else if (event.getTarget().equals(mnu_paste) || event.getTarget().equals(mnu_canvasPaste))
		{
			if (DMSClipboard.get() != null)
			{
				MDMSContent sourceContent = DMSClipboard.get();
				MDMSContent destPasteContent = dirContent;
				if (destPasteContent != null && sourceContent.get_ID() == destPasteContent.get_ID())
				{
					FDialog.warn(0, "You cannot Paste into itself");
				}
				else
				{
					if (DMSClipboard.getIsCopy())
						dms.pasteCopyContent(sourceContent, destPasteContent, tableID, recordID, isTabViewer());
					else
						dms.pasteCutContent(sourceContent, destPasteContent);
					renderViewer();
				}
			}
		}
		else if (event.getTarget().equals(mnu_download))
		{
			DMS_ZK_Util.downloadDocument(dms, dirContent);
		}
		else if (event.getTarget().equals(mnu_rename))
		{
			final WRenameContent renameContent = new WRenameContent(dms, dirContent);
			renameContent.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>() {

				@Override
				public void onEvent(Event e) throws Exception
				{
					if (!renameContent.isCancel())
					{
						renderViewer();
					}
				}
			});
		}
		else if (event.getTarget().equals(mnu_delete))
		{
			// TODO inactive DMS_content and same change in solr index

			Callback<Boolean> callback = new Callback<Boolean>() {
				@Override
				public void onCallback(Boolean result)
				{
					if (result)
					{
						dms.deleteContent((MDMSContent) compCellRowViewer.getAttribute(DMSConstant.COMP_ATTRIBUTE_CONTENT),
								(MDMSAssociation) compCellRowViewer.getAttribute(DMSConstant.COMP_ATTRIBUTE_ASSOCIATION));
						try
						{
							renderViewer();
						}
						catch (Exception e)
						{
							throw new AdempiereException(e);
						}
					}
					else
					{
						return;
					}
				}
			};

			FDialog.ask(0, this,
					"Are you sure to delete " + ((MDMSContent) compCellRowViewer.getAttribute(DMSConstant.COMP_ATTRIBUTE_CONTENT)).getName() + "?", callback);

		}
		else if (event.getTarget().equals(mnu_associate))
		{
			new WDAssociationType(dms, copyDMSContent, (MDMSContent) compCellRowViewer.getAttribute(DMSConstant.COMP_ATTRIBUTE_CONTENT), getTable_ID(),
					getRecord_ID(), winContent);
		}
		else if (event.getTarget().equals(mnu_canvasCreateLink))
		{
			linkCopyDocument(currDMSContent, false);
		}
		else if (event.getName().equals("onUploadComplete"))
		{
			renderViewer();
		}
		else if (event.getName().equals("onRenameComplete"))
		{
			Tab tab = (Tab) tabBox.getSelectedTab();
			renderViewer();
			tabBox.setSelectedTab(tab);
		}
		else if (Events.ON_CLICK.equals(event.getName()) && event.getTarget().getClass().equals(BreadCrumbLink.class))
		{
			renderBreadCrumb(event);
		}
		else if ((Events.ON_OK.equals(event.getName()) || Events.ON_CLICK.equals(event.getName()))
				&& event.getTarget().getClass().equals(vsearchBox.getButton().getClass()))
		{
			breadRow.getChildren().clear();
			btnBack.setEnabled(true);
			btnNext.setEnabled(false);
			lblPositionInfo.setValue(null);

			isSearch = false;
			isGenericSearch = true;
			renderViewer();
		}

		// Disable navigation button based on "isAllowCreateDirectory" check on
		// client info window
		if (isTabViewer() && !isAllowCreateDirectory)
		{
			btnBack.setEnabled(false);
			btnNext.setEnabled(false);
			btnCreateDir.setDisabled(false);
		}

	} // onEvent

	private void renderBreadCrumb(Event event) throws IOException, URISyntaxException
	{
		breadCrumbEvent = (BreadCrumbLink) event.getTarget();
		boolean isRoot = breadCrumbEvent.getPathId().equals("0");
		if (isTabViewer())
		{
			if (isRoot)
			{
				MDMSContent mountingContent = dms.getMountingStrategy().getMountingParent(tableID, recordID);
				breadCrumbEvent.setPathId(String.valueOf(mountingContent.getDMS_Content_ID()));
			}

			if (breadCrumbEvent.getImageContent() != null)
			{
				btnBack.setEnabled(false);
				btnNext.setEnabled(false);
			}
		}

		if (breadCrumbEvent.getPathId().equals("0"))
		{
			selectedDMSContent.removeAllElements();
			selectedDMSAssociation.removeAllElements();
			btnNext.setEnabled(false);
			btnBack.setEnabled(false);
		}

		int DMS_Content_ID = Integer.valueOf(breadCrumbEvent.getPathId());
		currDMSContent = new MDMSContent(Env.getCtx(), DMS_Content_ID, null);

		lblPositionInfo.setValue(currDMSContent.getName());

		List<BreadCrumbLink> parents = getParentLinks();
		if (!parents.isEmpty())
		{
			breadRow.getChildren().clear();
			Iterator<BreadCrumbLink> iterator = parents.iterator();
			while (iterator.hasNext())
			{
				BreadCrumbLink breadCrumbLink = (BreadCrumbLink) iterator.next();
				breadCrumbLink.setStyle(DMSConstant.CSS_BREAD_CRUMB_LINK);
				breadRow.appendChild(breadCrumbLink);

				if (Integer.valueOf(breadCrumbLink.getPathId()) == currDMSContent.getDMS_Content_ID())
					break;

				breadRow.appendChild(new Space());
				breadRow.appendChild(new Label(">"));
				breadRows.appendChild(breadRow);
				gridBreadCrumb.appendChild(breadRows);
			}
		}
		renderViewer();
	} // renderBreadCrumb

	/**
	 * @throws IOException
	 * @throws URISyntaxException Render the Thumb Component
	 */
	public void renderViewer() throws IOException, URISyntaxException
	{
		HashMap<I_DMS_Content, I_DMS_Association> contentsMap = null;

		// Setting current dms content value on label
		if (isTabViewer())
		{
			String currContentValue = currDMSContent != null ? String.valueOf(currDMSContent.getName()) : null;
			lblPositionInfo.setValue(currContentValue);
		}

		if (isSearch)
			contentsMap = dms.renderSearchedContent(getQueryParamas(), currDMSContent);
		else if (isGenericSearch)
			contentsMap = dms.getGenericSearchedContent(vsearchBox.getTextbox().getValue(), tableID, recordID, currDMSContent);
		else
			contentsMap = dms.getDMSContentsWithAssociation(currDMSContent);

		// TODO
		String[] eventsList = new String[] { Events.ON_RIGHT_CLICK, Events.ON_DOUBLE_CLICK };

		AbstractComponentIconViewer viewerComponent = (AbstractComponentIconViewer) DMS_ZK_Util.getDMSCompViewer(currThumbViewerAction);
		viewerComponent.init(dms, contentsMap, grid, DMSConstant.CONTENT_LARGE_ICON_WIDTH, DMSConstant.CONTENT_LARGE_ICON_HEIGHT, this, eventsList);

		tabBox.setSelectedIndex(0);
	}

	/**
	 * Clear the grid view components
	 */
	private void clearComponents()
	{
		vsearchBox.setText(null);
		txtDocumentName.setValue(null);
		txtDescription.setValue(null);
		lstboxContentType.setValue(null);
		lstboxCreatedBy.setValue(null);
		lstboxUpdatedBy.setValue(null);
		dbCreatedFrom.setValue(null);
		dbCreatedTo.setValue(null);
		dbUpdatedFrom.setValue(null);
		dbUpdatedTo.setValue(null);
		chkInActive.setChecked(false);

		if (m_editors != null)
		{
			for (WEditor editor : m_editors)
				editor.setValue(null);
		}
		Components.removeAllChildren(panelAttribute);
	} // clearComponents

	/**
	 * open the Directory OR Content
	 * 
	 * @param component - Cell, Row, etc
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws DocumentException
	 * @throws com.itextpdf.text.DocumentException
	 */
	private void openDirectoryORContent(Component component) throws IOException, URISyntaxException, DocumentException, com.itextpdf.text.DocumentException
	{
		selectedDMSContent.push((MDMSContent) component.getAttribute(DMSConstant.COMP_ATTRIBUTE_CONTENT));
		selectedDMSAssociation.push((MDMSAssociation) component.getAttribute(DMSConstant.COMP_ATTRIBUTE_ASSOCIATION));

		if (selectedDMSContent.peek().getContentBaseType().equals(MDMSContent.CONTENTBASETYPE_Directory))
		{
			currDMSContent = selectedDMSContent.pop();
			showBreadcumb(currDMSContent);
			renderViewer();
			lblPositionInfo.setValue(currDMSContent.getName());
			btnBack.setEnabled(true);
			btnNext.setEnabled(false);
		}
		else if (selectedDMSContent.peek().getContentBaseType().equals(MDMSContent.CONTENTBASETYPE_Content))
		{
			MDMSMimeType mimeType = (MDMSMimeType) selectedDMSContent.peek().getDMS_MimeType();
			File documentToPreview = dms.getFileFromStorage(selectedDMSContent.peek());

			if (documentToPreview != null)
			{
				String name = selectedDMSContent.peek().getName();

				if (name.contains("(") && name.contains(")"))
					name = name.replace(name.substring(name.lastIndexOf("("), name.lastIndexOf(")") + 1), "");

				try
				{
					documentToPreview = dms.convertToPDF(documentToPreview, mimeType);
				}
				catch (Exception e)
				{
					selectedDMSContent.pop();
					throw new AdempiereException(e);
				}

				if (Utils.getContentEditor(mimeType.getMimeType()) != null)
				{
					Tab tabData = new Tab(name);
					tabData.setClosable(true);
					tabs.appendChild(tabData);
					tabBox.setSelectedTab(tabData);

					WDocumentViewer documentViewer = new WDocumentViewer(dms, tabBox, documentToPreview, selectedDMSContent.peek(), tableID, recordID);
					Tabpanel tabPanel = documentViewer.initForm(isWindowAccess);
					tabPanels.appendChild(tabPanel);
					documentViewer.getAttributePanel().addEventListener("onUploadComplete", this);
					documentViewer.getAttributePanel().addEventListener("onRenameComplete", this);

					this.appendChild(tabBox);
				}
				else
				{
					DMS_ZK_Util.downloadDocument(documentToPreview);
				}
				// Fix for search --> download content --> back (which was
				// navigate to home/root folder)
				selectedDMSContent.pop();
			}
			else
			{
				FDialog.error(0, dms.getPathFromContentManager(currDMSContent) + " Content missing in storage,");
			}
		}
	} // openDirectoryORContent

	/**
	 * Navigate the Previous Directory.
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private void backNavigation() throws IOException, URISyntaxException
	{
		List<BreadCrumbLink> parents = getParentLinks();
		if (!parents.isEmpty())
		{
			breadRow.getChildren().clear();
			int count = 0;
			Iterator<BreadCrumbLink> iterator = parents.iterator();
			while (iterator.hasNext())
			{
				BreadCrumbLink breadCrumbLink = (BreadCrumbLink) iterator.next();
				breadCrumbLink.setStyle(DMSConstant.CSS_BREAD_CRUMB_LINK);

				if (currDMSContent != null && parents.size() > 1)
				{
					lblShowBreadCrumb = new Label(">");
					breadRow.appendChild(breadCrumbLink);
					breadRow.appendChild(new Space());
					breadRow.appendChild(lblShowBreadCrumb);

					count++;

					if (parents.size() - 1 == count)
					{
						breadRow.removeChild(lblShowBreadCrumb);
						break;
					}
				}
				breadRows.appendChild(breadRow);
				gridBreadCrumb.appendChild(breadRows);
			}

			if (currDMSContent == null)
			{
				addRootBreadCrumb();
			}
		}
		else
		{
			addRootBreadCrumb();
		}

		nextDMSContent = currDMSContent;

		if (selectedDMSAssociation != null && !selectedDMSAssociation.isEmpty() && Utils.isLink(selectedDMSAssociation.peek()) && currDMSContent != null
				&& currDMSContent.getDMS_Content_ID() == selectedDMSAssociation.peek().getDMS_Content_ID())
		{
			currDMSContent = new MDMSContent(Env.getCtx(), selectedDMSAssociation.peek().getDMS_Content_Related_ID(), null);
			lblPositionInfo.setValue(currDMSContent.getName());
			if (currDMSContent.getParentURL() == null)
				btnBack.setEnabled(true);

			btnNext.setEnabled(true);
		}
		else if (currDMSContent != null)
		{
			int DMS_Content_ID = DB.getSQLValue(null,
					"SELECT DMS_Content_Related_ID FROM DMS_Association WHERE DMS_Content_ID = ? AND DMS_AssociationType_ID IS NULL",
					currDMSContent.getDMS_Content_ID());

			if (DMS_Content_ID <= 0)
			{
				currDMSContent = null;
				lblPositionInfo.setValue("");
				btnBack.setEnabled(false);
			}
			else
			{
				currDMSContent = new MDMSContent(Env.getCtx(), DMS_Content_ID, null);
				lblPositionInfo.setValue(currDMSContent.getName());
				if (currDMSContent.getParentURL() == null)
					btnBack.setEnabled(true);
			}
			btnNext.setEnabled(true);
		}
		else
		{
			btnBack.setEnabled(false);
		}

		if (!selectedDMSAssociation.isEmpty())
			previousDMSAssociation = selectedDMSAssociation.pop();

		renderViewer();

		if (recordID > 0 && tableID > 0)
		{
			// Getting initial mounting content for disabling back navigation
			MDMSContent mountingContent = dms.getMountingStrategy().getMountingParent(tableID, recordID);
			if (currDMSContent == null)
				currDMSContent = selectedDMSContent.peek();

			if (currDMSContent.getDMS_Content_ID() == mountingContent.getDMS_Content_ID())
			{
				btnBack.setDisabled(true);
				renderViewer();
			}
			return;
		}
		btnClear.setEnabled(false);

	} // backNavigation

	/**
	 * Move in the Directory
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private void directoryNavigation() throws IOException, URISyntaxException
	{
		BreadCrumbLink breadCrumbLink = new BreadCrumbLink();
		breadCrumbLink.setPathId(nextDMSContent.getName());
		breadCrumbLink.setLabel(nextDMSContent.getName());
		breadCrumbLink.setStyle(DMSConstant.CSS_BREAD_CRUMB_LINK);

		breadRow.appendChild(new Label(" > "));
		breadRow.appendChild(breadCrumbLink);
		breadRows.appendChild(breadRow);
		gridBreadCrumb.appendChild(breadRows);

		if (nextDMSContent != null)
		{
			currDMSContent = nextDMSContent;
			if (previousDMSAssociation != null)
				selectedDMSAssociation.add(selectedDMSAssociation.size(), previousDMSAssociation);
			renderViewer();
			lblPositionInfo.setValue(currDMSContent.getName());
		}
		btnNext.setEnabled(false);
		btnBack.setEnabled(true);
	} // directoryNavigation

	/**
	 * Make Directory
	 */
	private void createDirectory()
	{
		createDirectoryForm = new WCreateDirectoryForm(dms, currDMSContent, tableID, recordID);

		createDirectoryForm.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception
			{
				renderViewer();
			}
		});
	} // createDirectory

	/**
	 * Upload Content
	 */
	private void uploadContent()
	{
		uploadContent = new WUploadContent(dms, currDMSContent, false, tableID, recordID);

		uploadContent.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception
			{
				renderViewer();
			}
		});
		uploadContent.addEventListener(Events.ON_CLOSE, this);
	} // uploadContent

	/**
	 * Open MenuPopup when Right click on Directory OR Content
	 * 
	 * @param compCellRowViewer
	 */
	private void openContentContextMenu(final Component compCellRowViewer)
	{
		dirContent = (MDMSContent) compCellRowViewer.getAttribute(DMSConstant.COMP_ATTRIBUTE_CONTENT);
		contentContextMenu.setPage(compCellRowViewer.getPage());
		copyDMSContent = DMSClipboard.get();

		if (!isWindowAccess || (dirContent.isMounting() && dirContent.getContentBaseType().equals(MDMSContent.CONTENTBASETYPE_Directory)))
		{
			ctxMenuItemDisabled(true);

			((XulElement) compCellRowViewer).setContext(contentContextMenu);
			contentContextMenu.open(this, "at_pointer");
			return;
		}
		else
		{
			ctxMenuItemDisabled(false);
		}

		if (copyDMSContent == null)
		{
			mnu_paste.setDisabled(true);
			mnu_associate.setDisabled(true);
			mnu_createLink.setDisabled(true);
			mnu_versionList.setDisabled(true);
			mnu_canvasPaste.setDisabled(true);
			mnu_uploadVersion.setDisabled(true);
		}
		else if (copyDMSContent == dirContent)
		{
			mnu_paste.setDisabled(true);
			mnu_associate.setDisabled(true);
			mnu_createLink.setDisabled(true);
			mnu_canvasPaste.setDisabled(true);
		}
		else if (MDMSContent.CONTENTBASETYPE_Directory.equals(dirContent.getContentBaseType()))
		{
			mnu_paste.setDisabled(false);
			mnu_associate.setDisabled(true);
			mnu_versionList.setDisabled(true);
			mnu_canvasPaste.setDisabled(false);
			mnu_uploadVersion.setDisabled(true);
		}
		else
		{
			mnu_associate.setDisabled(false);
			mnu_createLink.setDisabled(false);
		}

		if (MDMSContent.CONTENTBASETYPE_Content.equals(dirContent.getContentBaseType()))
		{
			mnu_paste.setDisabled(true);
			mnu_download.setDisabled(false);
			mnu_createLink.setDisabled(true);
			mnu_versionList.setDisabled(false);
			mnu_canvasPaste.setDisabled(true);
			mnu_uploadVersion.setDisabled(false);

			if (copyDMSContent != null && copyDMSContent != dirContent)
				mnu_associate.setDisabled(false);
			else
				mnu_associate.setDisabled(true);
		}

		if (MDMSContent.CONTENTBASETYPE_Directory.equals(dirContent.getContentBaseType()))
		{
			if (copyDMSContent != null)
				mnu_createLink.setDisabled(false);

			if (DMSClipboard.get() != null && !DMSClipboard.getIsCopy())
			{
				mnu_paste.setDisabled(false);
				mnu_canvasPaste.setDisabled(false);
			}
			mnu_download.setDisabled(true);
		}

		mnu_copy.setDisabled(false);

		((XulElement) compCellRowViewer).setContext(contentContextMenu);
		contentContextMenu.open(this, "at_pointer");

		if (Utils.isLink(((MDMSAssociation) compCellRowViewer.getAttribute(DMSConstant.COMP_ATTRIBUTE_ASSOCIATION))))
		{
			if (MDMSContent.CONTENTBASETYPE_Content.equals(dirContent.getContentBaseType()))
			{
				mnu_cut.setDisabled(true);
				mnu_copy.setDisabled(true);
				mnu_paste.setDisabled(true);
				mnu_rename.setDisabled(true);
				mnu_delete.setDisabled(false);
				mnu_download.setDisabled(false);
				mnu_associate.setDisabled(true);
				mnu_createLink.setDisabled(true);
				mnu_versionList.setDisabled(false);
				mnu_canvasPaste.setDisabled(true);
				mnu_uploadVersion.setDisabled(false);
			}
			else if (MDMSContent.CONTENTBASETYPE_Directory.equals(dirContent.getContentBaseType()))
			{
				ctxMenuItemDisabled(true);

				mnu_canvasPaste.setDisabled(true);
			}
		}
	} // openContentContextMenu

	/**
	 * @param isDisabled
	 */
	public void ctxMenuItemDisabled(boolean isDisabled)
	{
		mnu_cut.setDisabled(isDisabled);
		mnu_copy.setDisabled(isDisabled);
		mnu_paste.setDisabled(isDisabled);
		mnu_rename.setDisabled(isDisabled);
		mnu_delete.setDisabled(isDisabled);
		mnu_download.setDisabled(isDisabled);
		mnu_associate.setDisabled(isDisabled);
		mnu_createLink.setDisabled(isDisabled);
		mnu_versionList.setDisabled(isDisabled);
		mnu_uploadVersion.setDisabled(isDisabled);
	} // ctxMenuItemDisabled

	private void openCanvasContextMenu(Event event)
	{
		Component compCellRowViewer = event.getTarget();
		dirContent = currDMSContent;
		canvasContextMenu.setPage(compCellRowViewer.getPage());
		((XulElement) compCellRowViewer).setContext(canvasContextMenu);

		if (DMSClipboard.get() == null)
		{
			mnu_canvasCreateLink.setDisabled(true);
			mnu_canvasPaste.setDisabled(true);
		}
		else
		{
			mnu_canvasCreateLink.setDisabled(false);
			mnu_canvasPaste.setDisabled(false);
		}

		if (tableID <= 0 || recordID <= 0)
		{
			if (DMSClipboard.get() == null || (DMSClipboard.get() != null && !DMSClipboard.getIsCopy()))
				mnu_canvasPaste.setDisabled(true);
			else
				mnu_canvasPaste.setDisabled(false);
		}

		if (currDMSContent != null)
		{
			if (currDMSContent.isMounting() && !currDMSContent.getName().equals(String.valueOf(getRecord_ID())))
			{
				mnu_canvasCreateLink.setDisabled(true);
				mnu_canvasPaste.setDisabled(true);
			}
		}

		canvasContextMenu.open(this, "at_pointer");
	} // openCanvasContextMenu

	// TODO Need check for refactoring
	/**
	 * @param DMSContent
	 * @param isDir
	 */
	private void linkCopyDocument(MDMSContent DMSContent, boolean isDir)
	{
		String warnMsg = dms.createLink(DMSContent, currDMSContent, DMSClipboard.get(), isDir, tableID, recordID);
		if (!Util.isEmpty(warnMsg, true))
			FDialog.warn(0, warnMsg);

		try
		{
			renderViewer();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "Render content problem.", e);
			throw new AdempiereException("Render content problem: " + e);
		}
	} // linkCopyDocument

	private HashMap<String, List<Object>> getQueryParamas()
	{
		HashMap<String, List<Object>> params = new LinkedHashMap<String, List<Object>>();

		if (!Util.isEmpty(txtDocumentName.getValue(), true))
			setSearchParams(DMSConstant.NAME, "*" + txtDocumentName.getValue().toLowerCase() + "*", null, params);

		if (!Util.isEmpty(txtDescription.getValue(), true))
			setSearchParams(DMSConstant.DESCRIPTION, "*" + txtDescription.getValue().toLowerCase().trim() + "*", null, params);

		//
		if (dbCreatedFrom.getValue() != null && dbCreatedTo.getValue() != null)
		{
			if (dbCreatedFrom.getValue().after(dbCreatedTo.getValue()))
				throw new WrongValueException(dbCreatedFrom, "Invalid Date Range");
			else
				setSearchParams(DMSConstant.CREATED, dbCreatedFrom.getValue(), dbCreatedTo.getValue(), params);
		}
		else if (dbCreatedFrom.getValue() != null && dbCreatedTo.getValue() == null)
			setSearchParams(DMSConstant.CREATED, dbCreatedFrom.getValue(), "*", params);
		else if (dbCreatedTo.getValue() != null && dbCreatedFrom.getValue() == null)
			setSearchParams(DMSConstant.CREATED, "*", dbCreatedTo.getValue(), params);

		//
		if (dbUpdatedFrom.getValue() != null && dbUpdatedTo.getValue() != null)
		{
			if (dbUpdatedFrom.getValue().after(dbUpdatedTo.getValue()))
				throw new WrongValueException(dbUpdatedFrom, "Invalid Date Range");
			else
				setSearchParams(DMSConstant.UPDATED, dbUpdatedFrom.getValue(), dbUpdatedTo.getValue(), params);
		}
		else if (dbUpdatedFrom.getValue() != null && dbUpdatedTo.getValue() == null)
			setSearchParams(DMSConstant.UPDATED, dbUpdatedFrom.getValue(), "*", params);
		else if (dbUpdatedTo.getValue() != null && dbUpdatedFrom.getValue() == null)
			setSearchParams(DMSConstant.UPDATED, "*", dbUpdatedTo.getValue(), params);

		if (lstboxCreatedBy.getValue() != null)
			setSearchParams(DMSConstant.CREATEDBY, lstboxCreatedBy.getValue(), null, params);

		if (lstboxUpdatedBy.getValue() != null)
			setSearchParams(DMSConstant.UPDATEDBY, lstboxUpdatedBy.getValue(), null, params);

		// if chkInActive = true, display all files
		// if chkInActive = false, display only active files
		if (chkInActive != null)
			setSearchParams(DMSConstant.SHOW_INACTIVE, chkInActive.isChecked(), chkInActive.isChecked() ? false : null, params);

		//
		if (lstboxContentType.getValue() != null)
		{
			setSearchParams(DMSConstant.CONTENTTYPE, lstboxContentType.getValue(), null, params);

			for (WEditor editor : m_editors)
			{
				String compName = null;
				int dt = editor.getGridField().getDisplayType();

				if (dt == DisplayType.Search || dt == DisplayType.Table || dt == DisplayType.List)
					compName = "ASI_" + editor.getColumnName().replaceAll("(?i)[^a-z0-9-_/]", "_");
				else
					compName = "ASI_" + editor.getLabel().getValue().replaceAll("(?i)[^a-z0-9-_/]", "_");

				compName = compName.replaceAll("/", "");

				Object from = null;
				Object to = null;

				if (dt == DisplayType.Number || dt == DisplayType.Integer || dt == DisplayType.Quantity || dt == DisplayType.Amount
						|| dt == DisplayType.CostPrice)
				{
					NumberBox fromNumBox = (NumberBox) ASI_Value.get(compName).getComponent();
					NumberBox toNumBox = (NumberBox) ASI_Value.get(compName + "to").getComponent();

					if (fromNumBox.getValue() != null && toNumBox.getValue() != null)
					{
						if (dt == DisplayType.Number)
						{
							from = fromNumBox.getValue().doubleValue();
							to = toNumBox.getValue().doubleValue();
						}
						else
						{
							from = fromNumBox.getValue();
							to = toNumBox.getValue();
						}
					}
					else if (fromNumBox.getValue() != null && toNumBox.getValue() == null)
					{
						if (dt == DisplayType.Number)
							from = fromNumBox.getValue().doubleValue();
						else
							from = fromNumBox.getValue();
						to = "*";
					}
					else if (fromNumBox.getValue() == null && toNumBox.getValue() != null)
					{
						from = "*";
						if (dt == DisplayType.Number)
							to = toNumBox.getValue().doubleValue();
						else
							to = toNumBox.getValue();
					}
				}
				// Component:Date-Datebox, DateTime-DatetimeBox, Time-Timebox
				else if (dt == DisplayType.Date || dt == DisplayType.DateTime || dt == DisplayType.Time)
				{
					WEditor dataFrom = (WEditor) ASI_Value.get(compName);
					WEditor dataTo = (WEditor) ASI_Value.get(compName + "to");

					if (dataFrom.getValue() != null && dataTo.getValue() != null)
					{
						if (((Date) dataFrom.getValue()).after((Date) dataTo.getValue()))
						{
							Clients.scrollIntoView((Component) dataFrom);
							throw new WrongValueException((Component) dataFrom, "Invalid Date Range");
						}
						else
						{
							from = dataFrom.getValue();
							to = dataTo.getValue();
						}
					}
					else if (dataFrom.getValue() != null && dataTo.getValue() == null)
					{
						from = dataFrom.getValue();
						to = "*";
					}
					else if (dataTo.getValue() != null && dataFrom.getValue() == null)
					{
						from = "*";
						to = dataTo.getValue();
					}
				}
				else if (dt == DisplayType.YesNo)
				{
					from = ((boolean) editor.getValue() ? "Y" : "N");
					to = null;
				}
				else if (dt == DisplayType.String || dt == DisplayType.Text)
				{
					if (!Util.isEmpty(editor.getValue().toString(), true))
					{
						from = editor.getValue().toString().toLowerCase();
						to = null;
					}
				}
				else if (dt == DisplayType.TableDir)
				{
					if (editor.getValue() != null)
						from = editor.getDisplay();
					to = null;
				}
				else if (!Util.isEmpty(editor.getDisplay()))
				{
					from = editor.getValue();
					to = null;
				}

				//
				if (from != null || to != null)
					setSearchParams(compName, from, to, params);
			}
		}

		if (tableID > 0)
			setSearchParams(DMSConstant.AD_Table_ID, tableID, null, params);

		if (recordID > 0)
			setSearchParams(DMSConstant.RECORD_ID, recordID, null, params);

		return params;
	} // getQueryParamas

	private void setSearchParams(String searchAttributeName, Object data, Object data2, HashMap<String, List<Object>> params)
	{
		ArrayList<Object> value = new ArrayList<Object>();

		if (data instanceof Date || data instanceof Timestamp)
			value.add(DMSConstant.SDF_DATE_FORMAT_WITH_TIME.format(data));
		else if (data != null)
			value.add(data);

		if (data2 instanceof Date || data2 instanceof Timestamp)
			value.add(DMSConstant.SDF_DATE_FORMAT_WITH_TIME.format(data2));
		else if (data2 != null)
			value.add(data2);

		params.put(searchAttributeName, value);
	} // setSearchParams

	@Override
	public void valueChange(ValueChangeEvent event)
	{
		if (event.getSource().equals(lstboxContentType))
		{
			Components.removeAllChildren(panelAttribute);

			Columns columns = new Columns();
			columns.appendChild(new Column());
			columns.appendChild(new Column());
			columns.appendChild(new Column());

			Rows rows = new Rows();

			Grid gridView = GridFactory.newGridLayout();
			gridView.setHeight("100%");
			gridView.appendChild(rows);
			gridView.appendChild(columns);

			if (lstboxContentType.getValue() != null)
			{
				ASI_Value.clear();
				asiPanel = new WDLoadASIPanel((int) lstboxContentType.getValue(), 0);
				m_editors = asiPanel.m_editors;

				for (WEditor editor : m_editors)
				{
					String compName = null;
					int dt = editor.getGridField().getDisplayType();

					if (dt == DisplayType.Search || dt == DisplayType.Table)
						compName = "ASI_" + editor.getColumnName().replaceAll("(?i)[^a-z0-9-_/]", "_");
					else
						compName = "ASI_" + editor.getLabel().getValue().replaceAll("(?i)[^a-z0-9-_/]", "_");
					compName = compName.replaceAll("/", "");

					Row row = rows.newRow();
					row.appendChild(editor.getLabel());

					if (dt == DisplayType.Number || dt == DisplayType.Integer || dt == DisplayType.Quantity || dt == DisplayType.Amount
							|| dt == DisplayType.CostPrice)
					{
						WNumberEditor numBox = new WNumberEditor(compName + "to", false, false, true, dt, "SB");
						row.appendChild(editor.getComponent());
						row.appendChild(numBox.getComponent());

						ASI_Value.put(compName, editor);
						ASI_Value.put(compName + "to", numBox);
					}
					// Component:Date-Datebox, DateTime-DatetimeBox,
					// Time-Timebox
					else if (dt == DisplayType.Date || dt == DisplayType.DateTime || dt == DisplayType.Time)
					{
						WEditor compTo = null;

						if (dt == DisplayType.Date)
						{
							compTo = new WDateEditor(compName + "to", false, false, true, "");
							row.appendChild(editor.getComponent());
							row.appendChild(compTo.getComponent());
						}
						else if (dt == DisplayType.Time)
						{
							compTo = new WTimeEditor(compName + "to", false, false, true, "");
							row.appendChild(editor.getComponent());
							row.appendChild(compTo.getComponent());
							((Timebox) compTo.getComponent()).setFormat("h:mm:ss a");
							((Timebox) compTo.getComponent()).setWidth("100%");
						}
						else if (dt == DisplayType.DateTime)
						{
							compTo = new WDatetimeEditor(compName, false, false, true, "");
							row.appendCellChild(editor.getComponent(), 2);
							row = rows.newRow();
							row.appendChild(new Space());
							row.appendCellChild(compTo.getComponent(), 2);
						}
						ASI_Value.put(compName, editor);
						ASI_Value.put(compName + "to", compTo);
					}
					else
					{
						row.appendCellChild(editor.getComponent(), 2);
						ASI_Value.put(editor.getLabel().getValue(), editor);
					}
				}
				panelAttribute.appendChild(gridView);
			}
		}
	} // valueChange

	private void showBreadcumb(MDMSContent breadcumbContent)
	{
		Components.removeAllChildren(gridBreadCrumb);

		lblShowBreadCrumb = new Label(">");
		breadRow.appendChild(new Space());
		breadRow.appendChild(lblShowBreadCrumb);

		BreadCrumbLink breadCrumbLink = new BreadCrumbLink();
		breadCrumbLink.setPathId(String.valueOf(breadcumbContent.getDMS_Content_ID()));
		breadCrumbLink.addEventListener(Events.ON_CLICK, this);
		// breadCrumbLink.addEventListener(Events.ON_MOUSE_OVER, this);
		breadCrumbLink.setLabel(breadcumbContent.getName());
		breadCrumbLink.setStyle(DMSConstant.CSS_BREAD_CRUMB_LINK);

		breadRow.appendChild(breadCrumbLink);
		breadRows.appendChild(breadRow);
		gridBreadCrumb.appendChild(breadRows);
	} // showBreadcumb

	public List<BreadCrumbLink> getParentLinks()
	{
		List<BreadCrumbLink> parents = new ArrayList<BreadCrumbLink>();
		for (Component component : breadRow.getChildren())
		{
			if (component instanceof BreadCrumbLink)
				parents.add((BreadCrumbLink) component);
		}
		return parents;
	}

	// TODO Refactor method by passing table & record id
	public void addRootBreadCrumb()
	{
		BreadCrumbLink rootBreadCrumbLink = new BreadCrumbLink();

		rootBreadCrumbLink.setImageContent(Utils.getImage("Home24.png"));
		rootBreadCrumbLink.setPathId(String.valueOf(0));
		rootBreadCrumbLink.addEventListener(Events.ON_CLICK, this);
		rootBreadCrumbLink.setStyle(DMSConstant.CSS_BREAD_CRUMB_LINK);

		breadRow.appendChild(rootBreadCrumbLink);
		breadRow.appendChild(new Label());
		breadRows.appendChild(breadRow);
		gridBreadCrumb.appendChild(breadRows);
	} // addRootBreadCrumb

	/**
	 * Get Current Toggle Action
	 * 
	 * @return Toggle Action like Panel, List, etc
	 */
	public String getCurrToggleAction()
	{
		return currThumbViewerAction;
	}

	public Row getBreadRow()
	{
		return breadRow;
	}

}
