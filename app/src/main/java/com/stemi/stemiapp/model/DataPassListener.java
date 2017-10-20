package com.stemi.stemiapp.model;

import java.util.ArrayList;

/**
 * Created by Pooja on 14-09-2017.
 */

public interface DataPassListener {
    void passData(MedicineDetails data,String name);
    void goBack();
}