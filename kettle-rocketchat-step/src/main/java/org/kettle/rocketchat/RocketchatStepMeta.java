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
    private ValueMetaAndData statusFieldName;
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

    public ValueMetaAndData getStatusFieldName() {
        return statusFieldName;
    }

    public void setStatusFieldName(ValueMetaAndData statusFieldName) {
        this.statusFieldName = statusFieldName;
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
        password_.setValueData(Encr.encryptPasswordIfNotUsingVariables(password_.getValueData().toString()));
        String retval = "";

        retval += "    <values>" + Const.CR;
            retval += url.getXML();
            retval += user.getXML();
            retval += password_.getXML();
            retval += channelField.getXML();
            retval += messageField.getXML();
            retval += advanced.getXML();
            if ((boolean) advanced.getValueData()) {
                retval += aliasField.getXML();
                retval += emojiField.getXML();
            }
            retval += statusFieldName.getXML();
        retval += "      </values>" + Const.CR;

        return retval;
    }

    @Override
    public void loadXML(Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore) throws KettleXMLException {
        try {
            url = new ValueMetaAndData(new ValueMetaString(), "url");
            user = new ValueMetaAndData(new ValueMetaString(), "user");
            password = new ValueMetaAndData(new ValueMetaString(), "password");
            channelField = new ValueMetaAndData(new ValueMetaString(), "channelField");
            messageField = new ValueMetaAndData(new ValueMetaString(), "messageField");
            advanced = new ValueMetaAndData(new ValueMetaBoolean(), "advanced");
            aliasField = new ValueMetaAndData(new ValueMetaString(), "aliasField");
            emojiField = new ValueMetaAndData(new ValueMetaString(), "emojiField");
            statusFieldName = new ValueMetaAndData(new ValueMetaString(), "statusFieldName");

            injectXML(stepnode, url, "url");
            injectXML(stepnode, user, "user");
            injectXML(stepnode, password, "password");
            injectXML(stepnode, channelField, "channelField");
            injectXML(stepnode, messageField, "messageField");
            injectXML(stepnode, advanced, "advanced");
            injectXML(stepnode, aliasField, "aliasField");
            injectXML(stepnode, emojiField, "emojiField");
            injectXML(stepnode, statusFieldName, "statusFieldName");
            fix_names();

            password.setValueData(Encr.decryptPasswordOptionallyEncrypted(password.getValueData().toString()));
        } catch (Exception e) {
            throw new KettleXMLException("Unable to read step info from XML node", e);
        }
    }

    @Override
    public void setDefault() {
        url = new ValueMetaAndData(new ValueMetaString(), "https://rocket.chat");
        user = new ValueMetaAndData(new ValueMetaString(), "Username");
        password = new ValueMetaAndData(new ValueMetaString(), "Password");
        channelField = new ValueMetaAndData(new ValueMetaString(), "Recipient / #Channel field");
        messageField = new ValueMetaAndData(new ValueMetaString(), "Message field");
        advanced = new ValueMetaAndData(new ValueMetaBoolean(), false);
        aliasField = new ValueMetaAndData(new ValueMetaString(), "Alias field");
        emojiField = new ValueMetaAndData(new ValueMetaString(), "Emoji / avatar url field");
        statusFieldName = new ValueMetaAndData(new ValueMetaString(), "status");
        fix_names();
    }

    public void fix_names() {
        if (url.getValueMeta().getName() == null) url.getValueMeta().setName("url");
        if (user.getValueMeta().getName() == null) user.getValueMeta().setName("user");
        if (password.getValueMeta().getName() == null) password.getValueMeta().setName("password");
        if (channelField.getValueMeta().getName() == null) channelField.getValueMeta().setName("channelField");
        if (messageField.getValueMeta().getName() == null) messageField.getValueMeta().setName("messageField");
        if (advanced.getValueMeta().getName() == null) advanced.getValueMeta().setName("advanced");
        if (aliasField.getValueMeta().getName() == null) aliasField.getValueMeta().setName("aliasField");
        if (emojiField.getValueMeta().getName() == null) emojiField.getValueMeta().setName("emojiField");
        if (statusFieldName.getValueMeta().getName() == null) statusFieldName.getValueMeta().setName("statusFieldName");
    }
    

    @Override
    public void getFields(RowMetaInterface r, String origin, RowMetaInterface[] info, StepMeta nextStep,
            VariableSpace space, Repository repository, IMetaStore metaStore) {
        ValueMetaAndData status = new ValueMetaAndData(new ValueMetaBoolean(), null);
        status.getValueMeta().setName(statusFieldName.getValueData().toString());
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

    private void injectXML(Node stepnode, ValueMetaAndData attribute, String name) {
        Node valnode = XMLHandler.getNodeWithTagValue(XMLHandler.getSubNode(stepnode, "values"), "value", "name", name,
                0);
        if (valnode != null) {
//            logError("XML nodeName: " + valnode.getTextContent());
//            logError("XML nodeName: " + XMLHandler.getSubNode(valnode, "name").getTextContent());
//            System.out.println("reading value in " + valnode);
            attribute.loadXML(valnode);
        }
        if (attribute.getValueData() == null) attribute.setValueData("");
    }
}