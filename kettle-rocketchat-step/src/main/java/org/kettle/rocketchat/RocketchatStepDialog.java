package org.kettle.rocketchat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaAndData;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.ui.core.widget.ColumnInfo;
import org.pentaho.di.ui.core.widget.ComboVar;
//import org.pentaho.di.ui.core.dialog.EnterValueDialog;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class RocketchatStepDialog extends BaseStepDialog implements StepDialogInterface {
    private RocketchatStepMeta input;
    private ValueMetaAndData url;
    private ValueMetaAndData user;
    private ValueMetaAndData password;
    private ValueMetaAndData channelField;
    private ValueMetaAndData messageField;
    private ValueMetaAndData advanced;
    private ValueMetaAndData aliasField;
    private ValueMetaAndData emojiField;
    private ValueMetaAndData statusFieldName;

    private Label wlUrl;
    private Text wUrl;
    private FormData fdlUrl, fdUrl;

    private Text wUser;
    private FormData fdlUser, fdUser;
    private Label wlUser;

    private Label wlPassword;
    private Text wPassword;
    private FormData fdlPassword, fdPassword;

    private Label wlChannelField;
    private ComboVar wChannelField;
    private FormData fdlChannelField, fdChannelField;

    private Label wlMessageField;
    private ComboVar wMessageField;
    private FormData fdlMessageField, fdMessageField;

    private Label wlAdvanced;
    private Button wAdvanced;
    private FormData fdlAdvanced, fdAdvanced;

    private Label wlAliasField;
    private ComboVar wAliasField;
    private FormData fdlAliasField, fdAliasField;

    private Label wlEmojiField;
    private ComboVar wEmojiField;
    private FormData fdlEmojiField, fdEmojiField;

    private Label wlStatusFieldName;
    private Text wStatusFieldName;
    private FormData fdlStatusFieldName, fdStatusFieldName;

    private boolean gotPreviousFields = false;
    private String[] fieldNames;
    private Map<String, Integer> inputFields;
    private ColumnInfo[] colinf, colinfoparams;

//    private Label wlValName;
//    private Text wValName;
//    private FormData fdlValName, fdValName;

//    private Label wlValue;
//    private Button wbValue;
//    private Text wValue;
//    private FormData fdlValue, fdbValue, fdValue;

    private static Class<?> PKG = RocketchatStepMeta.class; // for i18n purposes

    public RocketchatStepDialog(Shell parent, Object in, TransMeta transMeta, String sname) {
        super(parent, (BaseStepMeta) in, transMeta, sname);
        input = (RocketchatStepMeta) in;
        url = input.getUrl();
        user = input.getUser();
        password = input.getPassword();
        channelField = input.getChannelField();
        messageField = input.getMessageField();
        advanced = input.getAdvanced();
        aliasField = input.getAliasField();
        emojiField = input.getEmojiField();
        statusFieldName = input.getStatusFieldName();
        inputFields = new HashMap<String, Integer>();
    }

    @Override
    public String open() {
        Shell parent = getParent();
        Display display = parent.getDisplay();

        shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
        props.setLook(shell);
        setShellImage(shell, input);

        ModifyListener lsMod = new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                input.setChanged();
            }
        };
        changed = input.hasChanged();

        FormLayout formLayout = new FormLayout();
        formLayout.marginWidth = Const.FORM_MARGIN;
        formLayout.marginHeight = Const.FORM_MARGIN;

//        logError("DialogStep: " + BaseMessages.getString(PKG, "RocketChatPluginDialog.ValueName.Label"));

        shell.setLayout(formLayout);
        shell.setText(BaseMessages.getString(PKG, "RocketChatPluginDialog.Shell.Title")); //$NON-NLS-1$

        int middle = props.getMiddlePct();
        int margin = Const.MARGIN;

        // Stepname line
        wlStepname = new Label(shell, SWT.RIGHT);
        wlStepname.setText(BaseMessages.getString(PKG, "RocketChatPluginDialog.StepName.Label")); //$NON-NLS-1$
        props.setLook(wlStepname);
        fdlStepname = new FormData();
        fdlStepname.left = new FormAttachment(0, 0);
        fdlStepname.right = new FormAttachment(middle, -margin);
        fdlStepname.top = new FormAttachment(0, margin);
        wlStepname.setLayoutData(fdlStepname);
        wStepname = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
        wStepname.setText(stepname);
        props.setLook(wStepname);
        wStepname.addModifyListener(lsMod);
        fdStepname = new FormData();
        fdStepname.left = new FormAttachment(middle, 0);
        fdStepname.top = new FormAttachment(0, margin);
        fdStepname.right = new FormAttachment(100, 0);
        wStepname.setLayoutData(fdStepname);

        // URL Line
        wlUrl = new Label(shell, SWT.RIGHT);
        wlUrl.setText(BaseMessages.getString(PKG, "RocketChatPluginDialog.url.Label")); //$NON-NLS-1$
        props.setLook(wlUrl);
        fdlUrl = new FormData();
        fdlUrl.left = new FormAttachment(0, 0);
        fdlUrl.right = new FormAttachment(middle, -margin);
        fdlUrl.top = new FormAttachment(wStepname, 1 * margin);
        wlUrl.setLayoutData(fdlUrl);
        wUrl = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
        props.setLook(wUrl);
        wUrl.addModifyListener(lsMod);
        fdUrl = new FormData();
        fdUrl.left = new FormAttachment(middle, 0);
        fdUrl.right = new FormAttachment(100, 0);
        fdUrl.top = new FormAttachment(wStepname, margin);
        wUrl.setLayoutData(fdUrl);

        // Username Line
        wlUser = new Label(shell, SWT.RIGHT);
        wlUser.setText(BaseMessages.getString(PKG, "RocketChatPluginDialog.user.Label")); //$NON-NLS-1$
        props.setLook(wlUser);
        fdlUser = new FormData();
        fdlUser.left = new FormAttachment(0, 0);
        fdlUser.right = new FormAttachment(middle, -margin);
        fdlUser.top = new FormAttachment(wUrl, 1 * margin);
        wlUser.setLayoutData(fdlUser);
        wUser = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
        props.setLook(wUser);
        wUser.addModifyListener(lsMod);
        fdUser = new FormData();
        fdUser.left = new FormAttachment(middle, 0);
        fdUser.right = new FormAttachment(100, 0);
        fdUser.top = new FormAttachment(wUrl, margin);
        wUser.setLayoutData(fdUser);

        // Password line
        wlPassword = new Label(shell, SWT.RIGHT);
        wlPassword.setText(BaseMessages.getString(PKG, "RocketChatPluginDialog.password.Label")); //$NON-NLS-1$
        props.setLook(wlPassword);
        fdlPassword = new FormData();
        fdlPassword.left = new FormAttachment(0, 0);
        fdlPassword.right = new FormAttachment(middle, -margin);
        fdlPassword.top = new FormAttachment(wUser, 1 * margin);
        wlPassword.setLayoutData(fdlPassword);
        wPassword = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER | SWT.PASSWORD);
        props.setLook(wPassword);
        wPassword.addModifyListener(lsMod);
        fdPassword = new FormData();
        fdPassword.left = new FormAttachment(middle, 0);
        fdPassword.right = new FormAttachment(100, 0);
        fdPassword.top = new FormAttachment(wUser, margin);
        wPassword.setLayoutData(fdPassword);

        wlChannelField = new Label(shell, SWT.RIGHT);
        wlChannelField.setText(BaseMessages.getString(PKG, "RocketChatPluginDialog.ChannelField.Label"));
        props.setLook(wlChannelField);
        fdlChannelField = new FormData();
        fdlChannelField.left = new FormAttachment(0, 0);
        fdlChannelField.right = new FormAttachment(middle, -margin);
        fdlChannelField.top = new FormAttachment(wPassword, 1 * margin);
        wlChannelField.setLayoutData(fdlChannelField);
        wChannelField = new ComboVar(transMeta, shell, SWT.BORDER | SWT.READ_ONLY);
        props.setLook(wChannelField);
        wChannelField.addModifyListener(lsMod);
        wChannelField.setToolTipText(BaseMessages.getString(PKG, "RocketChatPluginDialog.ChannelField.Tooltip"));
        fdChannelField = new FormData();
        fdChannelField.left = new FormAttachment(middle, 0);
        fdChannelField.right = new FormAttachment(100, 0);
        fdChannelField.top = new FormAttachment(wPassword, margin);
        wChannelField.setLayoutData(fdChannelField);

        wChannelField.addFocusListener(new FocusListener() {
            public void focusLost(org.eclipse.swt.events.FocusEvent e) {
            }

            public void focusGained(org.eclipse.swt.events.FocusEvent e) {
                Cursor busy = new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT);
                shell.setCursor(busy);
                setStreamFields();
                shell.setCursor(null);
                busy.dispose();
            }
        });

        // Message Line

        wlMessageField = new Label(shell, SWT.RIGHT);
        wlMessageField.setText(BaseMessages.getString(PKG, "RocketChatPluginDialog.MessageField.Label"));
        props.setLook(wlMessageField);
        fdlMessageField = new FormData();
        fdlMessageField.left = new FormAttachment(0, 0);
        fdlMessageField.right = new FormAttachment(middle, -margin);
        fdlMessageField.top = new FormAttachment(wChannelField, 1 * margin);
        wlMessageField.setLayoutData(fdlMessageField);
        wMessageField = new ComboVar(transMeta, shell, SWT.BORDER | SWT.READ_ONLY);
        props.setLook(wMessageField);
        wMessageField.addModifyListener(lsMod);
        fdMessageField = new FormData();
        fdMessageField.left = new FormAttachment(middle, 0);
        fdMessageField.right = new FormAttachment(100, 0);
        fdMessageField.top = new FormAttachment(wChannelField, margin);
        wMessageField.setLayoutData(fdMessageField);

        wMessageField.addFocusListener(new FocusListener() {
            public void focusLost(org.eclipse.swt.events.FocusEvent e) {
            }

            public void focusGained(org.eclipse.swt.events.FocusEvent e) {
                Cursor busy = new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT);
                shell.setCursor(busy);
                setStreamFields();
                shell.setCursor(null);
                busy.dispose();
            }
        });

        // Activate extended options
        wlAdvanced = new Label(shell, SWT.RIGHT);
        wlAdvanced.setText(BaseMessages.getString(PKG, "RocketChatPluginDialog.Advanced.Label"));
        props.setLook(wlAdvanced);
        fdlAdvanced = new FormData();
        fdlAdvanced.left = new FormAttachment(0, 0);
        fdlAdvanced.right = new FormAttachment(middle, -margin);
        fdlAdvanced.top = new FormAttachment(wMessageField, 1 * margin);
        wlAdvanced.setLayoutData(fdlAdvanced);
        wAdvanced = new Button(shell, SWT.CHECK);
        props.setLook(wAdvanced);
        fdAdvanced = new FormData();
        fdAdvanced.left = new FormAttachment(middle, 0);
        fdAdvanced.right = new FormAttachment(100, 0);
        fdAdvanced.top = new FormAttachment(wMessageField, margin);
        wAdvanced.setLayoutData(fdAdvanced);
        wAdvanced.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                input.setChanged();
                activeAdvanced();
            }
        });

        // Alias line

        wlAliasField = new Label(shell, SWT.RIGHT);
        wlAliasField.setText(BaseMessages.getString(PKG, "RocketChatPluginDialog.AliasField.Label"));
        props.setLook(wlAliasField);
        fdlAliasField = new FormData();
        fdlAliasField.left = new FormAttachment(0, 0);
        fdlAliasField.right = new FormAttachment(middle, -margin);
        fdlAliasField.top = new FormAttachment(wAdvanced, 1 * margin);
        wlAliasField.setLayoutData(fdlAliasField);
        wAliasField = new ComboVar(transMeta, shell, SWT.BORDER | SWT.READ_ONLY);
        props.setLook(wAliasField);
        wAliasField.addModifyListener(lsMod);
        wAliasField.setToolTipText(BaseMessages.getString(PKG, "RocketChatPluginDialog.AliasField.Tooltip"));
        fdAliasField = new FormData();
        fdAliasField.left = new FormAttachment(middle, 0);
        fdAliasField.right = new FormAttachment(100, 0);
        fdAliasField.top = new FormAttachment(wAdvanced, margin);
        wAliasField.setLayoutData(fdAliasField);

        wAliasField.addFocusListener(new FocusListener() {
            public void focusLost(org.eclipse.swt.events.FocusEvent e) {
            }

            public void focusGained(org.eclipse.swt.events.FocusEvent e) {
                Cursor busy = new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT);
                shell.setCursor(busy);
                setStreamFields();
                shell.setCursor(null);
                busy.dispose();
            }
        });

        // Avatar line

        wlEmojiField = new Label(shell, SWT.RIGHT);
        wlEmojiField.setText(BaseMessages.getString(PKG, "RocketChatPluginDialog.EmojiField.Label"));
        props.setLook(wlEmojiField);
        fdlEmojiField = new FormData();
        fdlEmojiField.left = new FormAttachment(0, 0);
        fdlEmojiField.right = new FormAttachment(middle, -margin);
        fdlEmojiField.top = new FormAttachment(wAliasField, 1 * margin);
        wlEmojiField.setLayoutData(fdlEmojiField);
        wEmojiField = new ComboVar(transMeta, shell, SWT.BORDER | SWT.READ_ONLY);
        props.setLook(wEmojiField);
        wEmojiField.addModifyListener(lsMod);
        wEmojiField.setToolTipText(BaseMessages.getString(PKG, "RocketChatPluginDialog.EmojiField.Tooltip"));
        fdEmojiField = new FormData();
        fdEmojiField.left = new FormAttachment(middle, 0);
        fdEmojiField.right = new FormAttachment(100, 0);
        fdEmojiField.top = new FormAttachment(wAliasField, margin);
        wEmojiField.setLayoutData(fdEmojiField);

        wEmojiField.addFocusListener(new FocusListener() {
            public void focusLost(org.eclipse.swt.events.FocusEvent e) {
            }

            public void focusGained(org.eclipse.swt.events.FocusEvent e) {
                Cursor busy = new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT);
                shell.setCursor(busy);
                setStreamFields();
                shell.setCursor(null);
                busy.dispose();
            }
        });

        wlStatusFieldName = new Label(shell, SWT.RIGHT);
        wlStatusFieldName.setText(BaseMessages.getString(PKG, "RocketChatPluginDialog.fieldName.Label")); //$NON-NLS-1$
        props.setLook(wlStatusFieldName);
        fdlStatusFieldName = new FormData();
        fdlStatusFieldName.left = new FormAttachment(0, 0);
        fdlStatusFieldName.right = new FormAttachment(middle, -margin);
        fdlStatusFieldName.top = new FormAttachment(wEmojiField, 1 * margin);
        wlStatusFieldName.setLayoutData(fdlStatusFieldName);
        wStatusFieldName = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
        props.setLook(wStatusFieldName);
        wStatusFieldName.addModifyListener(lsMod);
        fdStatusFieldName = new FormData();
        fdStatusFieldName.left = new FormAttachment(middle, 0);
        fdStatusFieldName.right = new FormAttachment(100, 0);
        fdStatusFieldName.top = new FormAttachment(wEmojiField, margin);
        wStatusFieldName.setLayoutData(fdStatusFieldName);

        final Runnable runnable = new Runnable() {
            public void run() {
                StepMeta stepMeta = transMeta.findStep(stepname);
                if (stepMeta != null) {
                    try {
                        RowMetaInterface row = transMeta.getPrevStepFields(stepMeta);

                        // Remember these fields...
                        for (int i = 0; i < row.size(); i++) {
                            inputFields.put(row.getValueMeta(i).getName(), Integer.valueOf(i));
                        }

                        setComboBoxes();
                    } catch (KettleException e) {
                        log.logError(toString(), BaseMessages.getString(PKG, "System.Dialog.GetFieldsFailed.Message"));
                    }
                }
            }
        };
        new Thread(runnable).start();
        colinf = new ColumnInfo[] {
                new ColumnInfo(BaseMessages.getString(PKG, "RestDialog.ColumnInfo.Field"),
                        ColumnInfo.COLUMN_TYPE_CCOMBO, new String[] { "" }, false),
                new ColumnInfo(BaseMessages.getString(PKG, "RestDialog.ColumnInfo.Name"), ColumnInfo.COLUMN_TYPE_TEXT,
                        false) };

        // OK / Cancel button
        wOK = new Button(shell, SWT.PUSH);
        wOK.setText(BaseMessages.getString(PKG, "System.Button.OK")); //$NON-NLS-1$
        wCancel = new Button(shell, SWT.PUSH);
        wCancel.setText(BaseMessages.getString(PKG, "System.Button.Cancel")); //$NON-NLS-1$

        BaseStepDialog.positionBottomButtons(shell, new Button[] { wOK, wCancel }, margin, wStatusFieldName);

        // Add listeners
        lsCancel = new Listener() {
            @Override
            public void handleEvent(Event e) {
                cancel();
            }
        };
        lsOK = new Listener() {
            @Override
            public void handleEvent(Event e) {
                ok();
            }
        };

        wCancel.addListener(SWT.Selection, lsCancel);
        wOK.addListener(SWT.Selection, lsOK);

        lsDef = new SelectionAdapter() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                ok();
            }
        };

        wStepname.addSelectionListener(lsDef);
        wUrl.addSelectionListener(lsDef);

        // Detect X or ALT-F4 or something that kills this window...
        shell.addShellListener(new ShellAdapter() {
            @Override
            public void shellClosed(ShellEvent e) {
                cancel();
            }
        });

        // Set the shell size, based upon previous time...
        setSize();

        getData();
        input.setChanged(changed);

        wAdvanced.setSelection((boolean) advanced.getValueData());
        activeAdvanced();

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return stepname;
    }

    public void getData() {
        wStepname.selectAll();
        if (url != null) {
            wUrl.setText(url.getValueData().toString());
        }
        if (user != null) {
            wUser.setText(user.getValueData().toString());
        }
        if (password != null) {
            wPassword.setText(password.getValueData().toString());
        }
        if (channelField != null) {
            wChannelField.setText(channelField.getValueData().toString());
        }

        if (messageField != null) {
            wMessageField.setText(messageField.getValueData().toString());
        }

        if (advanced != null) {
            wAdvanced.setSelection((boolean) advanced.getValueData()); // Difference!
        }

        if (aliasField != null) {
            wAliasField.setText(aliasField.getValueData().toString());
        }

        if (emojiField != null) {
            wEmojiField.setText(emojiField.getValueData().toString());
        }
        if (statusFieldName != null) {
            wStatusFieldName.setText(statusFieldName.getValueData().toString());
        }
    }

    private void cancel() {
        stepname = null;
        input.setChanged(changed);
        dispose();
    }

    private void ok() {
        stepname = wStepname.getText();
        url.setValueData(wUrl.getText());
        user.setValueData(wUser.getText());
        password.setValueData(wPassword.getText());
        channelField.setValueData(wChannelField.getText());
        messageField.setValueData(wMessageField.getText());
        advanced.setValueData(wAdvanced.getSelection());
        aliasField.setValueData(wAliasField.getText());
        emojiField.setValueData(wEmojiField.getText());
        statusFieldName.setValueData(wStatusFieldName.getText());
//        urlValue.getValueMeta().setName( wUrl.getText() );
//        userValue.getValueMeta().setName( wUser.getText() );
//        passwordValue.getValueMeta().setName( wPassword.getText() );

        input.setUrl(url);
        input.setUser(user);
        input.setPassword(password);
        input.setChannelField(channelField);
        input.setMessageField(messageField);
        input.setAdvanced(advanced);
        input.setAliasField(aliasField);
        input.setEmojiField(emojiField);
        input.setStatusFieldName(statusFieldName);
        dispose();
    }

    protected void setComboBoxes() {
        // Something was changed in the row.
        //
        final Map<String, Integer> fields = new HashMap<String, Integer>();

        // Add the currentMeta fields...
        fields.putAll(inputFields);

        Set<String> keySet = fields.keySet();
        List<String> entries = new ArrayList<String>(keySet);

        fieldNames = entries.toArray(new String[entries.size()]);

        Const.sortStrings(fieldNames);
        colinfoparams[0].setComboValues(fieldNames);
        colinf[0].setComboValues(fieldNames);
    }

    private void setStreamFields() {
        if (!gotPreviousFields) {
            String channelfield = wChannelField.getText();
            String messagefield = wMessageField.getText();
            String aliasfield = wAliasField.getText();
            String emojifield = wEmojiField.getText();

            wChannelField.removeAll();
            wMessageField.removeAll();
            wAliasField.removeAll();
            wEmojiField.removeAll();

            try {
                if (fieldNames != null) {
                    wChannelField.setItems(fieldNames);
                    wMessageField.setItems(fieldNames);
                    wAliasField.setItems(fieldNames);
                    wEmojiField.setItems(fieldNames);
                } else {
                    logError("fieldnames was null!");
                }
            } finally {
                if (channelfield != null) {
                    wChannelField.setText(channelfield);
                }
                if (channelfield != null) {
                    wMessageField.setText(messagefield);
                }
                if (aliasfield != null) {
                    wAliasField.setText(aliasfield);
                }
                if (aliasfield != null) {
                    wEmojiField.setText(emojifield);
                }
            }
            gotPreviousFields = true;
        }
    }

    private void activeAdvanced() {
        wlAliasField.setEnabled(wAdvanced.getSelection());
        wAliasField.setEnabled(wAdvanced.getSelection());
        wlEmojiField.setEnabled(wAdvanced.getSelection());
        wEmojiField.setEnabled(wAdvanced.getSelection());
    }
}