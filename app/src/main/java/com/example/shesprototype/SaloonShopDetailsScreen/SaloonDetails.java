package com.example.shesprototype.SaloonShopDetailsScreen;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaloonDetails {

    @SerializedName("result")
    @Expose
    private SaloonDetailsData result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public SaloonDetailsData getResult() {
        return result;
    }

    public void setResult(SaloonDetailsData result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
