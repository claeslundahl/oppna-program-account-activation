<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<link href="css/style.css" type="text/css" />


<style type="text/css">

    a.buttonlink {
        background: #d4d4d4 url(/vgr-theme/images/portlet/header_bg.png) 0 0 repeat-x;
        border: 1px solid #dedede;
        border-color: #C8C9CA #9E9E9E #9E9E9E #C8C9CA;
        -moz-border-radius: 4px;
        -webkit-border-radius: 4px;
        border-radius: 4px;
        color: #34404f;
        cursor: pointer;
        font-weight: bold;
        line-height: 1.3;
        margin: 0 3px 0 0;
        padding: 5px;
        position: relative;
        text-decoration: none;
        text-shadow: 1px 1px #fff;
        width: auto;
    }

    a.buttonlink:hover {
        background: #B9CED9 url(/vgr-theme/images/application/state_hover_bg.png) 0 0 repeat-x;
        border-color: #A7CEDF;
        color: #336699;
        text-decoration: none;
    }

</style>

<portlet:renderURL var="renderUrl"/>
<p>
  <spring:message code="${message}" text="${message}" />
</p>
<p>
    <a class="buttonlink" href="${renderUrl}">Tillbaka</a>
</p>
