package org.kettle.rocketchat;

import java.io.IOException;
import java.util.Objects;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.ValueMetaAndData;
import org.pentaho.di.core.row.value.ValueMetaBoolean;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;

public class RocketchatStep extends BaseStep implements StepInterface {
    private RocketchatStepData data;
    private RocketchatStepMeta meta;

    public RocketchatStep(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis) {
        super(s, stepDataInterface, c, t, dis);
    }

    @Override
    public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
        meta = (RocketchatStepMeta) smi;
        data = (RocketchatStepData) sdi;

        Object[] r = getRow(); // get row, blocks when needed!
        if (r == null) { // no more input to be expected...
            setOutputDone();
            return false;
        }

        if (first) {
            first = false;
            data.outputRowMeta = getInputRowMeta().clone();
            meta.getFields(data.outputRowMeta, getStepname(), null, null, this, repository, metaStore);
        }
        String channel = Objects.toString(r[data.outputRowMeta.indexOfValue(meta.getChannelField().toString())], "");
        String message = Objects.toString(r[data.outputRowMeta.indexOfValue(meta.getMessageField().toString())], "");

        boolean success;
        if ((boolean) meta.getAdvanced().getValueData()) {
            String alias = Objects.toString(r[data.outputRowMeta.indexOfValue(meta.getAliasField().toString())], "");
            String emoji = Objects.toString(r[data.outputRowMeta.indexOfValue(meta.getEmojiField().toString())], "");
            success = meta.sendRocketchat(channel, message, alias, emoji);
        } else {
            success = meta.sendRocketchat(channel, message);
        }

        ValueMetaAndData extraValue = new ValueMetaAndData(new ValueMetaBoolean("status"), success);
        Object[] outputRow = RowDataUtil.addValueData(r, data.outputRowMeta.indexOfValue("status"),
                extraValue.getValueData());

        putRow(data.outputRowMeta, outputRow); // copy row to possible alternate rowset(s).

        if (checkFeedback(getLinesRead())) {
            logBasic("Linenr " + getLinesRead()); // Some basic logging every 5000 rows.
        }

        return true;
    }

    public Object find_field(Object[] array, String column_name) {
        for (Object cell : array) {
            logBasic(cell.toString());
        }
        return null;
    }

    @Override
    public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
        meta = (RocketchatStepMeta) smi;
        data = (RocketchatStepData) sdi;

        String url = environmentSubstitute(meta.getUrl().toString());
        String user = environmentSubstitute(meta.getUser().toString());
        String password = environmentSubstitute(meta.getPassword().toString());
        try {
            meta.startRocketchat(url, user, password);
        } catch (IOException e) {
            logError("RocketChat Connection init failed. Bad RocketChat login information!");
            return false;
        }

        return super.init(smi, sdi);
    }

    @Override
    public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
        meta = (RocketchatStepMeta) smi;
        data = (RocketchatStepData) sdi;

        super.dispose(smi, sdi);
    }

    //
    // Run is were the action happens!
    public void run() {
        logBasic("Starting to run...");
        try {
            while (processRow(meta, data) && !isStopped()) {
                // Process rows
            }
        } catch (Exception e) {
            logError("Unexpected error : " + e.toString());
            logError(Const.getStackTracker(e));
            setErrors(1);
            stopAll();
        } finally {
            dispose(meta, data);
            logBasic("Finished, processing " + getLinesRead() + " rows");
            markStop();
        }
    }
}