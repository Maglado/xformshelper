<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Comment on xform Errors" otherwise="/login.htm" redirect="/module/xformshelper/resolveErrors.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="localHeader.jsp"%>

<h2><spring:message code="xformshelper.resolveErrors.title" /></h2>

<c:choose>
	<c:when test="${errorFormComment == null}">
		<br/>
		<i>(<spring:message code="xformshelper.resolveErrors.errorLoading"/>)</i>
		<br/>
	</c:when>
	<c:otherwise>
		
		<style type="text/css">
			#resolveErrorsTable tr td .value {
			  font-weight: bold;
			}
			#resolveErrorsTable tr.secondRow {
			  border-bottom: 1px solid black;
			}
		</style>
		
		<b class="boxHeader"><spring:message code="xformshelper.commentOnError" />:</b>
		<div class="box">
			<form method="post" onSubmit="return validateForm()" >
				<spring:message code="xformshelper.resolveErrors.help" /> 
				<br/>
				<br/>
				<table cellpadding="3" cellspacing="0" width="100%" id="resolveErrorsTable">
					<tr>
						<td>
							<spring:message code="Person.name" />: <span class="value">${errorFormComment.name}</span> <br/>
							<spring:message code="Patient.identifier" />: <span class="value">${errorFormComment.identifier}</span> <br/>
							<spring:message code="Person.gender" />: <span class="value">${errorFormComment.gender}</span> <br/>
							<spring:message code="Encounter.location" />: <span class="value">${errorFormComment.location}</span> <br/>
							<spring:message code="Encounter.datetime" />: <span class="value">${errorFormComment.encounterDate}</span> <br/>
							<spring:message code="xformshelper.resolveErrors.formName" />: <span class="value">${errorFormComment.formModelName} v${errorFormComment.formId}</span> <br/>
							<br/>
							<spring:message code="xformshelper.resolveErrors.errorId" />: <span >${errorFormComment.xformsErrorId}</span> <br/>
							<spring:message code="xformshelper.resolveErrors.errorDateCreated" />: <span >${errorFormComment.dateCreated}</span> <br/>
							<spring:message code="xformshelper.resolveErrors.error" />: <span >${errorFormComment.error}</span> <br/> <br/>
							<b><spring:message code="xformshelper.resolveErrors.errorDetails" />:</b><div style="height: 40px; overflow-y: scroll; border: 1px solid #BBB;">${errorFormComment.errorDetails}</div> <br/>
							
						</td>
					</tr>
					<tr class="<c:choose><c:when test="${queueItemStatus.index % 2 == 0}">evenRow</c:when><c:otherwise>oddRow</c:otherwise></c:choose>">
						<td>
							<b><spring:message code="xformshelper.commentOnError.title" /></b><br/>
							<textarea rows="4" cols="60" id="comment" name="comment" onKeyUp="clearError('comment')"></textarea>
							<span class="error" id="commentError">Please enter a comment</span>
						</td>
					</tr>
					<tr class="<c:choose><c:when test="${queueItemStatus.index % 2 == 0}">evenRow</c:when><c:otherwise>oddRow</c:otherwise></c:choose>">
						<td>
							<a href="javascript:void(0)" onClick="loadUrlIntoMessagePopup(
								'<spring:message code="xformshelper.resolveErrors.formDetails"/>', 
								'${pageContext.request.contextPath}/module/xformshelper/formDisplay.form?errorId=${errorFormComment.xformsErrorId}'); return false;"><spring:message code="xformshelper.viewForm"/></a>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<input type="submit" name="action" value='<spring:message code="general.submit" />' />
						</td>
					</tr>
				</table>
			</form>
		</div>
	</c:otherwise>
</c:choose>
<br/>

<script type="text/javascript">
	clearError("comment");

	function validateForm() {
		var comment = document.getElementById("comment");
		
		if (comment.value == "") {
			document.getElementById("commentError").style.display = "";
			return false;
		}
		return true;
	}

	function clearError(errorName) {
		document.getElementById(errorName + "Error").style.display = "none";
	}
</script>

<script src="${pageContext.request.contextPath}/scripts/jquery/jquery-1.3.2.min.js" type="text/javascript" ></script>
<link href="${pageContext.request.contextPath}/scripts/jquery/dataTables/css/dataTables.css" type="text/css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/scripts/jquery/dataTables/js/jquery.dataTables.min.js" type="text/javascript" ></script>
<script src="${pageContext.request.contextPath}/scripts/jquery-ui/js/jquery-ui-1.7.2.custom.min.js" type="text/javascript" ></script>
<link href="${pageContext.request.contextPath}/scripts/jquery-ui/css/redmond/jquery-ui-1.7.2.custom.css" type="text/css" rel="stylesheet" />

<script type="text/javascript">
	var $j = jQuery.noConflict(); 
</script>

<div id="displayMessagePopup">
	<iframe id="displayMessagePopupIframe" width="100%" height="100%" marginWidth="0" marginHeight="0" frameBorder="0" scrolling="auto"></iframe>
</div>

<script type="text/javascript">
	$j(document).ready(function() {
		$j('#displayMessagePopup').dialog({
				title: 'dynamic',
				autoOpen: false,
				draggable: false,
				resizable: false,
				width: '70%',
				modal: true
		});
	});

	function loadUrlIntoMessagePopup(title, urlToLoad) {
		$j("#displayMessagePopupIframe").attr("src", urlToLoad);
		$j('#displayMessagePopup')
			.dialog('option', 'title', title)
			.dialog('option', 'height', $j(window).height() - 50) 
			.dialog('open');
	}
</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>