			<script language="javascript">createCell('SEP');</script>

			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<script language="javascript">createCell('XLS');</script>
			</authz:authorize>

			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<script language="javascript">;createCell('XLSX');</script>
			</authz:authorize>

			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<script language="javascript">createCell('CSV');</script>
			</authz:authorize>			
			
			<script language="javascript">createCell('WAIT')</script>	