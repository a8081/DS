<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<!--<jstl:if test="${isAdministrator eq true }" >
			<acme:button url="submission/administrator/runAssignation.do" name="display" code="conference.run.assignation"/>

</jstl:if>

<jstl:if test="${messageSuccessRunAssignation eq true}">
    	<h5 style="color: red;"><spring:message code="message.success.run.assignation"/></h5>
</jstl:if> -->

<jstl:if test="${messageSuccessAssign eq true}">
    	<h5 style="color: red;"><spring:message code="message.success.assign"/></h5>
</jstl:if> 
    
<jstl:if test="${isAdministrator eq true or isAuthor eq true }" >

<display:table name="submissions" id="row"
		requestURI="${requestURI}" pagesize="5"
		class="displaytag">
		
	
	<display:column property="ticker" titleKey="submission.ticker" />
	
	<display:column property="conference.title" titleKey="submission.conference" />
	
	<display:column property="status" titleKey="submission.status" />
	
	
	
	<jstl:choose>
	<jstl:when test="${isAdministrator eq true }">
		
		<display:column>
			<acme:button url="submission/administrator/display.do?submissionId=${row.id}" name="display" code="submission.display"/>
			
		</display:column>
		<display:column>
			<jstl:if test="${row.status eq 'UNDER-REVIEWED' }" >
			<acme:button url="submission/administrator/assign.do?submissionId=${row.id}" name="display" code="submission.assign.reviewer"/>
			</jstl:if>
		</display:column>
	
	</jstl:when>
	<jstl:when test="${isAuthor eq true }">
	
		<display:column>
			<acme:button url="submission/author/display.do?submissionId=${row.id}" name="display" code="submission.display"/>
			
		</display:column>
		
		
		
	</jstl:when>
	</jstl:choose>
	
	
	

</display:table>

</jstl:if>