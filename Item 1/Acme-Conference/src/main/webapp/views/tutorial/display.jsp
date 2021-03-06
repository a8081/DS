<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<acme:showActivity title="${tutorial.title}"
	hours="${tutorial.hours}"
	minutes="${tutorial.minutes}" room="${tutorial.minutes}"
	summary="${tutorial.summary}"
	lang="${lang}"/>

<jstl:choose>
	<jstl:when test="${not empty tutorial.startMoment}">
		<jstl:choose>
			<jstl:when test="${lang eq 'en' }">
				<spring:message code="activity.startMoment" />: <fmt:formatDate
					value="${tutorial.startMoment}" type="both" pattern="yyyy/MM/dd HH:mm" />
			</jstl:when>
			<jstl:otherwise>
				<spring:message code="activity.startMoment" />: <fmt:formatDate
					value="${tutorial.startMoment}" type="both" pattern="dd/MM/yyyy HH:mm" />
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
<jstl:set value="${tutorial.attachments.size()}" var="s1"/>
<jstl:set value="${tutorial.speakers.size()}" var="s2"/>
<jstl:choose>
	<jstl:when test="${not empty tutorial.attachments and (s1 gt 1)}">
		<spring:message code="activity.attachments"/>
		<jstl:forEach items="${tutorial.attachments}" var="line">
		<ul>
			<li><a href="${line}"><jstl:out value="${line}"/></a></li>
		</ul>
		</jstl:forEach>
	</jstl:when>
	<jstl:when test="${not empty tutorial.attachments and (s1 le 1)}">
		<spring:message code="activity.attachments"/>: 
			<a href="${tutorial.attachments.get(0)}"><jstl:out value="${tutorial.attachments.get(0)}"/></a><br />
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="activity.attachments" /> <spring:message code="empty" />
		<br />
	</jstl:otherwise>
</jstl:choose>
<jstl:choose>
	<jstl:when test="${not empty tutorial.speakers and (s2 gt 1)}">
		<spring:message code="activity.speakers"/>
		<jstl:forEach items="${tutorial.speakers}" var="line">
		<ul>
			<li><jstl:out value="${line}"/></li>
		</ul>
		</jstl:forEach>
	</jstl:when>
	<jstl:when test="${not empty tutorial.speakers and (s2 le 1)}">
		<spring:message code="activity.speakers"/>: 
			<jstl:out value="${tutorial.speakers.get(0)}"/><br />
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="activity.speakers" /> <spring:message code="empty" />
		<br />
	</jstl:otherwise>
</jstl:choose>
<br/>

<h3><spring:message code="tutorial.section" /></h3>
<!-- Lista de sections dentro del tutorial -->
<display:table name="${tutorial.sections}" id="row"
	requestURI="section/list.do?tutorialId=${tutorial.id}" pagesize="5"
	class="displaytag">

	<display:column property="title" titleKey="tutorial.section.title" />

	<display:column>
			<acme:button
				url="section/display.do?sectionId=${row.id}&tutorialId=${tutorial.id}&conferenceId=${conferenceId}&isDraft=${isDraft}"
				name="edit" code="tutorial.section.display" />
	</display:column>

	<display:column>
		<security:authorize access="hasRole('ADMIN')">
		<jstl:if test="${isDraft}">
			<acme:button
				url="section/edit.do?sectionId=${row.id}&tutorialId=${tutorial.id}&conferenceId=${conferenceId}"
				name="edit" code="tutorial.section.edit" />
		</jstl:if>
		</security:authorize>
	</display:column>
</display:table>
<security:authorize access="hasRole('ADMIN')">
	<jstl:if test="${isDraft}">
	<acme:button url="section/create.do?tutorialId=${tutorial.id}&conferenceId=${conferenceId}"
		name="edit" code="tutorial.section.create" /><br/>
	</jstl:if>
</security:authorize>

<security:authorize access="hasRole('ADMIN')">
	<jstl:if test="${isDraft}">
	<br/>
	<acme:button url="tutorial/delete.do?tutorialId=${tutorial.id}&conferenceId=${conferenceId}"
		name="edit" code="activity.delete" /><br/>
	</jstl:if>
</security:authorize>

<acme:button url="tutorial/list.do?conferenceId=${conferenceId}"
	name="back" code="activity.back" />

	
	<jstl:if test="${not empty error}">
	<h3 style="color: red;">
		<spring:message code="${error}" />
	</h3>
</jstl:if>
	