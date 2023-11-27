package com.desarrollojlcp.gps_topography.model.csv;

public interface CSVReadProc {
    void procRow(int rowIndex, String... values);
}
