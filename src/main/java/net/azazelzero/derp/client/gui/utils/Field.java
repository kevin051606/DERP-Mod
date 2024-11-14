package net.azazelzero.derp.client.gui.utils;

import java.util.List;

public class Field {
    public final String FieldName;
    public String defaultString;
    public int defaultInteger;
    public boolean defaultBool;
    public List<String> defaultList;
    public final FieldType type;

    public Field(String fieldName, String string){
        FieldName = fieldName;
        this.defaultString =string;
        this.type = FieldType.String;
    }
    public Field(String fieldName, int integer){
        FieldName = fieldName;
        this.defaultInteger =integer;
        this.type = FieldType.Integer;
    }
    public Field(String fieldName, boolean bool){
        FieldName = fieldName;
        this.defaultBool =bool;
        this.type = FieldType.Boolean;
    }
    public Field(String fieldName, List<String> list){
        FieldName = fieldName;
        this.defaultList=list;
        this.type = FieldType.List;
    }
    public enum FieldType{
        String,
        Integer,
        List, Boolean
    }

}
