 <!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Ahmad Iqbal-->
<%@include file="/common/taglibs.jsp"%>
<menu:useMenuDisplayer name="Velocity" config="WEB-INF/classes/cssHorizontalMenu.vm" permissions="customRolesPermissionsAdapter">  
	<ul id="primary-nav" class="menuList">
  		<li class="pad">&nbsp;</li>
	    <menu:displayMenu name="i8_TransactionDetails"/>
	    <menu:displayMenu name="PG_Admin"/>
	    <menu:displayMenu name="Partner_Management"/>
	    <menu:displayMenu name="Agent_Hierarchy"/>
	    <menu:displayMenu name="mng_cash_payments"/>
	    <menu:displayMenu name="PG_Customers_Management"/>
		<menu:displayMenu name="Card_Management" />
	    <menu:displayMenu name="Configuration"/>
		<menu:displayMenu name="Bulk_Operations"/>
		
		<menu:displayMenu name="Chargeback_And_Adjustment"/>
	  	<menu:displayMenu name="Logs_Menu"/>
	 	<menu:displayMenu name="PG_Reports"/>
	  	<menu:displayMenu name="Search_Customer"/>
	  	<menu:displayMenu name="Complaints_And_Concerns"/>
		<menu:displayMenu name="Bank_Management"/>
		<menu:displayMenu name="Commission_Management"/>
		<menu:displayMenu name="Credit_Management"/>
		<menu:displayMenu name="Customer_Management"/>
		<menu:displayMenu name="Distribution_Management"/>
	
		<menu:displayMenu name="Miscellaneous"/>
		<menu:displayMenu name="MNO_Management"/>
		<menu:displayMenu name="Operator_Management"/>
		<menu:displayMenu name="Retailer_Information_Management"/>
		<menu:displayMenu name="Service_Management"/>
		<menu:displayMenu name="Supplier_Management"/>
		<menu:displayMenu name="Switch_Management"/>
		<menu:displayMenu name="OLA_Management"/>
		<menu:displayMenu name="Product_Payments"/>
		<menu:displayMenu name="All_Pay"/>
		<menu:displayMenu name="Retailer_Management"/>
		<menu:displayMenu name="Compliances"/>
		
	</ul>
</menu:useMenuDisplayer>

<script type="text/javascript">
/*<![CDATA[*/
var navItems = document.getElementById("primary-nav").getElementsByTagName("li");

for (var i=0; i<navItems.length; i++) {
    if(navItems[i].className == "menubar") {
        navItems[i].onmouseover=function() {
        	this.className += " over";
        	//Following code is to hide caret from text field when a menu is opened so that caret does not blink through menu(IE bug ID 1182)
        	if( document.readyState == "complete" )
        	{
       			var activeElement = document.activeElement;
        		if( 'undefined' != activeElement && ( "INPUT" == activeElement.nodeName || "SELECT" == activeElement.nodeName ) )
	        	{
	        		activeElement.blur();
	        	}
	        }
        }
        navItems[i].onmouseout=function() {
        	this.className = "menubar";
       	}
    }
}
/*]]>*/
</script>


