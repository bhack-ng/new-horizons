package ru.simplex_software.arbat_baza.viewmodel.admin;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ExecutionArgParam;

import java.util.List;
public class FeedMessageLogVM {
    List<String> messageList ;
    @AfterCompose
    public void afterCompose(@ExecutionArgParam("errors")List<String> messageList){
        this.messageList = messageList;
    }

    public List<String> getMessageList() {
        return messageList;
    }
}
