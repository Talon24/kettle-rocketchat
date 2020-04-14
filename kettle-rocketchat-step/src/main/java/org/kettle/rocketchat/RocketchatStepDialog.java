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

    private Label wlUrl;
    private Text wUrl;
    private FormData fdlUrl, fdUrl;
    
    private Text wUser;
    private FormData fdluser, fdUser;
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


    public RocketchatStepDialog(Shell parent, Object in, TransMeta transMeta, String sname ) {
        super( parent, (BaseStepMeta) in, transMeta, sname );
        input = (RocketchatStepMeta) in;
        url = input.getUrl();
        user = input.getUser();
        password = input.getPassword();
        channelField = input.getChannelField();
        messageField = input.getMessageField();
        advanced = input.getAdvanced();
        aliasField = input.getAliasField();
        emojiField = input.getEmojiField();
        inputFields = new HashMap<String, Integer>();
    }

    @Override
    public String open() {
        Shell parent = getParent();
        Display display = parent.getDisplay();

        shell = new Shell( parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX );
        props.setLook( shell );
        setShellImage( shell, input );

        ModifyListener lsMod = new ModifyListener() {
            @Override
            public void modifyText( ModifyEvent e ) {
                input.setChanged();
            }
        };
        changed = input.hasChanged();

        FormLayout formLayout = new FormLayout();
        formLayout.marginWidth  = Const.FORM_MARGIN;
        formLayout.marginHeight = Const.FORM_MARGIN;
        
//        logError("DialogStep: " + BaseMessages.getString(PKG, "RocketChatPluginDialog.ValueName.Label"));

        shell.setLayout( formLayout );
        shell.setText( BaseMessages.getString(PKG, "RocketChatPluginDialog.Shell.Title" ) ); //$NON-NLS-1$

        int middle = props.getMiddlePct();
        int margin = Const.MARGIN;

        // Stepname line
        wlStepname = new Label( shell, SWT.RIGHT );
        wlStepname.setText( BaseMessages.getString(PKG, "RocketChatPluginDialog.StepName.Label" ) ); //$NON-NLS-1$
        props.setLook( wlStepname );
        fdlStepname = new FormData();
        fdlStepname.left = new FormAttachment( 0, 0 );
        fdlStepname.right = new FormAttachment( middle, -margin );
        fdlStepname.top  = new FormAttachment( 0, margin );
        wlStepname.setLayoutData( fdlStepname );
        wStepname = new Text( shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
        wStepname.setText( stepname );
        props.setLook( wStepname );
        wStepname.addModifyListener( lsMod );
        fdStepname = new FormData();
        fdStepname.left = new FormAttachment( middle, 0 );
        fdStepname.top = new FormAttachment( 0, margin );
        fdStepname.right = new FormAttachment( 100, 0 );
        wStepname.setLayoutData( fdStepname );
        
        
        // URL Line
        wlUrl = new Label( shell, SWT.RIGHT );
        wlUrl.setText( BaseMessages.getString(PKG, "RocketChatPluginDialog.url.Label" ) ); //$NON-NLS-1$
        props.setLook( wlUrl );
        fdlUrl = new FormData();
        fdlUrl.left = new FormAttachment( 0, 0 );
        fdlUrl.right = new FormAttachment( middle, -margin );
        fdlUrl.top = new FormAttachment( wStepname, 2*margin );
        wlUrl.setLayoutData( fdlUrl );
        wUrl = new Text( shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
        props.setLook( wUrl );
        wUrl.addModifyListener( lsMod );
        fdUrl = new FormData();
        fdUrl.left = new FormAttachment( middle, 0 );
        fdUrl.right = new FormAttachment( 100, 0 );
        fdUrl.top  = new FormAttachment( wStepname, margin );
        wUrl.setLayoutData( fdUrl );


      // Username Line
      wlUser = new Label( shell, SWT.RIGHT );
      wlUser.setText( BaseMessages.getString(PKG, "RocketChatPluginDialog.user.Label" ) ); //$NON-NLS-1$
      props.setLook( wlUser );
      fdluser = new FormData();
      fdluser.left = new FormAttachment( 0, 0 );
      fdluser.right = new FormAttachment( middle, -margin );
      fdluser.top = new FormAttachment( wlUrl, 2*margin );
      wlUser.setLayoutData( fdluser );
      wUser = new Text( shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
      props.setLook( wUser );
      wUser.addModifyListener( lsMod );
      fdUser = new FormData();
      fdUser.left = new FormAttachment( middle, 0 );
      fdUser.right = new FormAttachment( 100, 0 );
      fdUser.top  = new FormAttachment( wUrl, margin );
      wUser.setLayoutData( fdUser );

      // Password line
      wlPassword = new Label( shell, SWT.RIGHT );
      wlPassword.setText( BaseMessages.getString(PKG, "RocketChatPluginDialog.password.Label" ) ); //$NON-NLS-1$
      props.setLook( wlPassword );
      fdlPassword = new FormData();
      fdlPassword.left = new FormAttachment( 0, 0 );
      fdlPassword.right = new FormAttachment( middle, -margin );
      fdlPassword.top = new FormAttachment( wlUser, 2*margin );
      wlPassword.setLayoutData( fdlPassword );
      wPassword = new Text( shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
      props.setLook( wPassword );
      wPassword.addModifyListener( lsMod );
      fdPassword = new FormData();
      fdPassword.left = new FormAttachment( middle, 0 );
      fdPassword.right = new FormAttachment( 100, 0 );
      fdPassword.top  = new FormAttachment( wUser, margin );
      wPassword.setLayoutData( fdPassword );
      
      wlChannelField = new Label (shell, SWT.RIGHT);
      wlChannelField.setText(BaseMessages.getString(PKG, "RocketChatPluginDialog.ChannelField.Label"));
      props.setLook(wlChannelField);
      fdlChannelField = new FormData();
      fdlChannelField.left = new FormAttachment( 0, 0 );
      fdlChannelField.right = new FormAttachment( middle, -margin );
      fdlChannelField.top = new FormAttachment( wlPassword, 2*margin );
      wlChannelField.setLayoutData( fdlChannelField );
      wChannelField = new ComboVar( transMeta, shell, SWT.BORDER | SWT.READ_ONLY );
      props.setLook( wChannelField );
      wChannelField.addModifyListener( lsMod );
      fdChannelField = new FormData();
      fdChannelField.left = new FormAttachment( middle, 0 );
      fdChannelField.right = new FormAttachment( 100, 0 );
      fdChannelField.top  = new FormAttachment( wPassword, margin );
      wChannelField.setLayoutData( fdChannelField );
      
      wChannelField.addFocusListener( new FocusListener() {
          public void focusLost( org.eclipse.swt.events.FocusEvent e ) {
          }

          public void focusGained( org.eclipse.swt.events.FocusEvent e ) {
            Cursor busy = new Cursor( shell.getDisplay(), SWT.CURSOR_WAIT );
            shell.setCursor( busy );
            setStreamFields();
            shell.setCursor( null );
            busy.dispose();
          }
        } );
      
      final Runnable runnable = new Runnable() {
          public void run() {
            StepMeta stepMeta = transMeta.findStep( stepname );
            if ( stepMeta != null ) {
              try {
                RowMetaInterface row = transMeta.getPrevStepFields( stepMeta );

                // Remember these fields...
                for ( int i = 0; i < row.size(); i++ ) {
                  inputFields.put( row.getValueMeta( i ).getName(), Integer.valueOf( i ) );
                }

                setComboBoxes();
              } catch ( KettleException e ) {
                log.logError( toString(), BaseMessages.getString( PKG, "System.Dialog.GetFieldsFailed.Message" ) );
              }
            }
          }
        };
        new Thread( runnable ).start();
        colinf =
	      new ColumnInfo[] {
	        new ColumnInfo(
	          BaseMessages.getString( PKG, "RestDialog.ColumnInfo.Field" ), ColumnInfo.COLUMN_TYPE_CCOMBO,
	          new String[] { "" }, false ),
	        new ColumnInfo(
	          BaseMessages.getString( PKG, "RestDialog.ColumnInfo.Name" ), ColumnInfo.COLUMN_TYPE_TEXT, false ) };
        
        
        // Message Line
        
        wlMessageField = new Label (shell, SWT.RIGHT);
        wlMessageField.setText(BaseMessages.getString(PKG, "RocketChatPluginDialog.MessageField.Label"));
        props.setLook(wlMessageField);
        fdlMessageField = new FormData();
        fdlMessageField.left = new FormAttachment( 0, 0 );
        fdlMessageField.right = new FormAttachment( middle, -margin );
        fdlMessageField.top = new FormAttachment( wlChannelField, 2*margin );
        wlMessageField.setLayoutData( fdlMessageField );
        wMessageField = new ComboVar( transMeta, shell, SWT.BORDER | SWT.READ_ONLY );
        props.setLook( wMessageField );
        wMessageField.addModifyListener( lsMod );
        fdMessageField = new FormData();
        fdMessageField.left = new FormAttachment( middle, 0 );
        fdMessageField.right = new FormAttachment( 100, 0 );
        fdMessageField.top  = new FormAttachment( wChannelField, margin );
        wMessageField.setLayoutData( fdMessageField );
        
        wMessageField.addFocusListener( new FocusListener() {
            public void focusLost( org.eclipse.swt.events.FocusEvent e ) {
            }

            public void focusGained( org.eclipse.swt.events.FocusEvent e ) {
              Cursor busy = new Cursor( shell.getDisplay(), SWT.CURSOR_WAIT );
              shell.setCursor( busy );
              setStreamFields();
              shell.setCursor( null );
              busy.dispose();
            }
          } );
        
        
        // Activate extended options
        wlAdvanced = new Label( shell, SWT.RIGHT );
        wlAdvanced.setText( BaseMessages.getString( PKG, "RocketChatPluginDialog.Advanced.Label" ) );
        props.setLook( wlAdvanced );
        fdlAdvanced = new FormData();
        fdlAdvanced.left = new FormAttachment( 0, 0 );
        fdlAdvanced.right = new FormAttachment( middle, -margin );
        fdlAdvanced.top = new FormAttachment( wlMessageField, 2*margin );
        wlAdvanced.setLayoutData( fdlAdvanced );
        wAdvanced = new Button( shell, SWT.CHECK );
        props.setLook( wAdvanced );
        fdAdvanced = new FormData();
        fdAdvanced.left = new FormAttachment( middle, 0 );
        fdAdvanced.right = new FormAttachment( 100, 0 );
        fdAdvanced.top = new FormAttachment( wMessageField, margin );
        wAdvanced.setLayoutData( fdAdvanced );
        wAdvanced.addSelectionListener( new SelectionAdapter() {
          public void widgetSelected( SelectionEvent e ) {
            input.setChanged();
            activeAdvanced();
          }
        } );
        // Extended fields
        
        
        wlAliasField = new Label (shell, SWT.RIGHT);
        wlAliasField.setText(BaseMessages.getString(PKG, "RocketChatPluginDialog.AliasField.Label"));
        props.setLook(wlAliasField);
        fdlAliasField = new FormData();
        fdlAliasField.left = new FormAttachment( 0, 0 );
        fdlAliasField.right = new FormAttachment( middle, -margin );
        fdlAliasField.top = new FormAttachment( wlAdvanced, 2*margin );
        wlAliasField.setLayoutData( fdlAliasField );
        wAliasField = new ComboVar( transMeta, shell, SWT.BORDER | SWT.READ_ONLY );
        props.setLook( wAliasField );
        wChannelField.addModifyListener( lsMod );
        fdAliasField = new FormData();
        fdAliasField.left = new FormAttachment( middle, 0 );
        fdAliasField.right = new FormAttachment( 100, 0 );
        fdAliasField.top  = new FormAttachment( wAdvanced, margin );
        wAliasField.setLayoutData( fdAliasField );
        
        wAliasField.addFocusListener( new FocusListener() {
            public void focusLost( org.eclipse.swt.events.FocusEvent e ) {
            }

            public void focusGained( org.eclipse.swt.events.FocusEvent e ) {
              Cursor busy = new Cursor( shell.getDisplay(), SWT.CURSOR_WAIT );
              shell.setCursor( busy );
              setStreamFields();
              shell.setCursor( null );
              busy.dispose();
            }
          } );
        
        wlEmojiField = new Label (shell, SWT.RIGHT);
        wlEmojiField.setText(BaseMessages.getString(PKG, "RocketChatPluginDialog.EmojiField.Label"));
        props.setLook(wlEmojiField);
        fdlEmojiField = new FormData();
        fdlEmojiField.left = new FormAttachment( 0, 0 );
        fdlEmojiField.right = new FormAttachment( middle, -margin );
        fdlEmojiField.top = new FormAttachment( wlAliasField, 2*margin );
        wlEmojiField.setLayoutData( fdlEmojiField );
        wEmojiField = new ComboVar( transMeta, shell, SWT.BORDER | SWT.READ_ONLY );
        props.setLook( wEmojiField );
        wChannelField.addModifyListener( lsMod );
        fdEmojiField = new FormData();
        fdEmojiField.left = new FormAttachment( middle, 0 );
        fdEmojiField.right = new FormAttachment( 100, 0 );
        fdEmojiField.top  = new FormAttachment( wAliasField, margin );
        wEmojiField.setLayoutData( fdEmojiField );
        
        wEmojiField.addFocusListener( new FocusListener() {
            public void focusLost( org.eclipse.swt.events.FocusEvent e ) {
            }

            public void focusGained( org.eclipse.swt.events.FocusEvent e ) {
              Cursor busy = new Cursor( shell.getDisplay(), SWT.CURSOR_WAIT );
              shell.setCursor( busy );
              setStreamFields();
              shell.setCursor( null );
              busy.dispose();
            }
          } );
      
      
      
//        // ValName line
//        wlValName = new Label( shell, SWT.RIGHT );
//        wlValName.setText( BaseMessages.getString(PKG, "RocketChatPluginDialog.ValueName.Label" ) ); //$NON-NLS-1$
//        props.setLook( wlValName );
//        fdlValName = new FormData();
//        fdlValName.left = new FormAttachment( 0, 0 );
//        fdlValName.right = new FormAttachment( middle, -margin );
//        fdlValName.top = new FormAttachment( wStepname, margin );
//        wlValName.setLayoutData( fdlValName );
//        wValName = new Text( shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
//        props.setLook( wValName );
//        wValName.addModifyListener( lsMod );
//        fdValName = new FormData();
//        fdValName.left = new FormAttachment( middle, 0 );
//        fdValName.right = new FormAttachment( 100, 0 );
//        fdValName.top  = new FormAttachment( wStepname, margin );
//        wValName.setLayoutData( fdValName );

        // Value line
//        wlValue = new Label( shell, SWT.RIGHT );
//        wlValue.setText( BaseMessages.getString(PKG, "RocketChatPluginDialog.ValueToAdd.Label" ) ); //$NON-NLS-1$
//        props.setLook( wlValue );
//        fdlValue = new FormData();
//        fdlValue.left = new FormAttachment( 0, 0 );
//        fdlValue.right = new FormAttachment( middle, -margin );
//        fdlValue.top = new FormAttachment( wValName, margin );
//        wlValue.setLayoutData( fdlValue );
//
//        wbValue = new Button( shell, SWT.PUSH | SWT.CENTER );
//        props.setLook( wbValue );
//        wbValue.setText( BaseMessages.getString(PKG, "System.Button.Edit" ) ); //$NON-NLS-1$
//        fdbValue = new FormData();
//        fdbValue.right = new FormAttachment( 100, 0 );
//        fdbValue.top  = new FormAttachment( wValName, margin );
//        wbValue.setLayoutData( fdbValue );
//
//        wValue = new Text( shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
//        props.setLook( wValue );
//        wValue.addModifyListener( lsMod );
//        fdValue = new FormData();
//        fdValue.left = new FormAttachment( middle, 0 );
//        fdValue.right = new FormAttachment( wbValue, -margin );
//        fdValue.top  = new FormAttachment( wValName, margin );
//        wValue.setLayoutData( fdValue );
//
//        wbValue.addSelectionListener( new SelectionAdapter() {
//            @Override
//            public void widgetSelected( SelectionEvent arg0 ) {
//                ValueMetaAndData v = (ValueMetaAndData) value.clone();
//                EnterValueDialog evd = new EnterValueDialog( shell, SWT.NONE, v.getValueMeta(), v.getValueData() );
//                ValueMetaAndData newval = evd.open();
//                if ( newval != null ) {
//                    value = newval;
//                    getData();
//                }
//            }
//        } );

        // Some buttons
        wOK = new Button( shell, SWT.PUSH );
        wOK.setText( BaseMessages.getString(PKG, "System.Button.OK" ) ); //$NON-NLS-1$
        wCancel = new Button( shell, SWT.PUSH );
        wCancel.setText( BaseMessages.getString(PKG, "System.Button.Cancel" ) ); //$NON-NLS-1$

//        BaseStepDialog.positionBottomButtons( shell, new Button[] { wOK, wCancel }, margin, wValue );
        BaseStepDialog.positionBottomButtons( shell, new Button[] { wOK, wCancel }, margin, wEmojiField);

        // Add listeners
        lsCancel = new Listener() {
            @Override
            public void handleEvent( Event e ) {
                cancel();
            }
        };
        lsOK = new Listener() {
            @Override
            public void handleEvent( Event e ) {
                ok();
            }
        };

        wCancel.addListener( SWT.Selection, lsCancel );
        wOK.addListener( SWT.Selection, lsOK );

        lsDef = new SelectionAdapter() {
            @Override
            public void widgetDefaultSelected( SelectionEvent e ) {
                ok();
            }
        };

        wStepname.addSelectionListener( lsDef );
        wUrl.addSelectionListener( lsDef );

        // Detect X or ALT-F4 or something that kills this window...
        shell.addShellListener(  new ShellAdapter() {
            @Override
            public void shellClosed( ShellEvent e ) {
                cancel();
            }
        } );

        // Set the shell size, based upon previous time...
        setSize();

        getData();
        input.setChanged( changed );
        
        wAdvanced.setSelection((boolean) advanced.getValueData());
        activeAdvanced();

        shell.open();
        while ( !shell.isDisposed() ) {
            if ( !display.readAndDispatch() ) {
                display.sleep();
            }
        }
        return stepname;
    }

    // Read data from input (TextFileInputInfo)
    public void getData() {
        wStepname.selectAll();
//        logError("url: " + urlValue);
//        logError("url: " + urlValue.toString());
//        logError("url: " + urlValue.getValueData());
        if ( url != null ) {
            wUrl.setText( url.getValueData().toString());
//            wValue.setText( value.toString() + " (" + value.toStringMeta() + ")" ); //$NON-NLS-1$ //$NON-NLS-2$
        }
//        logError("username: " + userValue);
//        logError("username: " + userValue.toString());
//        logError("username: " + userValue.getValueData());
        if ( user != null ) {
            wUser.setText( user.getValueData().toString() );
        }
//        logError("pass: " + passwordValue);
//        logError("pass: " + passwordValue.toString());
//        logError("pass: " + passwordValue.getValueData());
        if ( password != null ) {
            wPassword.setText( password.getValueData().toString());
        }
        if ( channelField != null ) {
            wChannelField.setText( channelField.getValueData().toString());
        }
        
        if ( messageField != null ) {
            wMessageField.setText( messageField.getValueData().toString());
        }
        
        if ( advanced!= null ) {
            wAdvanced.setText( advanced.getValueData().toString());
        }
        
        if ( aliasField != null ) {
            wAliasField.setText( aliasField.getValueData().toString());
        }
        
        if ( emojiField != null ) {
            wEmojiField.setText( emojiField.getValueData().toString());
        }
    }

    private void cancel() {
        stepname = null;
        input.setChanged(changed);
        dispose();
    }

    private void ok() {
        stepname = wStepname.getText(); // return value
//        logBasic("ok clicked url: " + wUrl.getText());
//        logBasic("ok clicked usr: " + wUser.getText());
//        logBasic("ok clicked pwd: " + wPassword.getText());
        url.setValueData(wUrl.getText());
        user.setValueData(wUser.getText());
        password.setValueData(wPassword.getText());
        channelField.setValueData(wChannelField.getText());
        messageField.setValueData(wMessageField.getText());
        advanced.setValueData(wAdvanced.getSelection());
    	aliasField.setValueData(wAliasField.getText());
        emojiField.setValueData(wEmojiField.getText());        	
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
        dispose();
    }
    
    protected void setComboBoxes() {
        // Something was changed in the row.
        //
        final Map<String, Integer> fields = new HashMap<String, Integer>();

        // Add the currentMeta fields...
        fields.putAll( inputFields );

        Set<String> keySet = fields.keySet();
        List<String> entries = new ArrayList<String>( keySet );

        fieldNames = entries.toArray( new String[entries.size()] );

        Const.sortStrings( fieldNames );
        colinfoparams[0].setComboValues( fieldNames );
        colinf[0].setComboValues( fieldNames );
      }
    
    private void setStreamFields() {
	    if ( !gotPreviousFields ) {
	      String channelfield = wChannelField.getText();
	      String messagefield= wEmojiField.getText();
	      String aliasfield = wAliasField.getText();
	      String emojifield = wEmojiField.getText();

	      wChannelField.removeAll();
	      wMessageField.removeAll();
	      wAliasField.removeAll();
	      wEmojiField.removeAll();

	      try {
	        if ( fieldNames != null ) {
	        	wChannelField.setItems( fieldNames );
	        	wMessageField.setItems( fieldNames );
	        	wAliasField.setItems( fieldNames );
	        	wEmojiField.setItems( fieldNames );
	        } else {
	        	logError("fieldnames was null!");
	        }
	      } finally {
	        if ( channelfield != null ) {
	          wChannelField.setText( channelfield );
	        }
	        if ( channelfield != null ) {
	          wMessageField.setText( messagefield );
	        }
	        if ( aliasfield != null ) {
	          wAliasField.setText( aliasfield );
	        }
	        if ( aliasfield != null ) {
	          wEmojiField.setText( emojifield);
	        }
	      }
	      gotPreviousFields = true;
	    }
    }
    
    private void activeAdvanced() {
        wlAliasField.setEnabled( wAdvanced.getSelection() );
        wAliasField.setEnabled( wAdvanced.getSelection() );
        wlEmojiField.setEnabled( wAdvanced.getSelection() );
        wEmojiField.setEnabled( wAdvanced.getSelection() );
      }
}