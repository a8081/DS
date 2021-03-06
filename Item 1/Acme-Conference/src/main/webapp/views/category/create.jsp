<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form action="category/administrator/edit.do" modelAttribute="category">

    <form:hidden path="id"/>
    <form:hidden path="version"/>
     
    
    <acme:textbox path="titleEn" code="category.titleEn"/>
    <br/>
    
    <acme:textbox path="titleEs" code="category.titleEs"/>
    <br/>
        <spring:message code="category.father"/>
    <form:select path="father" code="category.titleEn">
    	<jstl:forEach items="${categories}" var="cat">
    		<jstl:if test="${lang eq 'en' }" >
    			<form:option value="${cat}" label="${cat.titleEn}"/>
    		</jstl:if>
    		<jstl:if test="${lang eq 'es' }" >
    			<form:option value="${cat}" label="${cat.titleEs}"/>
    		</jstl:if>
    	</jstl:forEach>
    </form:select>

    <br/>
    <br/>
    
    
	
	<br/>
    <br/>
    <br/>
    
    <jstl:if test="${not empty msgerror  }">
    	<h5 style="color: red;"><spring:message code="${msgerror}"/></h5>
    </jstl:if>
  
	
    <br/>
    <br/>


    <button name="save" type="submit" class="button2">
        <spring:message code="category.save"/>
    </button>

    <input type="button" class="btn btn-danger" name="cancel"
           value="<spring:message code="category.cancel" />"
           onclick="relativeRedir('category/administrator/display.do?categoryId=${category.id}');"/>

</form:form>
