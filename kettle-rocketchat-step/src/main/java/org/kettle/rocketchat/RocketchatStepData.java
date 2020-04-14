package org.kettle.rocketchat;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

public class RocketchatStepData extends BaseStepData implements StepDataInterface {
    public RowMetaInterface outputRowMeta;

    public RocketchatStepData() {
        super();
    }
}