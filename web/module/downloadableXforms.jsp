<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Downloadable forms" otherwise="/login.htm" redirect="/module/xformshelper/downloadableXforms.list" />
	
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="xformshelper.downloadableXforms" /></h2>	

<form id="resourceAddForm" name="uploadForm" method="post" enctype="multipart/form-data">
	<div style="width: 50.5%; float: left; margin-left: 4px;">
		Select program to add form to:
		<select id="program" name="program">
			<option value="0">Select Program</option>
			<c:forEach items="${programs}" var="program">
				<option value="${program.id}">${program.name}</option>
			</c:forEach>
		</select>
		<br/>
		<br/>
	</div>
	
	<div style="width: 50.5%; float: left; margin-left: 4px;">
		<b class="boxHeader"><spring:message code="xformshelper.downloadableXforms.filesystem.add"/></b>
		<div class="box">
			<input type="file" name="resourceFile" size="40" />
			<input type="submit" name="action" value='<spring:message code="xformshelper.downloadableXforms.upload"/>'
				onClick="return (checkType() && checkProgram());"/>
		</div>
	</div>
	<div style="width: 48.5%; float: right; margin-left: 4px;">
		<b class="boxHeader"><spring:message code="xformshelper.downloadableXforms.database.add"/></b>
		<div class="box">
			<spring:message code="xformshelper.downloadableXforms.select"/>
			<select id="form" name="form">
				<c:forEach items="${forms}" var="form">
					<option value="${form.id}">${form.name}</option>
				</c:forEach>
			</select>
			<input type="submit" name="action" value='<spring:message code="xformshelper.downloadableXforms.loadXform"/>'
				onClick="return checkProgram();"/>
		</div>
	</div>
</form>

<br style="clear:both"/>
<br/>

<c:if test="${fn:length(downloadableXforms) > 0}">
	<div style="width=70%; margin-left: 4px;">
		<b class="boxHeader"><spring:message code="xformshelper.downloadableXforms.manage"/></b>
		<div class="box">
			<form method="post" name="resourcesForm" onSubmit="return checkSelected(this)">
				<table cellpadding="2" cellspacing="0" width="98%">
					<tr>
						<th></th>
						<th><spring:message code="xformshelper.downloadableXforms.formId"/></th>
						<th><spring:message code="general.name"/></th>
						<th><spring:message code="Program.program"/></th>
						<th><spring:message code="xformshelper.downloadableXforms.xform.uploadedOn"/></th>
					</tr>
					<c:forEach var="var" items="${downloadableXforms}" varStatus="status">
						<tr class="<c:choose><c:when test="${status.index % 2 == 0}">oddRow</c:when><c:otherwise>evenRow</c:otherwise></c:choose>">
							<td><input type="checkbox" name="xformsHelperXformId" value="${var.xformsHelperXformId}" onclick="clearError('xformsHelperXformId')"/></td>
							<td valign="top" style="white-space: nowrap">${var.xformId}</td>
							<td valign="top" style="white-space: nowrap">${var.xformName}</td>
							<td valign="top" style="white-space: nowrap">${var.programConcept.name}</td>
							<td valign="top">${var.xformMeta}</td>
						</tr>
					</c:forEach>
				</table>
				<input type="submit" name="action" value="<spring:message code='xformshelper.downloadableXforms.delete'/>"
					onclick="return confirm('<spring:message code="xformshelper.downloadableXforms.deleteWarning"/>');"/>
				<span class="error" id="xformsHelperXformIdError">Nothing is selected to delete!</span>
			</form>
		</div>
	</div>
</c:if>

<c:if test="${fn:length(downloadableXforms) == 0}">
	<i> &nbsp; <spring:message code="xformshelper.downloadableXforms.noUploaded"/></i><br/>
</c:if>

<br style="clear:both"/>
<br/>

<div style="margin-left: 4px;">
	<b class="boxHeader"><spring:message code="xformshelper.downloadableXforms.help" /></b>
	<div class="box">
		<ul>
			<li><i><spring:message code="xformshelper.downloadableXforms.help.main"/></i>
		</ul>
	</div>
</div>

<script type="text/javascript" language="JavaScript">
	clearError("xformsHelperXformId");

	function checkSelected(form) {
	    //get total number of CheckBoxes in form
	    var formLength = form.length;
	    var chkBoxCount = 0;
	    for (i=0;i<formLength;i++){
			if (form[i].type == 'checkbox') 
				chkBoxCount++;
		}

	    if (chkBoxCount==1) { //we dont have an array
	    	if (form.xformsHelperXformId.checked) 
	        {
	            //it's checked so return true and exit
	            return true;
	        }
	    }else {
		    //loop through each CheckBox
		    for (var i = 0; i < chkBoxCount; i++) 
		    {
		        if (form.xformsHelperXformId[i].checked) 
		        {
		            //it's checked so return true and exit
		            return true;
		        }
		    }
	    }
	    document.getElementById("xformsHelperXformIdError").style.display = "";
	    return false;
	}

	function clearError(errorName) {
		document.getElementById(errorName + "Error").style.display = "none";
	}
	
	function checkProgram() {
		var program = document.getElementById("program").value
		if (program==0) {
            alert('<spring:message code="Please select program to add form to" javaScriptEscape="true"/>');
			return false;
		}
		return true;
	}
	
	function checkType() {
		var extension = new Array();
		
		var fieldvalue = document.uploadForm.resourceFile.value;
		
		extension[0] = ".xml";
		extension[1] = ".xhtml";
		
		var thisext = fieldvalue.substr(fieldvalue.lastIndexOf('.'));
		for(var i = 0; i < extension.length; i++) {
			if(thisext == extension[i]) { return true; }
		}
		alert("Your upload form type is not permitted.");
		return false;
	}
</script>
<%@ include file="/WEB-INF/template/footer.jsp" %>