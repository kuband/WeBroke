package com.backend.app.model.type;

import java.io.Serializable;

public enum Operation implements Serializable {
    UPSERT,
    DELETE;

    public String getStatus() {
        return this.name();
    }
}
