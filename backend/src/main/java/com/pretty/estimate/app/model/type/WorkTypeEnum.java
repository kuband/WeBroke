package com.we.broke.app.model.type;

import java.io.Serializable;

public enum WorkTypeEnum implements Serializable {
    PROJECT,
    ESTIMATE,
    ORG;

    public String getStatus() {
        return this.name();
    }
}
