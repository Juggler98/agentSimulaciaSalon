package simulation;

import OSPABA.*;
import agents.*;

public class MySimulation extends Simulation {
    public MySimulation() {
        init();
    }

    @Override
    public void prepareSimulation() {
        super.prepareSimulation();
        // Create global statistcis
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Reset entities, queues, local statistics, etc...
    }

    @Override
    public void replicationFinished() {
        // Collect local statistics into global, update UI, etc...
        super.replicationFinished();
    }

    @Override
    public void simulationFinished() {
        // Dysplay simulation results
        super.simulationFinished();
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		setAgentModelu(new AgentModelu(Id.agentModelu, this, null));
		setAgentOkolia(new AgentOkolia(Id.agentOkolia, this, agentModelu()));
		setAgentSalonu(new AgentSalonu(Id.agentSalonu, this, agentModelu()));
		setAgentRecepcie(new AgentRecepcie(Id.agentRecepcie, this, agentSalonu()));
		setAgentUcesov(new AgentUcesov(Id.agentUcesov, this, agentSalonu()));
		setAgentLicenia(new AgentLicenia(Id.agentLicenia, this, agentSalonu()));
	}

	private AgentModelu _agentModelu;

public AgentModelu agentModelu()
	{ return _agentModelu; }

	public void setAgentModelu(AgentModelu agentModelu)
	{_agentModelu = agentModelu; }

	private AgentOkolia _agentOkolia;

public AgentOkolia agentOkolia()
	{ return _agentOkolia; }

	public void setAgentOkolia(AgentOkolia agentOkolia)
	{_agentOkolia = agentOkolia; }

	private AgentSalonu _agentSalonu;

public AgentSalonu agentSalonu()
	{ return _agentSalonu; }

	public void setAgentSalonu(AgentSalonu agentSalonu)
	{_agentSalonu = agentSalonu; }

	private AgentRecepcie _agentRecepcie;

public AgentRecepcie agentRecepcie()
	{ return _agentRecepcie; }

	public void setAgentRecepcie(AgentRecepcie agentRecepcie)
	{_agentRecepcie = agentRecepcie; }

	private AgentUcesov _agentUcesov;

public AgentUcesov agentUcesov()
	{ return _agentUcesov; }

	public void setAgentUcesov(AgentUcesov agentUcesov)
	{_agentUcesov = agentUcesov; }

	private AgentLicenia _agentLicenia;

public AgentLicenia agentLicenia()
	{ return _agentLicenia; }

	public void setAgentLicenia(AgentLicenia agentLicenia)
	{_agentLicenia = agentLicenia; }
	//meta! tag="end"
}