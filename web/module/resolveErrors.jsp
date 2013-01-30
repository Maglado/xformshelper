<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="View xform Errors" otherwise="/login.htm" redirect="/module/xformshelper/resolveErrors.list"/>

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp"%>

<style type="text/css">
	#resolveErrorsTable tr td .value {
	  font-weight: bold;
	}
	#resolveErrorsTable tr.secondRow {
	  border-bottom: 1px solid black;
	}
	.resolveButton {
		border: 1px solid gray;
		background-color: #E0E0F0;
		font-size: .75em;
		color: black;
		float: right;
		width: 52px;
		margin: 2px;
		padding: 1px;
		cursor: pointer;
	}.commentButton {
		border: 1px solid gray;
		background-color: lightpink;
		font-size: .75em;
		color: black;
		float: right;
		margin: 2px;
		padding: 1px;
		cursor: pointer;
	}
</style>

<h2><spring:message code="xformshelper.resolveErrors.title"/></h2>

<c:set var="errorSize" value="${fn:length(xformsErrors)}" />

<div>
	<b class="boxHeader"><spring:message code="xformshelper.resolveErrors.errors"/> (${errorSize})</b>
	<div class="box">
		<c:choose>
			<c:when test="${errorSize < 1}">
				<br/>
				<i>(<spring:message code="xformshelper.resolveErrors.empty"/>)</i>
				<br/>
			</c:when>
			<c:otherwise>
					<form method="post">
						<table cellpadding="8" cellspacing="0">
							<tr>
								<th><spring:message code="xformshelper.resolveErrors.errorId"/></th>
								<th><spring:message code="xformshelper.resolveErrors.error"/></th>
								<th><spring:message code="xformshelper.resolveErrors.errorDetails"/></th>
								<th><spring:message code="xformshelper.resolveErrors.formName"/></th>
								<th></th>
								<th></th>
							</tr>
							<c:forEach items="${xformsErrors}" var="error" >
								<tr>
									<td valign="top">${error.id}</td>
									<td>${error.error}</td>
									<td>${error.errorDetails}</td>
									<td>${error.formName}</td>
									<td>
										<c:set var="isCommented" value="${fn:length(error.comment)}" />
										<c:choose>
											<c:when test="${isCommented < 1}">
												<a href="resolveErrorComment.form?errorId=${error.id}"><input type="button" value='Comment' class="commentButton"/></a>
											</c:when>
											<c:otherwise>
												<a href="resolveError.form?errorId=${error.id}"><input type="button" value='Resolve' class="resolveButton"/></a>
											</c:otherwise>
										</c:choose>
									</td>
								</tr>
							</c:forEach>
						</table>
					</form>
			</c:otherwise>
		</c:choose>
	</div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>