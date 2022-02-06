package ru.simplex_software.arbat_baza.viewmodel.admin;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.simplex_software.arbat_baza.AuthService;
import ru.simplex_software.arbat_baza.dao.AgencyDAO;
import ru.simplex_software.arbat_baza.dao.AgentDAO;
import ru.simplex_software.arbat_baza.model.Agent;

import java.util.HashMap;
import java.util.List;

/**
 * View model for user management..
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AgentListVM {
	@WireVariable
	private AgentDAO agentDAO;
	@WireVariable
	private AuthService authService;
	private boolean showBlocked = false;

	List<Agent> agents;

    private Window win;
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view){
		agents=agentDAO.findVisible();
        win=view;
    }


	@Command
	public void edit(@BindingParam("agent") Agent agent){
		HashMap params = new HashMap();
		params.put("agent",agent);
		Component editWin = Executions.createComponents("/WEB-INF/zul/admin/editAgent.zul", win, params);
		editWin.addEventListener("onChangeList",  e-> refresh());


	}
	@Command
	public void create(){
		HashMap params = new HashMap();
		Agent agent = new Agent();
		agent.setAgency(authService.getLogginedAgent().getAgency());
		params.put("agent", agent);
		Component editWin = Executions.createComponents("/WEB-INF/zul/admin/editAgent.zul", win, params);
		editWin.addEventListener("onChangeList", e-> refresh());

	}

	@Command
    public void refresh() {
		if (showBlocked == true){
        agents=agentDAO.findAll();
		}
		else {
			agents = agentDAO.findVisible();
		}
        BindUtils.postNotifyChange(null,null,this,"*");
    }

    @Command
	public void delete(@BindingParam("agent") Agent agent){
		agent=agentDAO.get(agent.getPrimaryKey());
		agentDAO.delete(agent);
        refresh();
	}
    public List<Agent> getAgents(){
		return agents;
	}

	public boolean isShowBlocked() {
		return showBlocked;
	}

	public void setShowBlocked(boolean showBlocked) {
		this.showBlocked = showBlocked;
	}

}
