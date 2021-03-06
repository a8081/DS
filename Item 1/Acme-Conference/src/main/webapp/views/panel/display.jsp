<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

	<acme:showActivity title="${panel.title}"
	hours="${panel.hours}"
	minutes="${panel.minutes}" room="${panel.minutes}"
	summary="${panel.summary}"
	lang="${lang}" />

<jstl:choose>
	<jstl:when test="${not empty panel.startMoment}">
		<jstl:choose>
			<jstl:when test="${lang eq 'en' }">
				<spring:message code="activity.startMoment" />: <fmt:formatDate
					value="${panel.startMoment}" type="both" pattern="yyyy/MM/dd HH:mm" />
			</jstl:when>
			<jstl:otherwise>
				<spring:message code="activity.startMoment" />: <fmt:formatDate
					value="${panel.startMoment}" type="both" pattern="dd/MM/yyyy HH:mm" />
			</jstl:otherwise>
		</jstl:choose>
		<br />
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="activity.startMoment" />
		<spring:message code="empty" />
		<br />
	</jstl:otherwise>
</jstl:choose>

<jstl:set value="${panel.attachments.size()}" var="s1"/>
<jstl:set value="${panel.speakers.size()}" var="s2"/>
<jstl:choose>
	<jstl:when test="${not empty panel.attachments and (s1 gt 1)}">
		<spring:message code="activity.attachments"/>
		<jstl:forEach items="${panel.attachments}" var="line">
		<ul>
			<li><a href="${line}"><jstl:out value="${line}"/></a></li>
		</ul>
		</jstl:forEach>
	</jstl:when>
	<jstl:when test="${not empty panel.attachments and (s1 le 1)}">
		<spring:message code="activity.attachments"/>: 
			<a href="${panel.attachments.get(0)}"><jstl:out value="${panel.attachments.get(0)}"/></a><br />
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="activity.attachments" /> <spring:message code="empty" />
		<br />
	</jstl:otherwise>
</jstl:choose>
<jstl:choose>
	<jstl:when test="${not empty panel.speakers and (s2 gt 1)}">
		<spring:message code="activity.speakers"/>
		<jstl:forEach items="${panel.speakers}" var="line">
		<ul>
			<li><jstl:out value="${line}"/></li>
		</ul>
		</jstl:forEach>
	</jstl:when>
	<jstl:when test="${not empty panel.speakers and (s2 le 1)}">
		<spring:message code="activity.speakers"/>: 
			<jstl:out value="${panel.speakers.get(0)}"/><br />
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="activity.speakers" /> <spring:message code="empty" />
		<br />
	</jstl:otherwise>
</jstl:choose>
	
	<security:authorize access="hasRole('ADMIN')">
	<jstl:if test="${isDraft}">
	<acme:button url="panel/delete.do?panelId=${panel.id}&conferenceId=${conferenceId}"
		name="edit" code="activity.delete" /><br/>
	</jstl:if>
	</security:authorize>
	<acme:button url="panel/list.do?conferenceId=${conferenceId}" name="back" code="activity.back" />

<jstl:if test="${not empty error}">
	<h3 style="color: red;">
		<spring:message code="${error}" />
	</h3>
</jstl:if>



