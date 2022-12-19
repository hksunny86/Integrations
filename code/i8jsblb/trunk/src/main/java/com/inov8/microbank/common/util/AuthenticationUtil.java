package com.inov8.microbank.common.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtil{
	
	public AuthenticationUtil(){ }
  	
	public static boolean checkRightsIfAny(String csvRights){
        boolean result = true;
		Collection<GrantedAuthority> granted = getPrincipalAuthorities();
    	if ((null != csvRights) && !"".equals(csvRights)) {
            Set grantedCopy = retainAll(granted, parseAuthoritiesString(csvRights));
            if (grantedCopy.isEmpty()) {
            	result = false;
            }
        }
    	return result;
	}
	
	private static Collection getPrincipalAuthorities() {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (null == currentUser) {
            return Collections.EMPTY_LIST;
        }
        if ((null == currentUser.getAuthorities()) || currentUser.getAuthorities().isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        Collection granted = currentUser.getAuthorities();
        return granted;
    }

    private static Set parseAuthoritiesString(String authorizationsString) {
        final Set requiredAuthorities = new HashSet();
        final String[] authorities = org.springframework.util.StringUtils.commaDelimitedListToStringArray(authorizationsString);
        for (int i = 0; i < authorities.length; i++) {
            String authority = authorities[i];
            // Remove the role's whitespace characters without depending on JDK 1.4+
            // Includes space, tab, new line, carriage return and form feed.
            String role = authority.trim(); // trim, don't use spaces, as per SEC-378
            role = StringUtils.replace(role, "\t", "");
            role = StringUtils.replace(role, "\r", "");
            role = StringUtils.replace(role, "\n", "");
            role = StringUtils.replace(role, "\f", "");
            requiredAuthorities.add(new SimpleGrantedAuthority(role));
        }

        return requiredAuthorities;
    }
    
    private static Set authoritiesToRoles(Collection c) {
        Set target = new HashSet();
        for (Iterator iterator = c.iterator(); iterator.hasNext();) {
            GrantedAuthority authority = (GrantedAuthority) iterator.next();
            if (null == authority.getAuthority()) {
                throw new IllegalArgumentException(
                    "Cannot process GrantedAuthority objects which return null from getAuthority() - attempting to process "
                    + authority.toString());
            }
            target.add(authority.getAuthority());
        }
        return target;
    }
    
    private static Set retainAll(final Collection granted, final Set required) {
        Set grantedRoles = authoritiesToRoles(granted);
        Set requiredRoles = authoritiesToRoles(required);
        grantedRoles.retainAll(requiredRoles);
        return rolesToAuthorities(grantedRoles, granted);
    }
    
    private static Set rolesToAuthorities(Set grantedRoles, Collection granted) {
        Set target = new HashSet();
        for (Iterator iterator = grantedRoles.iterator(); iterator.hasNext();) {
            String role = (String) iterator.next();
            for (Iterator grantedIterator = granted.iterator(); grantedIterator.hasNext();) {
                GrantedAuthority authority = (GrantedAuthority) grantedIterator.next();
                if (authority.getAuthority().equals(role)) {
                    target.add(authority);
                    break;
                }
            }
        }
        return target;
    }
}
