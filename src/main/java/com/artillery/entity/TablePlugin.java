package com.artillery.entity;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TablePlugin {
    private final IntegerProperty pluginId;
    private final StringProperty pluginName;
    private final StringProperty pluginType;
    private final StringProperty pluginPath;


    public TablePlugin(int pluginId, String pluginName, String pluginType, String pluginPath) {
        this.pluginId = new SimpleIntegerProperty(pluginId);
        this.pluginName = new SimpleStringProperty(pluginName);
        this.pluginType = new SimpleStringProperty(pluginType);
        // Some initial dummy data, just for convenient testing.
        this.pluginPath = new SimpleStringProperty(pluginPath);
    }

    public int getPluginId() {
        return pluginId.get();
    }

    public void setPluginId(int pluginId) {
        this.pluginId.set(pluginId);
    }

    public IntegerProperty pluginIdProperty() {
        return pluginId;
    }


    public String getPluginName() {
        return pluginName.get();
    }

    public void setPluginName(String pluginName) {
        this.pluginName.set(pluginName);
    }

    public StringProperty pluginNameProperty() {
        return pluginName;
    }


    public String getPluginType() {
        return pluginType.get();
    }

    public void setPluginType(String pluginType) {
        this.pluginType.set(pluginType);
    }

    public StringProperty pluginTypeProperty() {
        return pluginType;
    }


    public String getPluginPath() {
        return pluginPath.get();
    }

    public void setPluginPath(String pluginPath) {
        this.pluginPath.set(pluginPath);
    }

    public StringProperty pluginPathProperty() {
        return pluginPath;
    }

}
