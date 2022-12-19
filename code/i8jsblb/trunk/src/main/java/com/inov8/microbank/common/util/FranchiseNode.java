package com.inov8.microbank.common.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.swing.tree.TreeNode;

import com.google.common.collect.Iterators;

/**
 * Usecase: Common For Tree Component
 * Date: 
 * Author: Rashid Mahmood
 * Email:  rashid.mahmood@its.ws
 */

public class FranchiseNode implements TreeNode
{
    private String label;
    private String value;
    
    private List<AgentNode> agentLevelNodes = new ArrayList<AgentNode>();
    
    public FranchiseNode()
    {
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getLabel()
    {
        return label;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
    
    public String getType()
    {
        return "FRANCHISE_NODE";
    }
    
    public List<AgentNode> getAgentLevelNodes() {
		return agentLevelNodes;
	}

	public void setAgentLevelNodes(List<AgentNode> agentLevelNodes) {
		this.agentLevelNodes = agentLevelNodes;
	}

	 @Override
		public TreeNode getChildAt(int childIndex) {
			return this.agentLevelNodes.get(childIndex);
		}

		@Override
		public int getChildCount() {
			return agentLevelNodes.size();
		}

		@Override
		public TreeNode getParent() 
		{
			return null;
		}

		@Override
		public int getIndex(TreeNode node) {
			return agentLevelNodes.indexOf(node);
		}

		@Override
		public boolean getAllowsChildren() {
			return true;
		}

		@Override
		public boolean isLeaf() {
			return agentLevelNodes.isEmpty();
		}

		@Override
		public Enumeration<AgentNode> children() {
			return Iterators.asEnumeration(agentLevelNodes.iterator());
		}
		
		public Iterator<AgentNode> childrenIterator() {
			return agentLevelNodes.iterator();
		}
		
		public void addChild(AgentNode agentNode)
		{
			this.agentLevelNodes.add(agentNode);
		}
}
