<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<acme:display code="report.originality" value="${report.originality}"/>
<acme:display code="report.quality" value="${report.quality}"/>
<acme:display code="report.readability" value="${report.readability}"/>
<spring:message code="report.decision" />:
<jstl:if test="${report.decision eq 'REJECT'}"><spring:message code="REJECT"/></jstl:if>
<jstl:if test="${report.decision eq 'BORDER-LINE'}"><spring:message code="BORDER.LINE"/></jstl:if>
<jstl:if test="${report.decision eq 'ACCEPT'}"><spring:message code="ACCEPT"/></jstl:if>

<security:authorize access="hasRole('AUTHOR')">
	<acme:button code="report.submission" name="submission" url="submission/author/display.do?submissionId=${report.submission.id}"/>
</security:authorize>

<security:authorize access="hasRole('REVIEWER')">
	<acme:button code="report.submission" name="submission" url="submission/reviewer/display.do?submissionId=${report.submission.id}"/>
</security:authorize>

<acme:button code="report.listComment" name="list" url="comment/list.do?entity=report&id=${report.id}"/>

