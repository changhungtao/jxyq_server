package com.jxyq.model.others;

public class ResponseContent {
    private Object success_message;
    private ErrorMessage error_message;

    public ResponseContent(){
        this.success_message = null;
        this.error_message = null;
    }

    public ErrorMessage getError_message() {
        return error_message;
    }

    public void setError_message(ErrorMessage error_message) {
        this.error_message = error_message;
    }

    public Object getSuccess_message() {
        return success_message;
    }

    public void setSuccess_message(Object success_message) throws Exception{
        if (success_message==null){
            this.success_message = new Object();}
        else{
            this.success_message = success_message;
        }
    }
}
