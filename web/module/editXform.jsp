<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<openmrs:require privilege="Edit xform" otherwise="/login.htm" redirect="/module/xformshelper/resolveError.form" />

<style>
 #buttonsAtBottom {
 	padding: 5px;
 }
</style>

<h2><spring:message code="xformshelper.resolveErrors.title"/></h2>	
<b class="boxHeader"><spring:message code="xformshelper.resolveErrors.action.editXform"/></b>
<form method="post" class="box">
	<c:choose>
		<c:when test="${errorObject == null}">
			<br/>
			<i>(<spring:message code="xformshelper.resolveErrors.null"/>)</i>
			<br/>
		</c:when>
		<c:otherwise>
			<textarea name="xmlString" rows="40" cols="118">${errorObject.formName}</textarea>
			<span id="buttonsAtBottom">
				<input type="submit" name="action" value='<spring:message code="xformshelper.resolveErrors.editXform.save"/>'/>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="submit" name="action" value='<spring:message code="xformshelper.resolveErrors.editXform.close"/>'/>
			</span>
			<br/>
		</c:otherwise>
	</c:choose>
</form>

<%@ include file="/WEB-INF/template/footer.jsp" %>