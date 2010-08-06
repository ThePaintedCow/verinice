/*******************************************************************************
 * Copyright (c) 2009 Daniel Murygin <dm[at]sernet[dot]de>.
 * This program is free software: you can redistribute it and/or 
 * modify it under the terms of the GNU Lesser General Public License 
 * as published by the Free Software Foundation, either version 3 
 * of the License, or (at your option) any later version.
 *     This program is distributed in the hope that it will be useful,    
 * but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU Lesser General Public License for more details.
 *     You should have received a copy of the GNU Lesser General Public 
 * License along with this program. 
 * If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Daniel Murygin <dm[at]sernet[dot]de> - initial API and implementation
 ******************************************************************************/
package sernet.gs.ui.rcp.main.connect;

import java.io.Serializable;

import sernet.verinice.interfaces.IBaseDao;
import sernet.verinice.interfaces.IRetrieveInfo;
import sernet.verinice.model.common.CnATreeElement;


/**
 * Parameter of Method retrieve in {@link IBaseDao}.
 * Determined by RetrieveInfo retrieve will join references of {@link CnATreeElement}.
 * 
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 */
public class RetrieveInfo implements Serializable, IRetrieveInfo{

	private static final RetrieveInfo PROPERTY_INSTANCE;
	private static final RetrieveInfo CHILDREN_INSTANCE;
	private static final RetrieveInfo PROPERTY_CHILDREN_INSTANCE;
	
	static {
		PROPERTY_INSTANCE = new RetrieveInfo();
		PROPERTY_INSTANCE.setProperties(true);
		
		CHILDREN_INSTANCE = new RetrieveInfo();
		CHILDREN_INSTANCE.setChildren(true);
		
		PROPERTY_CHILDREN_INSTANCE = new RetrieveInfo();
		PROPERTY_CHILDREN_INSTANCE.setProperties(true);
		PROPERTY_CHILDREN_INSTANCE.setChildren(true);
	}
	
	boolean properties = false;
	
	boolean linksUp = false;
	
	boolean linksUpProperties = false;
	
	boolean linksDown = false;
	
	boolean linksDownProperties = false;
	
	boolean children = false;
	
	boolean childrenProperties = false;
	
	private boolean grandchildren = false;
	
	boolean parent = false;
	
	boolean parentPermissions = false;
	
	boolean siblings = false;
	
	boolean permissions = false;
	
	boolean childrenPermissions = false;
	
	boolean innerJoin = false;

	public static RetrieveInfo getPropertyInstance() {
		return PROPERTY_INSTANCE;
	}
	
	public static RetrieveInfo getChildrenInstance() {
		return CHILDREN_INSTANCE;
	}
	
	public static RetrieveInfo getPropertyChildrenInstance() {
		return PROPERTY_CHILDREN_INSTANCE;
	}
	
	public RetrieveInfo() {
		super();
	}

	/**
	 * @return true if properties are joined and retrieved
	 */
	public boolean isProperties() {
		return properties;
	}

	public RetrieveInfo setProperties(boolean properties) {
		this.properties = properties;
		return this;
	}

	/**
	 * @return true if links-up are joined and retrieved
	 */
	public boolean isLinksUp() {
		return linksUp;
	}

	public RetrieveInfo setLinksUp(boolean linksUp) {
		this.linksUp = linksUp;
		return this;
	}

	/**
	 * @return true if properties of links-up are joined and retrieved
	 */
	public boolean isLinksUpProperties() {
		return linksUpProperties;
	}

	public RetrieveInfo setLinksUpProperties(boolean linksUpProperties) {
		this.linksUpProperties = linksUpProperties;
		return this;
	}

	/**
	 * @return true if links-down are joined and retrieved
	 */
	public boolean isLinksDown() {
		return linksDown;
	}

	public RetrieveInfo setLinksDown(boolean linksDown) {
		this.linksDown = linksDown;
		return this;
	}

	/**
	 * @return true if properties of links-down are joined and retrieved
	 */
	public boolean isLinksDownProperties() {
		return linksDownProperties;
	}

	public RetrieveInfo setLinksDownProperties(boolean linksDownProperties) {
		this.linksDownProperties = linksDownProperties;
		return this;
	}
	
	/**
	 * @return true if children are joined and retrieved
	 */
	public boolean isChildren() {
		return children;
	}

	public RetrieveInfo setChildren(boolean children) {
		this.children = children;
		return this;
	}
	
	/**
	 * @return true if properties of children are joined and retrieved
	 */
	public boolean isChildrenProperties() {
		return childrenProperties;
	}

	public RetrieveInfo setChildrenProperties(boolean childrenProperties) {
		this.childrenProperties = childrenProperties;
		return this;
	}
	
	public RetrieveInfo setGrandchildren(boolean grandchildren) {
		this.grandchildren = grandchildren;
		return this;
	}

	public boolean isGrandchildren() {
		return grandchildren;
	}

	public boolean isParent() {
		return parent;
	}

	public RetrieveInfo setParent(boolean parent) {
		this.parent = parent;
		return this;
	}


    public RetrieveInfo setParentPermissions(boolean parentPermissions) {
        this.parentPermissions = parentPermissions;
        return this;  
    }

    public boolean isParentPermissions() {
        return parentPermissions;
    }

    public boolean isSiblings() {
		return siblings;
	}

	public RetrieveInfo setSiblings(boolean siblings) {
		this.siblings = siblings;
		return this;
	}

	public boolean isPermissions() {
		return permissions;
	}
	
	public RetrieveInfo setPermissions(boolean permissions) {
		this.permissions = permissions;
		return this;
	}

	public RetrieveInfo setChildrenPermissions(boolean childrenPermissions) {
		this.childrenPermissions = childrenPermissions;
		return this;
	}

	public boolean isChildrenPermissions() {
		return childrenPermissions;
	}

	/**
	 * @return true if inner joins are used
	 */
	public boolean isInnerJoin() {
		return innerJoin;
	}

	public RetrieveInfo setInnerJoin(boolean innerJoin) {
		this.innerJoin = innerJoin;
		return this;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("RetrieveInfo - ");
		if(properties) {
			sb.append( " properties");
		}
		if(linksUp) {
			sb.append(" linksUp");
		}
		if(linksUpProperties) {
			sb.append(" linksUpProperties").append(linksUpProperties);
		}
		if(linksDown) {
			sb.append(" linksDown");
		}
		if(linksDownProperties) {
			sb.append(" linksDownProperties");
		}
		if(children) {
			sb.append(" children");
		}
		if(childrenProperties) {
			sb.append(" childrenProperties");
		}
		if(childrenProperties) {
			sb.append(" innerJoin");
		}
		if(isSiblings()) {
			sb.append(" siblings");
		}
		if(isPermissions()) {
			sb.append(" permissions");
		}
		if(isChildrenPermissions()) {
			sb.append(" childrenPermissions");
		}
        if(isParent()) {
            sb.append(" parent");
        }
        if(isParentPermissions()) {
            sb.append(" parentPermissions");
        }
		return sb.toString();
	}

	
}
