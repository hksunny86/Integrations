var jq = $.noConflict();
var isSaved=false;
		jq(document).ready(
	    	function($)
	      	{
				$('#body-content').on('change keyup keydown', 'input, textarea, select', function (e) {
					$(this).addClass('changed-input');
				});


				$(window).on('beforeunload', function () {
					if ( !isSaved) {
						if ($('.changed-input').length) {
							return 'You haven\'t saved your changes.';
						}
					}
				});

                jq("select[id$='appUserTypeId']").each( function () {
                    jq(this).trigger("change");
                });
	    	    jq(document).on('click', '#cardFeeConfigurationTable .addRow', function () {
	    	        var row = jq(this).closest('tr');
	    	        var clone = row.clone();
	    	        // clear the values
	    	        var tr = clone.closest('tr');
	    	        tr.find('input[type=text], select').val('');
	    	        tr.find('input[type=text], select').each(function() {
	    	        	this.style.borderColor="#e6e6e6";
	    	        });

	    	        tr.find('td:eq(0)').find("input:hidden").val('');
	    	        //alert(tr.siblings(":first").andSelf().find("input:hidden").val(''));

	    	        jq(this).closest('tr').after(clone);
	    	        resetIdAndNameAttributes();
	    	    });

	    	    jq(document).on('click', '#cardFeeConfigurationTable .removeRow', function () {
	    	        if (jq('#cardFeeConfigurationTable .addRow').length > 1) {
	    	            jq(this).closest('tr').remove();
	    	            //markRowDeleted();
	    	            resetIdAndNameAttributes();
	    	        }
	    	    });

  				jq(document).on('change', '#cardFeeConfigurationTable .textBox', function () {

	    	    	var selectId =  this.id;
                    /*var row = jq(this).closest('tr');
                    var selectArray = $(row).find('select');
                    selectArray[3].value='';
                    selectArray[3].style.display="table-row";
                    selectArray[4].style.display="table-row";*/
	    	    	if(selectId.indexOf("appUserTypeId") > -1){
		    	    	var row = jq(this).closest('tr');
		    	    	var selectArray = $(row).find('select');
                        selectArray[3].value='';
                        selectArray[3].style.display="table-row";
                        selectArray[4].style.display="table-row";
				    	/*if(selectArray[2].value == '2'){
				    		selectArray[4].value='';
				    		selectArray[3].style.display="table-row";
				    		selectArray[4].style.display="none";
				    	}
				    	else if(selectArray[2].value == '3'){
				    		selectArray[3].value='';
				    		selectArray[3].style.display="none";
				    		selectArray[4].style.display="table-row";
				    	}
				    	else{
				    		selectArray[3].style.display="table-row";
				    		selectArray[4].style.display="table-row";
				    	}*/
	    	    	}
	    	    });

	    	    function resetIdAndNameAttributes()
	    	    {
	    	    	jq('#cardFeeConfigurationTable tbody tr').each(
	    	    		function(index)
	    	    		{
	    	    			$(this).find(':input:not(:button)').each( function() {
	    	    					var name = $(this).attr('name');
	    	    					var listName = name.substr(0,name.indexOf("["));
	    	    					var propertyName = name.substr(name.indexOf("]")+2);
	    	    					name = listName + "[" + index + "]." + propertyName;
	    	    					var id = listName + index + "." + propertyName
	    	    					$(this).attr('id', id);
	    	    					$(this).attr('name', name);
    	    					}
	    	    			);
	    	    		}
	    	    	);
	    	    }

	    	    function resetForm()
	    	    {
	    	    	$('#cardFeeConfigurationTable tbody tr').find(':input:not(:button)').each( function() {
	    	    		this.style.borderColor="#e6e6e6";
	    	    	});
	    	    }

				$( "#btnSave" ).click(
					function()
					{
						var isValid = true;
						resetForm();
						$('#cardFeeConfigurationTable tbody tr').each( function(outerIndex) {
							var selectArray = $(this).find('select');

							if(selectArray[0].value.length == 0 )
							{
								isValid=false;
								selectArray[0].style.borderColor="red";
								alert('Please select Program Title.');
								return isValid;
							}
							if(selectArray[2].value.length == 0 )
							{
								isValid=false;
								selectArray[1].style.borderColor="red";
								alert('Please select Applicant Type.');
								return isValid;
							}

							if(selectArray[5].value.length == 0 )
							{
								isValid=false;
								selectArray[5].style.borderColor="red";
								alert('Please select Fee Type.');
								return isValid;
							}

							/*if(selectArray[0].value.length == 0 && selectArray[1].value.length == 0 && selectArray[2].value.length == 0 && selectArray[3].value.length == 0
								&& selectArray[4].value.length == 0 && selectArray[5].value.length == 0 && selectArray[6].value.length == 0)

							{
								isValid=false;
								selectArray.css("border-color","red");
								alert('Please select atleast one of the Category, Type, Applicant Type, Segment, Agent Network, Account Type or Fee Type.');
							}*/


							if(isValid)
							{

								var amount = $(this).find('input[id$="amount"]').val();

                                if(amount.length == 0 || (amount.indexOf('.') != -1) && (amount.substring(amount.indexOf('.')+1).length > 2) )
								{
                                    isValid = false;
                                    $(this).find('input[id$="amount"]').css("border-color","red");
                                    $(this).find('input[id$="amount"]').focus();
                                    alert("Please provide amount upto two decimal places.");
								}
								else if((amount.indexOf('.') != -1) && amount.substring(0,amount.indexOf('.')).length > 4)
								{
									isValid = false;
									$(this).find('input[id$="amount"]').css("border-color","red");
									$(this).find('input[id$="amount"]').focus();
									alert("Please provide amount upto four digits.");
								}
								else if((amount.indexOf('.') == -1) && amount.length > 4)
								{
									isValid = false;
									$(this).find('input[id$="amount"]').css("border-color","red");
									$(this).find('input[id$="amount"]').focus();
									alert("Please provide amount upto four digits.");
								}

							}
						});

						if(isValid)//This is the last validation check
						{
							 $('#cardFeeConfigurationTable tbody tr').each( function(outerIndex) {
			                        var outerSelectArray = $(this).find('select');
			                        $('#cardFeeConfigurationTable tbody tr').each( function(innerIndex) {
			                            if(outerIndex != innerIndex)
			                            {
			                                var innerSelectArray = $(this).find('select');
			                                if(outerSelectArray[0].value == innerSelectArray[0].value && outerSelectArray[1].value == innerSelectArray[1].value && outerSelectArray[2].value == innerSelectArray[2].value
			                                		&& outerSelectArray[3].value == innerSelectArray[3].value && outerSelectArray[4].value == innerSelectArray[4].value && outerSelectArray[5].value == innerSelectArray[5].value
			                                		&& outerSelectArray[6].value == innerSelectArray[6].value
											)
			                                {
			                                    isValid = false;
			                                    outerSelectArray.css("border-color","red");
			                                    innerSelectArray.css("border-color","red");

			                                    alert('Two rows cannot have same Card Program, Card Product Code, Applicant Type, Segment, Agent Network, Account Type or Fee Type.');
			                                    return isValid;
			                                }
			                            }
			                        });
			                        if(!isValid)
			                        {
			                            return false;
			                        }
			                    });
						}

						if(isValid)
						{
							isSaved=true;
							$('#cardFeeRuleVOForm').submit();
						}
					}
				);//end of btnSave click event

				$( "#btnRemoveAll" ).click(
					function()
					{
                        var cardFeeRuleId = $("#cardFeeConfigurationTable input[id$='cardFeeRuleId']").filter(":first").val();
                        if(cardFeeRuleId == undefined || cardFeeRuleId == "" )
                        {
                            alert("There are no debit card fee rules to delete.");
                        }
                        else
                        {
                            $( "#dialog-confirm" ).dialog({
                                resizable: false,
                                height:160,
                                width:340,
                                modal: true,
                                buttons: {
                                    "Remove All": function() {
                                        jq('#isUpdate').val(true);
                                        markAllRowsDeleted();
                                        $('#cardFeeRuleVOForm').submit();
                                    },
                                    Cancel: function() {
                                        $( this ).dialog( "close" );
                                    }
                                }
                            });
                        }
						isSaved=true;
                    }
				);//end of btnRemoveAll click event
	      	}
	    );

	function markAllRowsDeleted(){
        jq("#cardFeeConfigurationTable input[id$='isDeleted']").val(true);
	}
	function markRowDeleted(index){
			var s = index;
		//alert(document.getElementById("productChargesRuleModelList"+index+".isDeleted").value);
		//document.getElementById("productChargesRuleModelList"+index+".isDeleted").value=1;
	}
	/*function populateAccountTypes(obj)
	{
		var appuserType;
		if(jq(obj).val() == "")
		{
			jq(obj).closest("tr").find("select[id$='segmentId']").prop( "disabled", false );
			jq(obj).closest("tr").find("select[id$='distributorId']").prop( "disabled", false );
		}
		else
		{
			if(jq(obj).val() == CUSTOMER)
			{
				appuserType=2;
				jq(obj).closest("tr").find("select[id$='segmentId']").prop( "disabled", false );
				jq(obj).closest("tr").find("select[id$='distributorId']").prop( "disabled", false).val("");
			}
			else if(jq(obj).val() == RETAILER)
			{
				appuserType=3;
				jq(obj).closest("tr").find("select[id$='segmentId']").prop( "disabled", false ).val("");
				jq(obj).closest("tr").find("select[id$='distributorId']").prop( "disabled", false );
			}
		}

		var accountTypeId = jq(obj).closest("tr").find("select[id$='accountTypeId']").val();
		var test =jq(obj).closest("tr").find("select[id$='accountTypeId']").value;
		var test2=jq(obj).closest("tr").find("select[id$='accountTypeId']").val;

			jq.ajax({
				type: "POST",
				timeout: 50000,
				url: 'p_accountType.html',
				data: {"appUserTypeId":appuserType},
				dataType: 'xml',
				success: function (xml) {
					parse(xml , obj);
				},
				fail: function (result) {
					alert('No');
				}
			});
	}*/

/*
function parse(xml , obj){
	var mySel = jq(obj).closest("tr").find("select[id$='accountTypeId']");
	var selectedVal = mySel.val();
	var selectedItem = mySel.val();
	mySel.html('');
	mySel.append("<option value=''>" + "---Select---" + "</option>");
	jq(xml).find('item').each(function(){
		var name = jq(this).find('name').text();
		var value = jq(this).find('value').text();
		mySel.append("<option value=" +value + ">" + name + "</option>");
		mySel.val(selectedVal);
	});

}*/
