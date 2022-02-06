package ru.simplex_software.arbat_baza.viewmodel.admin;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import ru.simplex_software.arbat_baza.dao.AgentDAO;
import ru.simplex_software.arbat_baza.model.Agent;

import java.security.SecureRandom;

import static ru.simplex_software.arbat_baza.PhotoUtils.getAgentImageUrl;

/**
 * view model for edit user.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EditAgentVM {
    private static final Logger LOG= LoggerFactory.getLogger(EditAgentVM.class);
    @WireVariable
    private AgentDAO agentDAO;
    private boolean created = false;
    private Agent agent;
    private Window win;
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view,
                             @ExecutionArgParam("agent") Agent agent){
        this.agent=agent;
        win=view;
        if(agent.getLogin()!=null){
            created = true;
        }


    }
    public Agent.Role[] getRoles(){
        return Agent.Role.values();
    }

    @Command
    public void save(){
        if(!validate()){
            return;
        }
        agentDAO.saveOrUpdate(agent);
        Events.postEvent("onChangeList", win, null);
        win.detach();
    }

    public String getImageUrl(){
        return getAgentImageUrl(agent);
    }


    @Command("upload")
    @NotifyChange("*")
    public void onImageUpload(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) {
        LOG.debug("Upload Event Is Coming");
        UploadEvent upEvent = null;
        Object objUploadEvent = ctx.getTriggerEvent();
        if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
            upEvent = (UploadEvent) objUploadEvent;
        }
        if (upEvent != null) {
            Media media = upEvent.getMedia();
            agent.setPhotoContentType(media.getContentType());
            agent.setPhoto(media.getByteData());
            agentDAO.saveOrUpdate(agent);
        }
    }


    @Command @NotifyChange("*")
    public void generatePassword(){
        final String randomStr = RandomStringUtils.random(8,0,0,true,true, null, new SecureRandom());
        agent.setPassword(randomStr);
    }
    
    public Agent getAgent(){
		return agent;
	}


    public boolean validate(){
        boolean valid = true;
        if ( (created == false) && ( agentDAO.findByLogin(agent.getLogin()) != null ) ) {
            valid = false;
            String errMessage = "Данный логин уже зарегистрирован в системе ";
            Messagebox.show(errMessage, "Внимание!", Messagebox.OK, Messagebox.ERROR);
        }

        return valid ;

    }
    public boolean isCreated() {
        return created;
    }
}
