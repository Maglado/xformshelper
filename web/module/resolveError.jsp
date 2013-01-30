<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Resolve xform Error" otherwise="/login.htm" redirect="/module/xformshelper/resolveErrors.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="localHeader.jsp"%>

<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />
<openmrs:htmlInclude file="/scripts/dojoConfig.js" />
<openmrs:htmlInclude file="/scripts/dojo/dojo.js" />

<h2>
	<spring:message code="xformshelper.resolveErrors.title" />
</h2>

<c:choose>
	<c:when test="${errorFormResolve == null}">
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
		
		<b class="boxHeader"><spring:message code="xformshelper.resolveErrors.title" />:</b>
		<div class="box">
			<form method="post" action="">
				<table cellpadding="3" cellspacing="0" width="100%" id="resolveErrorsTable">
					<tr>
						<td><b><spring:message code="xformshelper.commentOnError.title" />: </b>${errorFormResolve.comment}</td>
					</tr>
					<tr class="<c:choose><c:when test="${queueItemStatus.index % 2 == 0}">evenRow</c:when><c:otherwise>oddRow</c:otherwise></c:choose>">
						<td colspan="2">
							<!-- Info about the patient and encounter -->
							<spring:message code="xformshelper.commentOnError.commentor" />: 
							<span >
								${errorFormResolve.commentedBy.names}
								<spring:message code="xformshelper.commentOnError.date" />:
								${errorFormResolve.dateCommented}
							</span> <br/><br/>
							<spring:message code="Person.name" />: <span class="value">${errorFormResolve.name}</span> <br/>
							<spring:message code="Patient.identifier" />: <span class="value">${errorFormResolve.identifier}</span> <br/>
							<spring:message code="Person.gender" />: <span class="value">${errorFormResolve.gender}</span> <br/>
							<br/>
							<spring:message code="Encounter.location" />: <span class="value">${errorFormResolve.location}</span> <br/>
							<spring:message code="Encounter.datetime" />: <span class="value">${errorFormResolve.encounterDate}</span> <br/>
							<spring:message code="xformshelper.resolveErrors.formName" />: <span class="value">${errorFormResolve.formModelName} v${errorFormResolve.formId}</span> <br/>
							<br/>
							<spring:message code="xformshelper.resolveErrors.errorId" />: <span >${errorFormResolve.xformsErrorId}</span> <br/>
							<spring:message code="xformshelper.resolveErrors.errorDateCreated" />: <span >${errorFormResolve.dateCreated}</span> <br/>
							<spring:message code="xformshelper.resolveErrors.error" />: <span >${errorFormResolve.error}</span> <br/><br/>
							<b><spring:message code="xformshelper.resolveErrors.errorDetails" />: </b><div style="height: 40px; overflow-y: scroll; border: 1px solid #BBB;">${errorFormResolve.errorDetails}</div> <br/>
						</td>
					</tr>
					<tr class="secondRow <c:choose><c:when test="${queueItemStatus.index % 2 == 0}">evenRow</c:when><c:otherwise>oddRow</c:otherwise></c:choose>">
						<td>
							<input type="hidden" name="xformsErrorId" value="${errorFormResolve.xformsErrorId}"/>
							
							<!-- Assign a new patient identifier-->
							<input type="radio" name="errorItemAction" value="newIdentifier"/> <spring:message code="xformshelper.resolveErrors.action.newIdentifier"/>:
							<input type="text" name="patientIdentifier" onfocus="clickRadio('errorItemAction', 'newIdentifier')"/><br/>
							
							<!-- Assign a birth date to patient -->
							<input type="radio" name="errorItemAction" value="assignBirthdate" /> <spring:message code="xformshelper.resolveErrors.action.assignBirthDate"/>:
							<input type="text" name="birthDate" onClick="showCalendar(this)" onfocus="clickRadio('errorItemAction', 'assignBirthdate')"/><br/>
							
							<!-- Pick a provider -->
							<input type="radio" name="errorItemAction" value="linkProvider"/> <spring:message code="xformshelper.resolveErrors.action.providerLink"/>:
							<openmrs_tag:userField formFieldName="providerId" searchLabelCode="xformshelper.resolveErrors.action.findProvider" initialValue="" callback="clickRadio('errorItemAction', 'linkProvider')"/><br/>
							
							<!-- Assign a gender to patient -->
							<input type="radio" name="errorItemAction" value="assignGender"/> <spring:message code="xformshelper.resolveErrors.action.assignGender"/>:
							<select id="gender" name="gender" onfocus="clickRadio('errorItemAction', 'assignGender')">
								<option value="M">Male</option>
								<option value="F">Female</option>
							</select><br/>
						</td>
						<td >
							<!-- This is an invalid comment, delete it -->
							<input type="radio" name="errorItemAction" value="deleteComment" />	<spring:message code="xformshelper.resolveErrors.action.deleteComment"/> <br/>
							
							<!-- This is an invalid error, delete it -->
							<input type="radio" name="errorItemAction" value="deleteError" /> <spring:message code="xformshelper.resolveErrors.action.deleteError"/> <br/>

							<!-- I don't want to do anything to this one now -->
							<input type="radio" name="errorItemAction" value="noChange" checked="checked"/> <spring:message code="xformshelper.resolveErrors.action.noChange"/> <br/>
							
							<!-- Have the knowhow edit the xform -->
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="editXform.form?errorId=${errorFormResolve.xformsErrorId}"><spring:message code="xformshelper.resolveErrors.action.editXform"/></a><br/>
							
							<!-- Have the machinery, create a new patient -->
							<input type="radio" name="errorItemAction" value="createPatient" /> <spring:message code="xformshelper.resolveErrors.action.createPatient"/> <br/>
						</td>
					</tr>
					<tr class="thirdRow <c:choose><c:when test="${queueItemStatus.index % 2 == 0}">evenRow</c:when><c:otherwise>oddRow</c:otherwise></c:choose>">
						<td colspan="2"> 
							<!-- Assign Person Names -->
							<input type="radio" name="errorItemAction" value="assignNames" /> <spring:message code="xformshelper.resolveErrors.action.assignNames"/>:<br/>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<spring:message code="xformshelper.resolveErrors.action.familyName"/><input type="text" name="familyName" onfocus="clickRadio('errorItemAction', 'assignNames')"/>
							&nbsp;<spring:message code="xformshelper.resolveErrors.action.givenName"/><input type="text" name="givenName" onfocus="clickRadio('errorItemAction', 'assignNames')"/>
							&nbsp;<spring:message code="xformshelper.resolveErrors.action.middleName"/><input type="text" name="middleName" onfocus="clickRadio('errorItemAction', 'assignNames')"/><br/>
						</td>
					</tr>
					<tr>
						<td>
							<a href="javascript:void(0)" onClick="loadUrlIntoMessagePopup(
							'<spring:message code="xformshelper.resolveErrors.formDetails"/>', 
							'${pageContext.request.contextPath}/module/xformshelper/formDisplay.form?errorId=${errorFormResolve.xformsErrorId}'); return false;"><spring:message code="xformshelper.viewForm"/></a>
						</td>
						<td>
							<br/><input type="submit" name="action" value='<spring:message code="general.submit" />' />
						</td>
					</tr>
				</table>
			</form>
		</div>
	</c:otherwise>
</c:choose>

<script type="text/javascript">
	function clickRadio(radioName, value) {
	    var radio = document.getElementsByName(radioName);
	    var radioLength = radio.length;
	    for(var i = 0; i < radioLength; i++) {
	        radio[i].checked = false;
	        if(radio[i].value == value.toString()) {
	            radio[i].checked = true;
	        }
	    }
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

<br/>
<%@ include file="/WEB-INF/template/footer.jsp"%>