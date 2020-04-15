package org.kettle.rocketchat;

import org.eclipse.swt.widgets.*;
import org.pentaho.di.core.*;
import org.pentaho.di.core.annotations.*;
import org.pentaho.di.core.database.*;
import org.pentaho.di.core.encryption.Encr;
import org.pentaho.di.core.exception.*;
import org.pentaho.di.core.row.*;
import org.pentaho.di.core.row.value.*;
import org.pentaho.di.core.variables.*;
import org.pentaho.di.core.xml.*;
import org.pentaho.di.repository.*;
import org.pentaho.di.trans.*;
import org.pentaho.di.trans.step.*;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.*;

import java.util.List;

import java.io.IOException;
import java.net.MalformedURLException;
//import java.util.*;

/*
 * Created on 02-jun-2003
 * Modified on 2020-04-09
 *
 */

// @formatter:off
@Step(
        id = "RocketChatSender",
        image = "plugins/rocketchat-plugin/rocket.svg",
        i18nPackageName = "be.ibridge.kettle.rocket.chat",
        name = "RocketChat.Step.Name",
        description = "RocketChat.Step.Description",
        categoryDescription = "i18n:org.pentaho.di.trans.step:BaseStep.Category.Utility",
        documentationUrl = "https://github.com/Talon24/kettle-rocketchat",
        casesUrl = "https://github.com/Talon24/kettle-rocketchat/issues"
//        image = "rocket.svg",
//        image = "ui/images/rocket.svg",
//        suggestion = "DummyStep.Step.SuggestedStep"
        )
// @formatter:on
public class RocketchatStepMeta extends BaseStepMeta implements StepMetaInterface {
    private ValueMetaAndData url;
    private ValueMetaAndData user;
    private ValueMetaAndData password;
    private ValueMetaAndData messageField;
    private ValueMetaAndData channelField;
    private ValueMetaAndData advanced;
    private ValueMetaAndData aliasField;
    private ValueMetaAndData emojiField;
    private RocketchatClient rocketchat;

    public RocketchatStepMeta() {
        super(); // allocate BaseStepInfo
    }

    /**
     * @return Returns the value.
     */
    public ValueMetaAndData getUrl() {
        return url;
    }

    /**
     * @param value The value to set.
     */
    public void setUrl(ValueMetaAndData url) {
        this.url = url;
    }

    public ValueMetaAndData getUser() {
        return user;
    }

    public void setUser(ValueMetaAndData user) {
        this.user = user;
    }

    public ValueMetaAndData getPassword() {
        return password;
    }

    public void setPassword(ValueMetaAndData password) {
        this.password = password;
    }

    public ValueMetaAndData getChannelField() {
        return channelField;
    }

    public void setChannelField(ValueMetaAndData channelField) {
        this.channelField = channelField;
    }

    public ValueMetaAndData getMessageField() {
        return messageField;
    }

    public void setMessageField(ValueMetaAndData messageField) {
        this.messageField = messageField;
    }

    public ValueMetaAndData getAdvanced() {
        return advanced;
    }

    public void setAdvanced(ValueMetaAndData advanced) {
        this.advanced = advanced;
    }

    public ValueMetaAndData getAliasField() {
        return aliasField;
    }

    public void setAliasField(ValueMetaAndData aliasField) {
        this.aliasField = aliasField;
    }

    public ValueMetaAndData getEmojiField() {
        return emojiField;
    }

    public void setEmojiField(ValueMetaAndData emojiField) {
        this.emojiField = emojiField;
    }

    public void startRocketchat(String url, String user, String password) throws IOException {
        rocketchat = new RocketchatClient(url, user, password);
    }

    public boolean sendRocketchat(String channel, String message) {
        try {
            rocketchat.send_message(channel, message);
            return true;
        } catch (MalformedURLException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean sendRocketchat(String channel, String message, String alias, String emoji) {
        try {
            rocketchat.send_message(channel, message, alias, emoji);
            return true;
        } catch (MalformedURLException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public String getXML() throws KettleException {
        ValueMetaAndData password_ = (ValueMetaAndData) password.clone();
        password_.setValueData(Encr.encryptPasswordIfNotUsingVariables((String) password_.getValueData()));
        String retval = "";

        retval += "    <values>" + Const.CR;
        if (url != null) {
            retval += url.getXML();
            retval += user.getXML();
            retval += password_.getXML();
            retval += channelField.getXML();
            retval += messageField.getXML();
            retval += advanced.getXML();
            retval += aliasField.getXML();
            retval += emojiField.getXML();
        }
        retval += "      </values>" + Const.CR;

        return retval;
    }

    @Override
    public void getFields(RowMetaInterface r, String origin, RowMetaInterface[] info, StepMeta nextStep,
            VariableSpace space, Repository repository, IMetaStore metaStore) {
//        if ( url != null ) {
//            ValueMetaInterface v = url.getValueMeta();
//            v.setOrigin( origin );
//            r.addValueMeta( v );
//        }
//        if ( user != null ) {
//            ValueMetaInterface v = user.getValueMeta();
//            v.setOrigin( origin );
//            r.addValueMeta( v );
//        }
//        if ( password != null ) {
//            ValueMetaInterface v = password.getValueMeta();
//            v.setOrigin( origin );
//            r.addValueMeta( v );
//        }
//        if ( channelField != null ) {
//            ValueMetaInterface v = channelField.getValueMeta();
//            v.setOrigin( origin );
//            r.addValueMeta( v );
//        }
        ValueMetaAndData status = new ValueMetaAndData(new ValueMetaBoolean(), null);
        status.getValueMeta().setName("status");
        ValueMetaInterface v = status.getValueMeta();
        v.setOrigin(origin);
        r.addValueMeta(v);
//      }
    }

    @Override
    public Object clone() {
        Object retval = super.clone();
        return retval;
    }

    @Override
    public void loadXML(Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore) throws KettleXMLException {
        try {
            url = new ValueMetaAndData();
            user = new ValueMetaAndData();
            password = new ValueMetaAndData();
            channelField = new ValueMetaAndData();
            messageField = new ValueMetaAndData();
            advanced = new ValueMetaAndData(new ValueMetaBoolean(), "advanced");
            aliasField = new ValueMetaAndData();
            emojiField = new ValueMetaAndData();

//            Node val = XMLHandler.getNodeWithTagValue(XMLHandler.getSubNode( stepnode, "values"), "value", "name", "url", 0);
//            if (val != null) {logError("search for the url tag: " + val.getTextContent());} else {logError("search for url tag failed!");}
//
//            Node valnode = XMLHandler.getSubNode( stepnode, "values", "value" );
            Node valnode = XMLHandler.getNodeWithTagValue(XMLHandler.getSubNode(stepnode, "values"), "value", "name",
                    "url", 0);
            if (valnode != null) {
//                logError("XML nodeName: " + valnode.getTextContent());
//                logError("XML nodeName: " + XMLHandler.getSubNode(valnode, "name").getTextContent());
                System.out.println("reading value in " + valnode);
                url.loadXML(valnode);
            }
            valnode = XMLHandler.getNodeWithTagValue(XMLHandler.getSubNode(stepnode, "values"), "value", "name", "user",
                    0);
            if (valnode != null) {
                System.out.println("reading value in " + valnode);
                user.loadXML(valnode);
            }
            valnode = XMLHandler.getNodeWithTagValue(XMLHandler.getSubNode(stepnode, "values"), "value", "name",
                    "password", 0);
            if (valnode != null) {
                System.out.println("reading value in " + valnode);
                password.loadXML(valnode);
            }
            valnode = XMLHandler.getNodeWithTagValue(XMLHandler.getSubNode(stepnode, "values"), "value", "name",
                    "channelField", 0);
            if (valnode != null) {
                System.out.println("reading value in " + valnode);
                channelField.loadXML(valnode);
            }
            valnode = XMLHandler.getNodeWithTagValue(XMLHandler.getSubNode(stepnode, "values"), "value", "name",
                    "messageField", 0);
            if (valnode != null) {
                System.out.println("reading value in " + valnode);
                messageField.loadXML(valnode);
            }
            valnode = XMLHandler.getNodeWithTagValue(XMLHandler.getSubNode(stepnode, "values"), "value", "name",
                    "advanced", 0);
            if (valnode != null) {
                System.out.println("reading value in " + valnode);
                advanced.loadXML(valnode);
            }
            valnode = XMLHandler.getNodeWithTagValue(XMLHandler.getSubNode(stepnode, "values"), "value", "name",
                    "aliasField", 0);
            if (valnode != null) {
                System.out.println("reading value in " + valnode);
                aliasField.loadXML(valnode);
            }
            valnode = XMLHandler.getNodeWithTagValue(XMLHandler.getSubNode(stepnode, "values"), "value", "name",
                    "emojiField", 0);
            if (valnode != null) {
                System.out.println("reading value in " + valnode);
                emojiField.loadXML(valnode);
            }
            if (url.getValueData() == null) url.setValueData("");
            if (user.getValueData() == null) user.setValueData("");
            if (password.getValueData() == null) password.setValueData("");
            if (channelField.getValueData() == null) channelField.setValueData("");
            if (messageField.getValueData() == null) messageField.setValueData("");
            if (advanced.getValueData() == null) advanced.setValueData("");
            if (aliasField.getValueData() == null) aliasField.setValueData("");
            if (emojiField.getValueData() == null) emojiField.setValueData("");
            password.setValueData(Encr.decryptPasswordOptionallyEncrypted((String) password.getValueData()));
        } catch (Exception e) {
            throw new KettleXMLException("Unable to read step info from XML node", e);
        }
    }

    @Override
    public void setDefault() {
//        urlValue = new ValueMetaAndData( new ValueMetaNumber( "valuename" ), new Double( 123.456 ) );
//        urlValue.getValueMeta().setLength( 12 );
//        urlValue.getValueMeta().setPrecision( 4 );
        url = new ValueMetaAndData(new ValueMetaString(), "https://rocket.chat");
        user = new ValueMetaAndData(new ValueMetaString(), "Username");
        password = new ValueMetaAndData(new ValueMetaString(), "Password");
        channelField = new ValueMetaAndData(new ValueMetaString(), "Recipient / #Channel field");
        messageField = new ValueMetaAndData(new ValueMetaString(), "Message field");
        advanced = new ValueMetaAndData(new ValueMetaBoolean(), false);
        aliasField = new ValueMetaAndData(new ValueMetaString(), "Alias field");
        emojiField = new ValueMetaAndData(new ValueMetaString(), "Emoji / avatar url field");
        url.getValueMeta().setName("url");
        user.getValueMeta().setName("user");
        password.getValueMeta().setName("password");
        channelField.getValueMeta().setName("channelField");
        messageField.getValueMeta().setName("messageField");
        advanced.getValueMeta().setName("advanced");
        aliasField.getValueMeta().setName("aliasField");
        emojiField.getValueMeta().setName("emojiField");
    }

    @Override
    public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev,
            String[] input, String[] output, RowMetaInterface info, VariableSpace space, Repository repository,
            IMetaStore metaStore) {
        CheckResult cr;
        if (prev == null || prev.size() == 0) {
            cr = new CheckResult(CheckResult.TYPE_RESULT_WARNING, "Not receiving any fields from previous steps!",
                    stepMeta);
            remarks.add(cr);
        } else {
            cr = new CheckResult(CheckResult.TYPE_RESULT_OK,
                    "Step is connected to previous one, receiving " + prev.size() + " fields", stepMeta);
            remarks.add(cr);
        }

        // See if we have input streams leading to this step!
        if (input.length > 0) {
            cr = new CheckResult(CheckResult.TYPE_RESULT_OK, "Step is receiving info from other steps.", stepMeta);
            remarks.add(cr);
        } else {
            cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, "No input received from other steps!", stepMeta);
            remarks.add(cr);
        }
    }

    public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta, String name) {
        return new RocketchatStepDialog(shell, meta, transMeta, name);
    }

    @Override
    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
            Trans disp) {
        return new RocketchatStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
    }

    @Override
    public StepDataInterface getStepData() {
        return new RocketchatStepData();
    }
}