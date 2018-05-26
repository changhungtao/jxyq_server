package com.jxyq.model.doctor;

public class Filter {
    private int filter_id;
    private int data_permission_id;
    private int column_name_id;
    private int comparison_op_id;
    private int column_value;
    private int logical_op_id;

    public int getFilter_id() {
        return filter_id;
    }

    public void setFilter_id(int filter_id) {
        this.filter_id = filter_id;
    }

    public int getData_permission_id() {
        return data_permission_id;
    }

    public void setData_permission_id(int data_permission_id) {
        this.data_permission_id = data_permission_id;
    }

    public int getColumn_name_id() {
        return column_name_id;
    }

    public void setColumn_name_id(int column_name_id) {
        this.column_name_id = column_name_id;
    }

    public int getComparison_op_id() {
        return comparison_op_id;
    }

    public void setComparison_op_id(int comparison_op_id) {
        this.comparison_op_id = comparison_op_id;
    }

    public int getColumn_value() {
        return column_value;
    }

    public void setColumn_value(int column_value) {
        this.column_value = column_value;
    }

    public int getLogical_op_id() {
        return logical_op_id;
    }

    public void setLogical_op_id(int logical_op_id) {
        this.logical_op_id = logical_op_id;
    }
}
