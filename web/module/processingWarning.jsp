<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="View xform Errors" otherwise="/login.htm" redirect="/module/xformshelper/processingWarning.list"/>

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp"%>

<c:set var="warningsSize" value="${fn:length(warnings)}" />

<div>
	<b class="boxHeader"><spring:message code="xformshelper.processingWarning.title"/> (${warningsSize})</b>
	<div class="box">
		<c:choose>
			<c:when test="${warningsSize < 1}">
				<br/>&nbsp;&nbsp;<i>(<spring:message code="xformshelper.processingWarning.empty"/>)</i><br/>
			</c:when>
			<c:otherwise>
				<form method="post">
					<table cellpadding="4" cellspacing="0">
						<tr>
							<th></th>
							<th><u><spring:message code="xformshelper.processingWarning.warning"/></u></th>
							<th><u><spring:message code="xformshelper.processingWarning.message"/></u></th>
							<th><u><spring:message code="xformshelper.resolveErrors.formName"/></u></th>
							<th><u><spring:message code="xformshelper.processingWarning.dateCreated"/></u></th>
						</tr>
						<c:forEach items="${warnings}" var="warning" varStatus="varStatus">
							<tr class="<c:choose><c:when test="${varStatus.index % 2 == 0}">evenRow</c:when><c:otherwise>oddRow</c:otherwise></c:choose>">
								<td valign="top">${varStatus.index}</td>
								<td>${warning.error}</td>
								<td>${warning.errorDetails}</td>
								<td>${warning.formName}</td>
								<td><openmrs:formatDate date="${warning.dateCreated}" type="medium" /></td>
							</tr>
						</c:forEach>
					</table>
					<br/>
					<input type="submit" name="archive" value="<spring:message code='xformshelper.processingWarning.clear'/>" 
					onclick="return confirm('<spring:message code="xformshelper.processingWarning.clear.confirm"/>');"/>
				</form>
			</c:otherwise>
		</c:choose>
	</div>
</div>
<br/>
<%@ include file="/WEB-INF/template/footer.jsp" %>