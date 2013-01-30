<ul id="menu">
	<li class="first">
		<a href="${pageContext.request.contextPath}/admin"><spring:message code="admin.title.short"/></a>
	</li>
	
	<openmrs:hasPrivilege privilege="View xform Properties">
		<li <c:if test='<%= request.getRequestURI().contains("xformshelper/properties") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/xformshelper/propertiesPage.form">
				<spring:message code="xformshelper.properties"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
	
	<openmrs:hasPrivilege privilege="Manage Downloadable forms">
		<li <c:if test='<%= request.getRequestURI().contains("xformshelper/downloadableXforms") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/xformshelper/downloadableXforms.list">
				<spring:message code="xformshelper.downloadableXforms"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
	
	<openmrs:hasPrivilege privilege="View xform Errors">
		<li <c:if test='<%= request.getRequestURI().contains("xformshelper/resolveError") %>'>class="active"</c:if>
			<c:if test='<%= request.getRequestURI().contains("xformshelper/editXform") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/xformshelper/resolveErrors.list">
				<spring:message code="xformshelper.resolveErrors.title"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
	
	<openmrs:hasPrivilege privilege="View xform Errors">
		<li <c:if test='<%= request.getRequestURI().contains("xformshelper/processingWarning") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/xformshelper/processingWarning.list">
				<spring:message code="xformshelper.processingWarning.title"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
	
	<openmrs:hasPrivilege privilege="View xform Properties">
		<li <c:if test='<%= request.getRequestURI().contains("xformshelper/syncLog") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/xformshelper/syncLog.list">
				<spring:message code="xformshelper.sync.title"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
</ul>